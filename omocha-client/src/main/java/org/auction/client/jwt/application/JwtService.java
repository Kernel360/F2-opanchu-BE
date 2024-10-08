package org.auction.client.jwt.application;

import java.security.Key;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.auction.client.jwt.JwtCategory;
import org.auction.client.jwt.RefreshToken;
import org.auction.client.jwt.util.JwtGenerator;
import org.auction.client.jwt.util.JwtUtil;
import org.auction.client.member.application.MemberService;
import org.auction.domain.member.domain.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtGenerator jwtGenerator;
	private final JwtUtil jwtUtil;
	private final MemberService memberService;
	private final CustomUserDetailsService customUserDetailsService;

	@Value("${url.domain}")
	private String domain;
	@Value("${jwt.access_secret}")
	private String ACCESS_SECRET;
	@Value("${jwt.refresh_secret}")
	private String REFRESH_SECRET;
	private SecretKey accessKey;
	private SecretKey refreshKey;
	private final static long ACCESS_EXPIRATION = (long)1000 * 60 * 60;
	private final static long REFRESH_EXPIRATION = (long)1000 * 60 * 60 * 24;

	@PostConstruct
	public void init() {
		accessKey = Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes());
		refreshKey = Keys.hmacShaKeyFor(REFRESH_SECRET.getBytes());
	}

	public String generateAccessToken(
		HttpServletResponse response,
		MemberEntity memberEntity
	) {
		String accessToken = jwtGenerator.generateAccessToken(memberEntity, accessKey, ACCESS_EXPIRATION);
		ResponseCookie cookie = setTokenToCookie(JwtCategory.ACCESS.getValue(), accessToken);
		response.addHeader("Set-Cookie", cookie.toString());

		return accessToken;
	}

	public String generateRefreshToken(
		HttpServletResponse response,
		MemberEntity memberEntity
	) {
		String refreshToken = jwtGenerator.generateRefreshToken(memberEntity, refreshKey, REFRESH_EXPIRATION);
		RefreshToken.removeUserRefreshToken(memberEntity.getMemberId());
		RefreshToken.putRefreshToken(refreshToken, memberEntity.getMemberId());

		ResponseCookie cookie = setTokenToCookie(JwtCategory.REFRESH.getValue(), refreshToken);
		response.addHeader("Set-Cookie", cookie.toString());

		return refreshToken;
	}

	public ResponseCookie setTokenToCookie(
		String tokenPrefix,
		String token
	) {
		long maxAgeSeconds =
			(tokenPrefix.equals(JwtCategory.ACCESS.getValue())) ? ACCESS_EXPIRATION : REFRESH_EXPIRATION;

		return ResponseCookie.from(tokenPrefix, token)
			.path("/")
			.maxAge(maxAgeSeconds)
			.httpOnly(true)
			.domain(StringUtils.hasText(domain) ? domain : null)
			.sameSite("None")
			.secure(true)
			.build();
	}

	public boolean validateAccessToken(
		String token
	) {
		return jwtUtil.validateToken(token, accessKey);
	}

	public boolean validateRefreshToken(
		final String token
	) {
		return jwtUtil.validateToken(token, refreshKey);
	}

	public String resolveTokenFromCookie(
		HttpServletRequest request,
		JwtCategory tokenPrefix
	) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			// throw new JwtTokenNotFoundException(JWT_TOKEN_NOT_FOUND);
			return null;
		}
		return jwtUtil.resolveTokenFromCookie(cookies, tokenPrefix);
	}

	public Authentication getAuthentication(
		String token
	) {
		UserDetails principal = customUserDetailsService.loadUserByUsername(getUserPk(token, accessKey));
		return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
	}

	private String getUserPk(
		String token,
		Key secretKey
	) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public MemberEntity findMemberByRefreshToken(
		String refreshToken
	) {
		Long memberId = RefreshToken.findMemberIdByRefreshToken(refreshToken);
		return memberService.findMemberByMemberId(memberId);
	}

	// TODO: AccessToken에서 Claim 데이터 가져오는 메서드 (추후 사용을 위해 남겨놓음)
	private <T> T findClaimFromToken(
		String token,
		Key secretKey,
		Function<Claims, T> claimsResolver
	) {
		if (jwtUtil.validateToken(token, secretKey)) {
			return null;
		}

		final Claims claims = Jwts.parserBuilder()
			.setSigningKey(accessKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claimsResolver.apply(claims);
	}

	public void logout(
		MemberEntity requestMember,
		HttpServletResponse response
	) {
		RefreshToken.removeUserRefreshToken(requestMember.getMemberId());

		Cookie accessCookie = jwtUtil.resetToken(JwtCategory.ACCESS);
		Cookie refreshCookie = jwtUtil.resetToken(JwtCategory.REFRESH);

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
	}
}

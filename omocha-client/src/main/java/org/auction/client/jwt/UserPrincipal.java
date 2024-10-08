package org.auction.client.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.auction.domain.member.domain.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

	private final MemberEntity memberEntity;
	private Map<String, Object> attributes;

	public UserPrincipal(MemberEntity memberEntity) {
		this.memberEntity = memberEntity;
	}

	public UserPrincipal(MemberEntity memberEntity, Map<String, Object> attributes) {
		this.memberEntity = memberEntity;
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(memberEntity.getRole().name()));
	}

	// TODO : google은 nickname을 반환안하고, naver는 nickname을 반환함. 추후 고민해야함
	@Override
	public String getName() {
		return memberEntity.getNickname();
	}

	public Long getId() {
		return memberEntity.getMemberId();
	}

	@Override
	public String getUsername() {
		return memberEntity.getUsername();
	}

	@Override
	public String getPassword() {
		return memberEntity.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

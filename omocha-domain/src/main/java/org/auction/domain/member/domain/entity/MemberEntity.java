package org.auction.domain.member.domain.entity;

import java.util.Collection;
import java.util.Collections;

import org.auction.domain.common.domain.entity.TimeTrackableEntity;
import org.auction.domain.member.domain.enums.Role;
import org.auction.domain.member.domain.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends TimeTrackableEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "login_id")
	private String loginId;

	// TODO: Password VO로 변경해야함
	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "birth")
	private String birth;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "user_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	@Builder
	public MemberEntity(
		String loginId, String password, String nickname,
		String birth, String email, String phoneNumber,
		String profileImageUrl, Role role, UserStatus userStatus
	) {
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.birth = birth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.profileImageUrl = profileImageUrl;
		this.role = role;
		this.userStatus = userStatus;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return loginId;
	}

	@Override
	public String getPassword() {
		return password;
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

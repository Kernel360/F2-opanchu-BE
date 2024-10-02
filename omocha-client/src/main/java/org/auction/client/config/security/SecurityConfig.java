package org.auction.client.config.security;

import org.auction.client.config.security.filter.JwtAuthFilter;
import org.auction.client.config.security.handler.CustomAccessDeniedHandler;
import org.auction.client.config.security.handler.CustomAuthenticationEntryPointHandler;
import org.auction.domain.member.domain.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomCorsConfig customCorsConfig;
	private final JwtAuthFilter jwtAuthFilter;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;

	//TODO: 정적 자원 접근에러 파악해야함
	public static final String[] PERMITTED_ALL_URI = {
		"/swagger-ui/**", "/v3/api-docs/*"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors(corsCustomizer -> corsCustomizer.configurationSource(customCorsConfig))
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(HttpBasicConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PERMITTED_ALL_URI).permitAll()
				.requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/v1/auction/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/v1/auction").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/auction").hasRole(Role.ROLE_USER.getRole())
				.anyRequest().authenticated()
			)

			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

			.exceptionHandling(conf -> conf
				.accessDeniedHandler(customAccessDeniedHandler)
				.authenticationEntryPoint(customAuthenticationEntryPointHandler)
			);

		return http.build();
	}
}

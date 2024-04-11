package com.br.lab.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.br.lab.keycloak.converter.JwtConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtConverter jwtConverter;

	public SecurityConfig(JwtConverter jwtConverter) {
		this.jwtConverter = jwtConverter;
	}

	@Bean
	SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(new AntPathRequestMatcher("/private/customers*")).hasRole("update_costumer_web")
				.requestMatchers(new AntPathRequestMatcher("/public/hello")).permitAll()
				.anyRequest().authenticated());

		http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

		return http.build();
	}

}

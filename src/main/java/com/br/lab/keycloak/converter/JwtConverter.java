package com.br.lab.keycloak.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = Stream
				.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
				.collect(Collectors.toSet());
		return new JwtAuthenticationToken(jwt, authorities);
	}

	@SuppressWarnings("unchecked")
	private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {

		Map<String, Object> realmAccess = jwt.getClaim("realm_access"); // Recupera as Realm Roles

		Collection<String> realmRoles = Collections.emptyList();

		if (realmAccess != null) {
			realmRoles = (Collection<String>) realmAccess.get("roles");
		}

		Map<String, Object> resourceAccess = jwt.getClaim("resource_access"); // Recupera as Client Roles

		Collection<String> appClientRoles = Collections.emptyList();

		if (resourceAccess != null) {
			Map<String, Object> loginApp = (Map<String, Object>) resourceAccess.get("login-app");

			if (loginApp != null) {
				appClientRoles = (Collection<String>) loginApp.get("roles");
			}
		}

		return Stream.concat(realmRoles.stream(), appClientRoles.stream())
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());

	}

}

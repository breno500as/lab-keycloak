package com.br.lab.keycloak.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import com.br.lab.keycloak.config.JwtConverterProperties;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	    private final JwtConverterProperties properties;

	    public JwtConverter(JwtConverterProperties properties) {
	        this.properties = properties;
	    }

	    @Override
	    public AbstractAuthenticationToken convert(Jwt jwt) {
	        Collection<GrantedAuthority> authorities = Stream.concat(
	                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
	                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
	        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
	    }

	    private String getPrincipalClaimName(Jwt jwt) {
	        String claimName = JwtClaimNames.SUB;
	        if (properties.getPrincipalAttribute() != null) {
	            claimName = properties.getPrincipalAttribute();
	        }
	        return jwt.getClaim(claimName);
	    }

	    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
	    	
	        List<String> groupsRoles = jwt.getClaim("groups");
	    	
	        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
	        Map<String, Object> resource;
	        Collection<String> resourceRoles = Collections.emptyList();
	        
	        if (resourceAccess != null) {
	        	
	        	resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId());
	        	
	        	if (resource != null) {
	        		resourceRoles = (Collection<String>) resource.get("roles");
	        	}
	        	
	        }
	        
	 
	        return Stream.concat(groupsRoles.stream(), resourceRoles.stream())
	                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
	                .collect(Collectors.toSet());
	    }

}

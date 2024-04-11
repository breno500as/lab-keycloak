package com.br.lab.keycloak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${cors.origin.patterns:default}")
	private String corsOriginPatterns;

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		  registry.addMapping("/**").allowedOrigins("http://localhost:4200").allowedMethods("*")
		  .allowCredentials(true);

	//	registry.addMapping("/**").allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
	//			.allowedOrigins(this.corsOriginPatterns.split("http://127.0.0.1:4200")).allowCredentials(true);

	}

}

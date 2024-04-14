package com.br.lab.keycloak.controller;

import java.security.Principal;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

	private Logger log = LoggerFactory.getLogger(PrivateController.class);

	@GetMapping("/customers")
	public String customers(Principal principal) {

		var authentication = SecurityContextHolder.getContext().getAuthentication();

		var roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

		log.info(principal.getName());

		log.info(roles.stream().collect(Collectors.joining(" , ")));

		return "customers";
	}

}

package com.br.lab.keycloak.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

	@SuppressWarnings("unused")
	@GetMapping("/customers")
	public String customers(Principal principal, Model model) {

		var authentication = SecurityContextHolder.getContext().getAuthentication();

		var roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

		model.addAttribute("customers", Collections.emptyList());
		model.addAttribute("username", principal.getName());
		return "customers";
	}

	 
}

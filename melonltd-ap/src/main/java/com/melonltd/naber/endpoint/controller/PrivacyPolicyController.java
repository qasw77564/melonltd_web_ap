package com.melonltd.naber.endpoint.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "" })
public class PrivacyPolicyController {

	
	
	@GetMapping(value = "naber/privac/policy")
	public ModelAndView privacPolicy(HttpServletRequest req, HttpServletResponse resp) {
		return new ModelAndView("policy");
//		return "index";
	}
}
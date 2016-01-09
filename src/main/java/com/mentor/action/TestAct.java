package com.mentor.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestAct {

	
	@RequestMapping(value="/test/hello.jhtml")
	public String hello(HttpServletRequest request,ModelMap model){
		System.out.println("hello action");
		return "/test/hello";
	}
	
	
	
	
}

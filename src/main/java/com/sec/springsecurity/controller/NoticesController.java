package com.sec.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class NoticesController {

	@GetMapping("/notices")
	public String getNotice() {
		return  "Here are the notices details from the DB";
	}
}

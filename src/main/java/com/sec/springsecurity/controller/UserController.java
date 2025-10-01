package com.sec.springsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sec.springsecurity.entity.Customer;
import com.sec.springsecurity.repo.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final PasswordEncoder passwordEncoder;
	private final CustomerRepository customerRepository;

	@PostMapping("/register")
	 public ResponseEntity<String> registerUser(@RequestBody Customer customer){
		 
		 String hashPwd=passwordEncoder.encode(customer.getPwd());
		 customer.setPwd(hashPwd);
		 
		 Customer saveCustomer=customerRepository.save(customer);
		 
		 if(saveCustomer.getId()>0) {
			 return ResponseEntity.status(HttpStatus.CREATED).
                     body("Given user details are successfully registered");
		 }else {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                     body("User registration failed");
		 }
		 
	 }
}

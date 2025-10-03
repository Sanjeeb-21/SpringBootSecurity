package com.sec.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrfConfig -> csrfConfig.disable())
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
						.requestMatchers("/notices", "/contact", "/error", "/register", "/css/**", "/js/**",
								"/images/**", "/favicon.ico")
						.permitAll())

				.formLogin(Customizer.withDefaults()) // browser login form
				.httpBasic(Customizer.withDefaults()); // API basic auth;

		return http.build();
	}

	// The {bcrypt} part is not random text—it’s a prefix added by
	// DelegatingPasswordEncoder to indicate which algorithm was used for hashing.

//	@Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

	// The {bcrypt} prefix is automatically added by Spring Security’s
	// DelegatingPasswordEncoder to indicate which algorithm was used.
	// If you use new BCryptPasswordEncoder(), the prefix won’t be added.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // No {bcrypt} prefix
	}

	/*
	 * @Bean public AuthenticationManager authManager(HttpSecurity http) throws
	 * Exception { AuthenticationManagerBuilder authBuilder =
	 * http.getSharedObject(AuthenticationManagerBuilder.class);
	 * 
	 * // Custom provider: customer table
	 * authBuilder.userDetailsService(easyBankUserDetailsService)
	 * .passwordEncoder(passwordEncoder());
	 * 
	 * // Default JDBC provider: users + authorities tables
	 * authBuilder.jdbcAuthentication() .dataSource(dataSource)
	 * .passwordEncoder(passwordEncoder());
	 * 
	 * return authBuilder.build(); }
	 */

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hash = encoder.encode("EazyBytes@12345");

		System.out.println(hash);
	}
}

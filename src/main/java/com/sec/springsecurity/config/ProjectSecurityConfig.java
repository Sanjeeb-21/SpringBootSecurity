package com.sec.springsecurity.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sec.springsecurity.service.EasyBankUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {

	private final EasyBankUserDetailsService easyBankUserDetailsService;
    private final DataSource dataSource;
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrfConfig -> csrfConfig.disable())
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
						.requestMatchers("/notices", "/contact", "/error", "/register","/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll())

				.formLogin(Customizer.withDefaults()) // browser login form
				.httpBasic(Customizer.withDefaults()); // API basic auth;

		return http.build();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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

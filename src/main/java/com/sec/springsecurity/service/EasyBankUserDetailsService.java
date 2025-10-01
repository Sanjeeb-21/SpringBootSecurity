package com.sec.springsecurity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sec.springsecurity.entity.Customer;
import com.sec.springsecurity.repo.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EasyBankUserDetailsService implements UserDetailsService {

	private final CustomerRepository customerRepository;

	private final JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//			Customer customer=customerRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User details not found for the user: " + username));
//			List<GrantedAuthority> authorities=List.of(new SimpleGrantedAuthority("ROLE_"+customer.getRole()));
//			
//			return new User(customer.getEmail(), customer.getPwd(),authorities);

		Optional<Customer> customer = customerRepository.findByEmail(username);
		if (customer.isPresent()) {
			Customer c = customer.get();
			return new User(c.getEmail(), c.getPwd(), List.of(new SimpleGrantedAuthority("ROLE_" + c.getRole())));
		}

		// 2️⃣ Try users + authorities tables
		try {
			UserDetails user = jdbcTemplate.queryForObject(
					"select username, password, enabled from users where username=?",
					(rs, rowNum) -> User.withUsername(rs.getString("username")).password(rs.getString("password"))
							.disabled(!rs.getBoolean("enabled")).authorities(getAuthorities(rs.getString("username")))
							.build(),
					username);
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("User not found in customer or users table: " + username);
		}
	}

	private List<GrantedAuthority> getAuthorities(String username) {
		return jdbcTemplate.query("select authority from authorities where username=?",
				(rs, rowNum) -> new SimpleGrantedAuthority(rs.getString("authority")), username);
	}
}

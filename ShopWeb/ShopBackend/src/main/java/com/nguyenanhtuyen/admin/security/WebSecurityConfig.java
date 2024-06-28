package com.nguyenanhtuyen.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
         	.csrf(AbstractHttpConfigurer::disable)
         	.authorizeHttpRequests(requests -> {
         		requests
                 	.requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
                 	.requestMatchers("/users/**").hasAuthority("Admin")
                 	.requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
                 	.requestMatchers("/brands/**").hasAnyAuthority("Admin", "Editor")
                 	.requestMatchers("/products/**").hasAnyAuthority("Admin", "Sales", "Editor", "Shipper")
                 	.requestMatchers("/questions/**").hasAnyAuthority("Admin", "Assistant")
                 	.requestMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")
                 	.requestMatchers("/customers/**").hasAnyAuthority("Admin", "Sales")
                 	.requestMatchers("/shipping/**").hasAnyAuthority("Admin", "Sales")
                 	.requestMatchers("/orders/**").hasAnyAuthority("Admin", "Sales", "Shipper")
                 	.requestMatchers("/report/**").hasAnyAuthority("Admin", "Sales")
                 	.requestMatchers("/articles/**").hasAnyAuthority("Admin", "Editor")
                 	.requestMatchers("/menus/**").hasAnyAuthority("Admin", "Editor")
                 	.requestMatchers("/settings/**").hasAuthority("Admin")
                 	.anyRequest().authenticated();
         	})
         	.formLogin(form -> form
         			.loginPage("/login")
         			.defaultSuccessUrl("/", true)
         			.usernameParameter("email")
         			.permitAll()
         			)
         	.authenticationProvider(authenticationProvider());
		 
		 return http.build();
	}

}

package com.oneplat.oap.mgmt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
		.and()
		.authorizeRequests()
			.antMatchers("/users/{userId}").access("@authenticationCheckHandler.checkUserId(authentication,#userId)")
			.antMatchers("/admin/db/**").access("hasRole('ADMIN_MASTER') or hasRole('ADMIN') and hasRole('DBA')")
			.antMatchers("/register/**").hasRole("ANONYMOUS")
		.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.passwordParameter("password")
			.permitAll();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
}
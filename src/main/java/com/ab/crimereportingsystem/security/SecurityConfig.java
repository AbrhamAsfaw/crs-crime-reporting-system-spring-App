package com.ab.crimereportingsystem.security;

import com.ab.crimereportingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService userService;
	
	  @Override
	  protected void configure(AuthenticationManagerBuilder auth)
	      throws Exception {
		  auth.userDetailsService(userService)
	          .passwordEncoder(bCryptPasswordEncoder);
	  } 
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/super/signup").hasAuthority("SUPER")
        	.antMatchers("/user/comment/**").hasAnyAuthority("USER","ADMIN")
        	.antMatchers("/admin","/admin/**").hasAuthority("ADMIN")
            .antMatchers("/user","/user/**").hasAuthority("USER")
            .anyRequest()
            .permitAll()
	        .and()
		     	.formLogin()
		     			.loginPage("/login")
		     			.failureUrl("/login?error=true")
		     			.defaultSuccessUrl("/default")
		    .and()
		     	.logout()
		     			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		     			.logoutSuccessUrl("/")
		 	.and()
		     	.exceptionHandling()
		     	.accessDeniedPage("/access-denied")
		     ;
    }

}

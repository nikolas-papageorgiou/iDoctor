package com.iDoctor.iDoctor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                        .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/signup", "/login","/index","/","/css/**","/js/**","/assets/**").permitAll() //Permit all access to /register and /login
                        .anyRequest().authenticated() // Secure all other requests
                )
                .formLogin(form -> form
                        .loginPage("/login")               //We need to create a controller for this request mapping "/login
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/login?logout")
                ).headers().disable();

        //disable CSRF
        http.csrf(csrf->csrf.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }



}


/*
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(configurer->configurer
            .requestMatchers(HttpMethod.GET,"/idoctor/patients").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.GET,"/idoctor/patients/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.POST,"/idoctor/patients").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.PUT,"/idoctor/patients/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.DELETE,"/idoctor/patients/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET,"/hello").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.GET,"/patientForm").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.POST,"/patientConfirmation").hasRole("DOCTOR")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
    );
    //use HTTP Basic authentication
    http.httpBasic(Customizer.withDefaults());
    //disable CSRF
    http.csrf(csrf->csrf.disable());

    return http.build();
}*/
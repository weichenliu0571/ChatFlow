package com.chatflow.chatflow.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
        .requestMatchers("/ws/**").permitAll()
        .requestMatchers("/users/suggest").authenticated()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/login") /* custom login page */
        .loginProcessingUrl("/login") /* tells spring boot to deal with the route /login as login */
        .defaultSuccessUrl("/", true)
        .failureUrl("/login?error=true")
        .permitAll()
      )
      .logout(logout -> logout
        .logoutUrl("/logout") /* tells spring boot to deal with the route /logout as logout */
        .logoutSuccessUrl("/login?logout")
      )
      .csrf(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsManager userDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource); /* spring boot automatically injects datasource with the default db we have set up */
  }
}

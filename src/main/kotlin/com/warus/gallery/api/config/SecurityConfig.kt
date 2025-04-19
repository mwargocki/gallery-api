package com.warus.gallery.api.config

import com.warus.gallery.api.filter.JwtAuthenticationFilter
import com.warus.gallery.api.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
   private val jwtFilter: JwtAuthenticationFilter,
   private val userDetailsService: CustomUserDetailsService
) {

   @Bean
   fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
      return http
         .csrf { it.disable() }
         .cors { }
         .authorizeHttpRequests {
            it
               .requestMatchers("/auth/**").permitAll()
               .requestMatchers("/actuator/**").permitAll() //http://localhost:8080/actuator
               .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() //http://localhost:8080/swagger-ui/index.html#
               .requestMatchers(HttpMethod.GET, "/api/photos/**", "/api/filters/**", "/images/**").permitAll()
               .anyRequest().authenticated()
         }
         .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
         .userDetailsService(userDetailsService)
         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
         .build()
   }

   @Bean
   fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
      config.authenticationManager

   @Bean
   fun passwordEncoder(): PasswordEncoder {
      return BCryptPasswordEncoder()
   }
}

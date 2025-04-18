package com.warus.gallery.api.filter

import com.warus.gallery.api.service.CustomUserDetailsService
import com.warus.gallery.api.service.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
   private val jwtUtils: JwtUtils,
   private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

   override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain
   ) {
      val authHeader = request.getHeader("Authorization")
      val token = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

      if (!token.isNullOrBlank() && jwtUtils.validateToken(token)) {
         val username = jwtUtils.getUsername(token)
         val userDetails = userDetailsService.loadUserByUsername(username)

         val authentication = UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
         )
         authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
         SecurityContextHolder.getContext().authentication = authentication
      }

      filterChain.doFilter(request, response)
   }
}
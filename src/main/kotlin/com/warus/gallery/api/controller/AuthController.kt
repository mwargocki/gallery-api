package com.warus.gallery.api.controller

import com.warus.gallery.api.model.LoginRequest
import com.warus.gallery.api.model.LoginResponse
import com.warus.gallery.api.service.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
   private val authManager: AuthenticationManager,
   private val jwtUtils: JwtUtils
) {

   @PostMapping("/login")
   fun login(@RequestBody request: LoginRequest): LoginResponse {
      try {
         val authentication = UsernamePasswordAuthenticationToken(request.username, request.password)
         authManager.authenticate(authentication)

         val token = jwtUtils.generateToken(request.username)
         return LoginResponse(token)
      } catch (ex: AuthenticationException) {
         throw RuntimeException("Invalid credentials")
      }
   }
}

package com.warus.gallery.api.service

import com.warus.gallery.api.db.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
   private val userRepository: UserRepository
) : UserDetailsService {

   override fun loadUserByUsername(username: String): UserDetails {
      val user = userRepository.findByUsername(username)
         ?: throw UsernameNotFoundException("User not found: $username")

      return User(
         user.username,
         user.password,
         user.roles.split(",").map { SimpleGrantedAuthority("ROLE_$it") }
      )
   }
}

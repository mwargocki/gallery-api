package com.warus.gallery.api.service

import com.warus.gallery.api.db.model.User
import com.warus.gallery.api.db.repository.UserRepository
import com.warus.gallery.api.model.CreateUserRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
   private val userRepository: UserRepository,
   private val passwordEncoder: PasswordEncoder
) {

   fun createUser(request: CreateUserRequest): User {
      if (userRepository.findByUsername(request.username) != null) {
         throw IllegalArgumentException("Username already exists")
      }

      val encodedPassword = passwordEncoder.encode(request.password)

      val user = User(
         username = request.username,
         password = encodedPassword,
         roles = request.roles.uppercase() // np. "ADMIN" lub "USER"
      )

      return userRepository.save(user)
   }
}

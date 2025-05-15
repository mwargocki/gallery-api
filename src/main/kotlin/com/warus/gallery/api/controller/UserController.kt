package com.warus.gallery.api.controller

import com.warus.gallery.api.exception.ValidationException
import com.warus.gallery.api.model.CreateUserRequest
import com.warus.gallery.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
private class UserController(
   private val userService: UserService
) {

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   fun createUser(@RequestBody request: CreateUserRequest) {
      validate(request)
      userService.createUser(request)
   }

   private fun validate(request: CreateUserRequest) {
      if (request.username.isBlank() || request.password.isBlank()) {
         throw ValidationException("Username and password can't be blank")
      }
   }
}

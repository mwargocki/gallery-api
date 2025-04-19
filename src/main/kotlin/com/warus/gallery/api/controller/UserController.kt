package com.warus.gallery.api.controller

import com.warus.gallery.api.db.model.User
import com.warus.gallery.api.model.CreateUserRequest
import com.warus.gallery.api.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
private class UserController(
   private val userService: UserService
) {

   @PostMapping
   fun createUser(@RequestBody request: CreateUserRequest): User {
      return userService.createUser(request)
   }
}

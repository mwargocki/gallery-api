package com.warus.gallery.api.model

data class CreateUserRequest(
   val username: String,
   val password: String,
   val roles: String // eg. "USER" or "ADMIN"
)

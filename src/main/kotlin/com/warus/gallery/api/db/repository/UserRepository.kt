package com.warus.gallery.api.db.repository

import com.warus.gallery.api.db.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
   fun findByUsername(username: String): User?
}

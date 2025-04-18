package com.warus.gallery.api.db.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   val id: Long = 0,

   @Column(nullable = false, unique = true)
   val username: String,

   @Column(nullable = false)
   val password: String,

   @Column(nullable = false)
   val roles: String
)

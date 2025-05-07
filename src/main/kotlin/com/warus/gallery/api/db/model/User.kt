package com.warus.gallery.api.db.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
   @Column(nullable = false, unique = true)
   val username: String,

   @Column(nullable = false)
   val password: String,

   @Column(nullable = false)
   val roles: String,

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   var id: Long? = null
)

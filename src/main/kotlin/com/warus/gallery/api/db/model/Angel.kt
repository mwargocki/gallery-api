package com.warus.gallery.api.db.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "angels")
data class Angel(

   @Column(nullable = false)
   val height: Double,

   @Column(nullable = false)
   val material: String,

   @Column(nullable = false)
   val color: String,

   @Column(nullable = false)
   val type: String,

   @Column(name = "created_at")
   val createdAt: LocalDateTime = LocalDateTime.now(),

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   var id: Long? = null
)

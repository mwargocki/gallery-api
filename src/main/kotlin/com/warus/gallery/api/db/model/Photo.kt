package com.warus.gallery.api.db.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "photos")
data class Photo(
   @Column(name = "file_name", nullable = false)
   val filename: String,

   @Column(name = "thumbnail")
   val thumbnail: String? = null,

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

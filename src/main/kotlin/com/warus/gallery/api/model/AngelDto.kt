package com.warus.gallery.api.model

import java.time.LocalDateTime

data class AngelDto(
    val id: Long,
    val color: String,
    val material: String,
    val type: String,
    val height: Double,
    val thumbnail: String?,
    val createdAt: LocalDateTime
)

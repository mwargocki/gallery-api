package com.warus.gallery.api.model

data class PhotoUpdateRequest(
   val height: Int,
   val material: String,
   val color: String,
   val type: String
)

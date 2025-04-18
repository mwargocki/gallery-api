package com.warus.gallery.api.service

import com.warus.gallery.api.db.repository.PhotoRepository
import org.springframework.stereotype.Service

@Service
class FilterService(
   private val photoRepository: PhotoRepository
) {
   fun getAllColors(): List<String> = photoRepository.findAllDistinctColors()

   fun getAllTypes(): List<String> = photoRepository.findAllDistinctTypes()

   fun getAllMaterials(): List<String> = photoRepository.findAllDistinctMaterials()
}

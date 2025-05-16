package com.warus.gallery.api.service

import com.warus.gallery.api.db.repository.AngelRepository
import org.springframework.stereotype.Service

@Service
class FilterService(
   private val angelRepository: AngelRepository
) {
   fun getAllColors(): List<String> = angelRepository.findAllDistinctColors()

   fun getAllTypes(): List<String> = angelRepository.findAllDistinctTypes()

   fun getAllMaterials(): List<String> = angelRepository.findAllDistinctMaterials()
}

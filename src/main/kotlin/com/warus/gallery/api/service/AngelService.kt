package com.warus.gallery.api.service

import com.warus.gallery.api.config.UploadProperties
import com.warus.gallery.api.db.model.Angel
import com.warus.gallery.api.db.repository.AngelRepository
import com.warus.gallery.api.model.AngelDto
import com.warus.gallery.api.model.AngelUpdateRequest
import com.warus.gallery.api.model.AngelUploadRequest
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class AngelService(
   private val angelRepository: AngelRepository,
   private val photoStorageService: PhotoStorageService
) {

   @Transactional
   fun save(request: AngelUploadRequest, photos: List<MultipartFile>): Angel {
      val angel = Angel(
         height = request.height,
         material = request.material,
         color = request.color,
         type = request.type
      )
      val saved = angelRepository.save(angel)
      val id = saved.id ?:  throw IllegalStateException("Angel ID is null")

      try {
         photoStorageService.saveAngelPhotos(id, photos)
         return saved
      } catch (e: Exception) {
         photoStorageService.deleteAllPhotosForAngel(id)
         throw e
      }
   }

   fun getAngelsWithFilters(
      colors: List<String>?,
      types: List<String>?,
      materials: List<String>?,
      minHeight: Double?,
      maxHeight: Double?,
      page: Int,
      size: Int,
      sort: String?
   ): Page<AngelDto> {
      val spec = Specification.where<Angel>(null)
         .and(colors?.let { AngelSpecs.hasAnyColor(it) })
         .and(types?.let { AngelSpecs.hasAnyType(it) })
         .and(materials?.let { AngelSpecs.hasAnyMaterial(it) })
         .and(minHeight?.let { AngelSpecs.hasMinHeight(it) })
         .and(maxHeight?.let { AngelSpecs.hasMaxHeight(it) })

      val sortObj = when (sort) {
         "date_desc" -> Sort.by(Sort.Direction.DESC, "createdAt")
         "date_asc" -> Sort.by(Sort.Direction.ASC, "createdAt")
         "height_desc" -> Sort.by(Sort.Direction.DESC, "height")
         "height_asc" -> Sort.by(Sort.Direction.ASC, "height")
         else -> Sort.by(Sort.Direction.DESC, "createdAt")
      }

      val angels = angelRepository.findAll(spec, PageRequest.of(page, size, sortObj))
      return angels.map { toDto(it) }
   }

   fun getAngelById(id: Long): AngelDto =
      angelRepository.findById(id).map { toDto(it) }
         .orElseThrow { NoSuchElementException("Angel not found with id $id") }

   fun updateAngel(id: Long, request: AngelUpdateRequest): Angel {
      val angel = angelRepository.findById(id)
         .orElseThrow { NoSuchElementException("Angel not found with id $id") }

      val updated = angel.copy(
         height = request.height,
         material = request.material,
         color = request.color,
         type = request.type
      )

      return angelRepository.save(updated)
   }

   @Transactional
   fun deleteAngel(id: Long) {
      if (!angelRepository.existsById(id)) {
         throw NoSuchElementException("Angel with id $id not found")
      }

      angelRepository.deleteById(id)
      photoStorageService.deleteAllPhotosForAngel(id)
   }

   fun toDto(angel: Angel): AngelDto {
      val id = angel.id ?: throw IllegalStateException("Angel ID is null")
      val firstPhoto = photoStorageService.getSortedOriginalFilenames(id).firstOrNull()
      return AngelDto(
         id = id,
         color = angel.color,
         material = angel.material,
         type = angel.type,
         height = angel.height,
         thumbnail = firstPhoto,
         createdAt = angel.createdAt
      )
   }

}

package com.warus.gallery.api.service

import com.warus.gallery.api.config.UploadProperties
import com.warus.gallery.api.db.model.Angel
import com.warus.gallery.api.db.repository.AngelRepository
import com.warus.gallery.api.model.AngelUpdateRequest
import com.warus.gallery.api.model.AngelUploadRequest
import net.coobird.thumbnailator.Thumbnails
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class AngelService(
   private val angelRepository: AngelRepository,
   private val uploadProperties: UploadProperties
) {

   fun save(file: MultipartFile, metadata: AngelUploadRequest): Angel {
      val extension = file.originalFilename?.substringAfterLast('.', "")!!
      val uuid = UUID.randomUUID().toString()

      val photoFilename = "$uuid.$extension"
      val thumbnailFilename = "${uuid}_thumb.$extension"

      val uploadDir = Paths.get(uploadProperties.path)
      val fullPath = uploadDir.resolve(photoFilename)
      val thumbPath = uploadDir.resolve(thumbnailFilename)

      if (!Files.exists(uploadDir)) {
         Files.createDirectories(uploadDir)
      }

      // Zapis oryginału
      file.inputStream.use { input ->
         Files.copy(input, fullPath, StandardCopyOption.REPLACE_EXISTING)
      }

      // Tworzenie miniatury (max 1920x1080)
      Thumbnails.of(fullPath.toFile())
         .size(500, 500)
         .keepAspectRatio(true)
         .toFile(thumbPath.toFile())

      val angel = Angel(
         photo = photoFilename,
         thumbnail = thumbnailFilename,
         height = metadata.height,
         material = metadata.material,
         color = metadata.color,
         type = metadata.type
      )

      return angelRepository.save(angel)
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
   ): Page<Angel> {
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

      return angelRepository.findAll(spec, PageRequest.of(page, size, sortObj))
   }

   fun getAngelById(id: Long): Angel =
      angelRepository.findById(id)
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

   fun deleteAngel(id: Long) {
      val angel = angelRepository.findById(id)
         .orElseThrow { NoSuchElementException("Angel not found with id $id") }

      val uploadPath = Paths.get(uploadProperties.path)

      val originalPath: Path = uploadPath.resolve(angel.photo)
      Files.deleteIfExists(originalPath)

      val thumbName = angel.thumbnail
      if (!thumbName.isNullOrBlank()) {
         val thumbPath = uploadPath.resolve(thumbName)
         Files.deleteIfExists(thumbPath)
      }

      angelRepository.delete(angel)
   }

}

package com.warus.gallery.api.service

import com.warus.gallery.api.config.UploadProperties
import com.warus.gallery.api.db.model.Photo
import com.warus.gallery.api.db.repository.PhotoRepository
import com.warus.gallery.api.model.PhotoUpdateRequest
import com.warus.gallery.api.model.PhotoUploadRequest
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
class PhotoService(
   private val photoRepository: PhotoRepository,
   private val uploadProperties: UploadProperties
) {

   fun save(file: MultipartFile, metadata: PhotoUploadRequest): Photo {
      val extension = file.originalFilename?.substringAfterLast('.', "")!!
      val uuid = UUID.randomUUID().toString()

      val fileName = "$uuid.$extension"
      val thumbnailName = "${uuid}_thumb.$extension"

      val uploadDir = Paths.get(uploadProperties.path)
      val fullPath = uploadDir.resolve(fileName)
      val thumbPath = uploadDir.resolve(thumbnailName)

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

      val photo = Photo(
         filename = fileName,
         thumbnail = thumbnailName,
         height = metadata.height,
         material = metadata.material,
         color = metadata.color,
         type = metadata.type
      )

      return photoRepository.save(photo)
   }

   fun getPhotosWithFilters(
      colors: List<String>?,
      types: List<String>?,
      materials: List<String>?,
      minHeight: Int?,
      maxHeight: Int?,
      page: Int,
      size: Int
   ): Page<Photo> {
      val spec = Specification.where<Photo>(null)
         .and(colors?.let { PhotoSpecs.hasAnyColor(it) })
         .and(types?.let { PhotoSpecs.hasAnyType(it) })
         .and(materials?.let { PhotoSpecs.hasAnyMaterial(it) })
         .and(minHeight?.let { PhotoSpecs.hasMinHeight(it) })
         .and(maxHeight?.let { PhotoSpecs.hasMaxHeight(it) })

      return photoRepository.findAll(spec, PageRequest.of(page, size, Sort.by("id").descending()))
   }

   fun getPhotoById(id: Long): Photo =
      photoRepository.findById(id)
         .orElseThrow { NoSuchElementException("Photo not found with id $id") }

   fun updatePhoto(id: Long, request: PhotoUpdateRequest): Photo {
      val photo = photoRepository.findById(id)
         .orElseThrow { NoSuchElementException("Photo not found with id $id") }

      val updated = photo.copy(
         height = request.height,
         material = request.material,
         color = request.color,
         type = request.type
      )

      return photoRepository.save(updated)
   }

   fun deletePhoto(id: Long) {
      val photo = photoRepository.findById(id)
         .orElseThrow { NoSuchElementException("Photo not found with id $id") }

      val uploadPath = Paths.get(uploadProperties.path)

      val originalPath: Path = uploadPath.resolve(photo.filename)
      Files.deleteIfExists(originalPath)

      val thumbName = photo.thumbnail
      if (!thumbName.isNullOrBlank()) {
         val thumbPath = uploadPath.resolve(thumbName)
         Files.deleteIfExists(thumbPath)
      }

      photoRepository.delete(photo)
   }

}

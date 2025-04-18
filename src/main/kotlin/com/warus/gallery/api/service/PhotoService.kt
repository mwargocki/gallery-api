package com.warus.gallery.api.service

import com.warus.gallery.api.config.UploadProperties
import com.warus.gallery.api.db.model.Photo
import com.warus.gallery.api.db.repository.PhotoRepository
import com.warus.gallery.api.model.PhotoUploadRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
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
      val fileName = UUID.randomUUID().toString() + "." + extension
      val uploadDir = Paths.get(uploadProperties.path)

      if (!Files.exists(uploadDir)) {
         Files.createDirectories(uploadDir)
      }

      val filePath = uploadDir.resolve(fileName)
      file.inputStream.use { input ->
         Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING)
      }

      val imageUrl = "/images/$fileName"

      val photo = Photo(
         fileName = fileName,
         imageUrl = imageUrl,
         height = metadata.height,
         material = metadata.material,
         color = metadata.color,
         type = metadata.type
      )

      return photoRepository.save(photo)
   }

   fun getPhotosWithFilters(
      color: String?,
      type: String?,
      material: String?,
      minHeight: Int?,
      maxHeight: Int?,
      page: Int,
      size: Int
   ): Page<Photo> {
      val spec = Specification.where<Photo>(null)
         .and(color?.let { PhotoSpecs.hasColor(it) })
         .and(type?.let { PhotoSpecs.hasType(it) })
         .and(material?.let { PhotoSpecs.hasMaterial(it) })
         .and(minHeight?.let { PhotoSpecs.hasMinHeight(it) })
         .and(maxHeight?.let { PhotoSpecs.hasMaxHeight(it) })

      return photoRepository.findAll(spec, PageRequest.of(page, size))
   }

}

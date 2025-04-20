package com.warus.gallery.api.controller

import com.warus.gallery.api.db.model.Photo
import com.warus.gallery.api.model.PhotoUpdateRequest
import com.warus.gallery.api.model.PhotoUploadRequest
import com.warus.gallery.api.service.PhotoService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/photos")
class PhotoController(
   private val photoService: PhotoService
) {

   @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
//   @PreAuthorize("hasRole('ADMIN')")
   fun uploadPhoto(
      @RequestPart file: MultipartFile,
      @RequestPart photo: PhotoUploadRequest
   ): ResponseEntity<Photo> {
      val saved = photoService.save(file, photo.copy(
         material = photo.material.lowercase(),
         color = photo.color.lowercase(),
         type = photo.type.lowercase()
      ))
      return ResponseEntity.status(HttpStatus.CREATED).body(saved)
   }

   @GetMapping
   fun getPhotos(
      @RequestParam(required = false) color: List<String>?,
      @RequestParam(required = false) type: List<String>?,
      @RequestParam(required = false) material: List<String>?,
      @RequestParam(required = false) minHeight: Int?,
      @RequestParam(required = false) maxHeight: Int?,
      @RequestParam(defaultValue = "0") page: Int,
      @RequestParam(defaultValue = "20") size: Int
   ): Page<Photo> {
      return photoService.getPhotosWithFilters(color, type, material, minHeight, maxHeight, page, size)
   }

   @GetMapping("/{id}")
   fun getPhoto(@PathVariable id: Long): Photo {
      return photoService.getPhotoById(id)
   }

   @PutMapping("/{id}")
   fun updatePhoto(@PathVariable id: Long, @RequestBody request: PhotoUpdateRequest): Photo {
      return photoService.updatePhoto(id, request.copy(
         material = request.material.lowercase(),
         color = request.color.lowercase(),
         type = request.type.lowercase()
      ))
   }

   @DeleteMapping("/{id}")
   fun deletePhoto(@PathVariable id: Long): ResponseEntity<Void> {
      photoService.deletePhoto(id)
      return ResponseEntity.noContent().build()
   }
}

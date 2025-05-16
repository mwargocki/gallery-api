package com.warus.gallery.api.controller

import com.warus.gallery.api.db.model.Angel
import com.warus.gallery.api.model.AngelUpdateRequest
import com.warus.gallery.api.model.AngelUploadRequest
import com.warus.gallery.api.service.AngelService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/angels")
class AngelController(
   private val angelService: AngelService
) {

   @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
//   @PreAuthorize("hasRole('ADMIN')")
   fun addAngel(
      @RequestPart file: MultipartFile,
      @RequestPart angel: AngelUploadRequest
   ): ResponseEntity<Angel> {
      if (angel.height <= 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
      }

      val saved = angelService.save(file, angel.copy(
         material = angel.material.lowercase(),
         color = angel.color.lowercase(),
         type = angel.type.lowercase()
      ))
      return ResponseEntity.status(HttpStatus.CREATED).body(saved)
   }

   @GetMapping
   fun getAngels(
      @RequestParam(required = false) color: List<String>?,
      @RequestParam(required = false) type: List<String>?,
      @RequestParam(required = false) material: List<String>?,
      @RequestParam(required = false) minHeight: Double?,
      @RequestParam(required = false) maxHeight: Double?,
      @RequestParam(defaultValue = "0") page: Int,
      @RequestParam(defaultValue = "20") size: Int,
      @RequestParam(required = false) sort: String?
   ): Page<Angel> {
      return angelService.getAngelsWithFilters(color, type, material, minHeight, maxHeight, page, size, sort)
   }

   @GetMapping("/{id}")
   fun getAngel(@PathVariable id: Long): Angel {
      return angelService.getAngelById(id)
   }

   @PutMapping("/{id}")
   fun updateAngel(@PathVariable id: Long, @RequestBody request: AngelUpdateRequest): Angel {
      return angelService.updateAngel(id, request.copy(
         material = request.material.lowercase(),
         color = request.color.lowercase(),
         type = request.type.lowercase()
      ))
   }

   @DeleteMapping("/{id}")
   fun deleteAngel(@PathVariable id: Long): ResponseEntity<Void> {
      angelService.deleteAngel(id)
      return ResponseEntity.noContent().build()
   }

}

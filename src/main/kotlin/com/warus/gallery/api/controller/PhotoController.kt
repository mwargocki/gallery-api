package com.warus.gallery.api.controller

import com.warus.gallery.api.config.UploadProperties
import com.warus.gallery.api.service.PhotoStorageService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files

@RestController
@RequestMapping("/api/angels/{angelId}/photos")
class PhotoController(
    private val photoStorageService: PhotoStorageService
) {

    @PostMapping
    fun addPhotos(
        @PathVariable angelId: Long,
        @RequestPart photos: List<MultipartFile>
    ) {
        photoStorageService.saveAngelPhotos(angelId, photos)
    }

    @DeleteMapping("/{filename}")
    fun deletePhoto(
        @PathVariable angelId: Long,
        @PathVariable filename: String
    ) {
        photoStorageService.deleteAngelPhoto(angelId, filename)
    }

    @GetMapping
    fun listPhotos(@PathVariable angelId: Long): List<String> {
        return photoStorageService.getSortedOriginalFilenames(angelId)
    }

    @GetMapping(value = ["/{filename}/{type}"])
    fun downloadPhoto(
        @PathVariable angelId: Long,
        @PathVariable type: String, // "original" lub "scaled"
        @PathVariable filename: String
    ): ResponseEntity<ByteArray> {
        if (type != "original" && type != "scaled") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        return try {
            val (bytes, contentType) = photoStorageService
                .loadPhoto(angelId, type, filename)
                ?: return ResponseEntity.notFound().build()

            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
                .body(bytes)

        } catch (e: IOException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}
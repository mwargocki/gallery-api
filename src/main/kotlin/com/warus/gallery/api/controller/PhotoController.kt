package com.warus.gallery.api.controller

import com.warus.gallery.api.config.UploadProperties
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.IOException
import java.nio.file.Files

@RestController
@RequestMapping("/api/photos")
class PhotoController(
    private val uploadProperties: UploadProperties,
) {

    @GetMapping(value = ["/{photoId}"])
    fun downloadPhoto(
        @PathVariable photoId: String
    ): ResponseEntity<ByteArray> {
        val photoFile = File("${uploadProperties.path}/${photoId}")

        if (!photoFile.exists() || !photoFile.isFile) {
            return ResponseEntity.notFound().build()
        }

        return try {
            val content = photoFile.readBytes()
            val contentType = Files.probeContentType(photoFile.toPath()) ?: MediaType.APPLICATION_OCTET_STREAM_VALUE

            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(content)
        } catch (e: IOException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}
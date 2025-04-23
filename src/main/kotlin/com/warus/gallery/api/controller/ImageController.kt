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
import java.util.*

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val uploadProperties: UploadProperties,
) {

    @GetMapping(value = ["/{imageId}"])
    fun downloadImage(
        @PathVariable imageId: String
    ): ResponseEntity<ByteArray> {
        val imageFile = File("${uploadProperties.path}/${imageId}")

        if (!imageFile.exists() || !imageFile.isFile) {
            return ResponseEntity.notFound().build()
        }

        return try {
            val content = imageFile.readBytes()
            val contentType = Files.probeContentType(imageFile.toPath()) ?: MediaType.APPLICATION_OCTET_STREAM_VALUE

            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(content)
        } catch (e: IOException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}
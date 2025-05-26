package com.warus.gallery.api.service

import com.warus.gallery.api.config.UploadProperties
import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.*
import kotlin.io.path.*

const val ORIGINAL_TYPE = "original"
const val SCALED_TYPE = "scaled"

@Service
class PhotoStorageService(
    private val uploadProperties: UploadProperties
) {

    fun saveAngelPhotos(angelId: Long, photos: List<MultipartFile>) {
        val angelDir = Path.of(uploadProperties.path, angelId.toString())
        val originalDir = angelDir.resolve(ORIGINAL_TYPE)
        val scaledDir = angelDir.resolve(SCALED_TYPE)

        Files.createDirectories(originalDir)
        Files.createDirectories(scaledDir)

        val existingCount = Files.list(originalDir)
            .filter { it.isRegularFile() }
            .count().toInt()

        val sortedPhotos = photos.sortedBy { it.originalFilename?.ifBlank { "zzz" } ?: "zzz" }

        sortedPhotos.forEachIndexed { index, file ->
            val ext = file.originalFilename
                ?.substringAfterLast('.', "")
                ?.ifBlank { "jpg" } ?: "jpg"

            val newIndex = existingCount + index + 1
            val newName = String.format("%02d.%s", newIndex, ext)

            val originalPath = originalDir.resolve(newName)
            val scaledPath = scaledDir.resolve(newName)

            Files.copy(file.inputStream, originalPath, StandardCopyOption.REPLACE_EXISTING)

            Thumbnails.of(originalPath.toFile())
                .size(800, 800)
                .toFile(scaledPath.toFile())
        }
    }

    fun deleteAngelPhoto(angelId: Long, filename: String) {
        val angelDir = Path.of(uploadProperties.path, angelId.toString())
        val originalDir = angelDir.resolve(ORIGINAL_TYPE)
        val scaledDir = angelDir.resolve(SCALED_TYPE)

        Files.deleteIfExists(originalDir.resolve(filename))
        Files.deleteIfExists(scaledDir.resolve(filename))

        renumberPhotos(angelId)
    }

    private fun renumberPhotos(angelId: Long) {
        val angelDir = Path.of(uploadProperties.path, angelId.toString())
        val originalDir = angelDir.resolve(ORIGINAL_TYPE)
        val scaledDir = angelDir.resolve(SCALED_TYPE)

        val originalFiles = Files.list(originalDir)
            .filter { it.isRegularFile() }
            .sorted(Comparator.comparing { it.fileName.toString() })
            .toList()

        originalFiles.forEachIndexed { index, path ->
            val ext = path.fileName.toString().substringAfterLast('.', "jpg")
            val newName = String.format("%02d.%s", index + 1, ext)

            val newOriginal = originalDir.resolve(newName)

            Files.move(path, newOriginal, StandardCopyOption.REPLACE_EXISTING)
        }

        val scaledFiles = Files.list(scaledDir)
            .filter { it.isRegularFile() }
            .sorted(Comparator.comparing { it.fileName.toString() })
            .toList()

        scaledFiles.forEachIndexed { index, path ->
            val ext = path.fileName.toString().substringAfterLast('.', "jpg")
            val newName = String.format("%02d.%s", index + 1, ext)

            val newScaled = scaledDir.resolve(newName)

            Files.move(path, newScaled, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    fun getSortedOriginalFilenames(angelId: Long): List<String> {
        val originalDir = Path.of(uploadProperties.path, angelId.toString(), ORIGINAL_TYPE)
        if (!Files.exists(originalDir)) return emptyList()

        return Files.list(originalDir)
            .filter { it.isRegularFile() }
            .map { it.fileName.toString() }
            .sorted()
            .toList()
    }

    fun deleteAllPhotosForAngel(angelId: Long) {
        val angelDir = Path.of(uploadProperties.path, angelId.toString())
        if (!Files.exists(angelDir)) return

        Files.walk(angelDir)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.deleteIfExists(it) }
    }

    fun loadPhoto(angelId: Long, type: String, filename: String): Pair<ByteArray, String>? {
        if (type != ORIGINAL_TYPE && type != SCALED_TYPE) return null

        val safeFilename = Paths.get(filename).fileName.toString()
        val photoPath = Paths.get(uploadProperties.path, angelId.toString(), type, safeFilename).toFile()

        if (!photoPath.exists() || !photoPath.isFile) return null

        val bytes = photoPath.readBytes()
        val contentType = Files.probeContentType(photoPath.toPath()) ?: "application/octet-stream"

        return bytes to contentType
    }
}

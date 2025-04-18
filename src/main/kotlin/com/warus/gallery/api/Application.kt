package com.warus.gallery.api

import com.warus.gallery.api.config.SecurityProperties
import com.warus.gallery.api.config.UploadProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(UploadProperties::class, SecurityProperties::class)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

package com.warus.gallery.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "upload")
class UploadProperties(
   val path: String
)

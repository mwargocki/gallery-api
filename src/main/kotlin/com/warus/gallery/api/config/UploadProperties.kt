package com.warus.gallery.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "upload")
class UploadProperties(
   val path: String
)

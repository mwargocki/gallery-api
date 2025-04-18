package com.warus.gallery.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class SecurityProperties(
   val secret: String,
   val expiration: Long
)


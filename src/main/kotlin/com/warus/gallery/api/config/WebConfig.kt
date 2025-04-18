package com.warus.gallery.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
   private val uploadProperties: UploadProperties
) : WebMvcConfigurer {
   override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
      registry
         .addResourceHandler("/images/**")
         .addResourceLocations("file:${uploadProperties.path}/")
   }
}

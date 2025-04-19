package com.warus.gallery.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// http://localhost:8080/swagger-ui/index.html#
@Configuration
class OpenApiSecurityConfig {

   @Bean
   fun openApi(): OpenAPI {
      val securitySchemeName = "bearerAuth"

      return OpenAPI()
         .info(
            Info()
               .title("Photo Gallery API")
               .version("1.0")
               .description("Photo Gallery Api")
         )
         .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
         .components(
            io.swagger.v3.oas.models.Components()
               .addSecuritySchemes(
                  securitySchemeName,
                  SecurityScheme()
                     .name(securitySchemeName)
                     .type(HTTP)
                     .`in`(HEADER)
                     .scheme("bearer")
                     .bearerFormat("JWT")
               )
         )
   }
}
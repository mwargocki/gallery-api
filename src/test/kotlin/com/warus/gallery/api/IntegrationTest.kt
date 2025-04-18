package com.warus.gallery.api

import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTest {

   companion object {

      @Container
      private val postgresqlContainer = PostgreSQLContainer("postgres:13-alpine")

      @DynamicPropertySource
      @JvmStatic
      fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
         registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
         registry.add("spring.datasource.username", postgresqlContainer::getUsername)
         registry.add("spring.datasource.password", postgresqlContainer::getPassword)
      }
   }

}

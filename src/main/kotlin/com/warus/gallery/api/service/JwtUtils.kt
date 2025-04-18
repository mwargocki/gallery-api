package com.warus.gallery.api.service

import com.warus.gallery.api.config.SecurityProperties
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import io.jsonwebtoken.security.Keys
import java.util.*

@Component
class JwtUtils(private val securityProperties: SecurityProperties) {

   private val key: SecretKey = Keys.hmacShaKeyFor(securityProperties.secret.toByteArray())

   fun generateToken(username: String): String =
      Jwts.builder()
         .setSubject(username)
         .setIssuedAt(Date())
         .setExpiration(Date(System.currentTimeMillis() + securityProperties.expiration))
         .signWith(key)
         .compact()

   fun getUsername(token: String): String =
      Jwts.parserBuilder()
         .setSigningKey(key)
         .build()
         .parseClaimsJws(token)
         .body
         .subject

   fun validateToken(token: String): Boolean = try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
      true
   } catch (ex: JwtException) {
      false
   }

}

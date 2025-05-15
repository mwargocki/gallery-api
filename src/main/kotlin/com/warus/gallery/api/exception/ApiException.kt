package com.warus.gallery.api.exception

import org.springframework.http.HttpStatus

open class ApiException(message: String, val status: HttpStatus) : RuntimeException(message)

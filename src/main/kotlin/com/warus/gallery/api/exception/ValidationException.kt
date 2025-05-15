package com.warus.gallery.api.exception

import org.springframework.http.HttpStatus

class ValidationException(message: String) : ApiException(message, HttpStatus.BAD_REQUEST)

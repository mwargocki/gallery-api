package com.warus.gallery.api.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleException(exception: ApiException): ResponseEntity<ApiError> {
        logger.error("Exception has been thrown: ${exception.stackTraceToString()}", exception)
        return ResponseEntity.status(exception.status).body(ApiError.fromException(exception))
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception): ResponseEntity<ApiError> {
        logger.error("Unmapped exception has been thrown: ${exception.stackTraceToString()}", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiError.fromException(exception))
    }

    data class ApiError(val error: String, val message: String?) {
        companion object {
            fun fromException(exception: Exception): ApiError =
                ApiError(exception::class.simpleName!!, exception.message)
        }
    }

}

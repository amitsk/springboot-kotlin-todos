package com.github.amitsk.todos

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.kotlin.core.publisher.toMono


data class ApiError(val errorCodes: List<ErrorCode>, val httpStatus: HttpStatus, val message: String = "")

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(val errorCode: Int, val message: String) {
  TODO_NOT_FOUND(30, "No Todo Found"),
  GENERIC_ERROR(32, "Generic Error"),
  BAD_REQUEST(31, "Bad Request");
}

fun ApiError.toServerResponse() =
    ServerResponse.status(this.httpStatus).json().body(this.errorCodes.toMono())
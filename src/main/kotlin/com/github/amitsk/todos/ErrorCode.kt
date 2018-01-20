package com.github.amitsk.todos

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.toMono


data class ApiError(val errorCodes: List<ErrorCode>, val httpStatus: HttpStatus)

val TODO_NOT_FOUND_ERROR = ApiError(listOf(ErrorCode.TODO_NOT_FOUND), HttpStatus.NOT_FOUND)

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(val errorCode: Int, val message: String) {
  TODO_NOT_FOUND(30, "No Todo Found");
}

fun ApiError.toServerResponse() =
    ServerResponse.status(this.httpStatus).json().body(this.errorCodes.toMono())
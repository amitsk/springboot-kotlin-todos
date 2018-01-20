package com.github.amitsk.todos

import com.nike.backstopper.apierror.ApiError
import com.nike.backstopper.apierror.ApiErrorBase
import java.util.*

enum class TodosApiError(private val delegate: ApiError) : ApiError {
  // Constructor args for this example are: errorCode, message, httpStatusCode
  TODO_NOT_FOUND(30, "No Todo Found", 404);
  // -- SNIP --

  @Suppress("unused")
  constructor(errorCode: Int, message: String, httpStatusCode: Int, metadata: Map<String, Any>? = null):
      this(ApiErrorBase(
          "delegated-to-enum-wrapper-" + UUID.randomUUID().toString(), errorCode, message, httpStatusCode,
          metadata
      ))

  override fun getName(): String {
    return this.name
  }

  override fun getErrorCode(): String {
    return delegate.errorCode
  }

  override fun getMessage(): String {
    return delegate.message
  }

  override fun getMetadata(): Map<String, Any> {
    return delegate.metadata
  }

  override fun getHttpStatusCode(): Int {
    return delegate.httpStatusCode
  }

}
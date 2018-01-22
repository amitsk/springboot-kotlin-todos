package com.github.amitsk.todos

data class TodoItem(val id: Long?, val name: String, val task: String)

class TodosException(val errors : List<ApiError>): RuntimeException()
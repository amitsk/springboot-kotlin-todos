package com.github.amitsk.todos

import jakarta.validation.constraints.Pattern

data class TodoItem(val id: Long?,
                    @get: Pattern(regexp = "[a-zA-Z0-9_-]{1,25}", message = "Name cannot exceed 25 characters. UJse valid characters")
                    val name: String,
                    @get: Pattern(regexp = "[a-zA-Z0-9 ._?-]{1,100}", message = "Task cannot be more than 100. Use valid characters")
                    val task: String)

class TodosException(val errors: List<ErrorCode>) : RuntimeException()
package com.github.amitsk.todos.handlers

import com.github.amitsk.todos.TodoItem
import com.github.amitsk.todos.json
import com.github.amitsk.todos.repository.TodosRepository
import jakarta.validation.ValidationException
import jakarta.validation.Validator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI

@Component
class TodosRoutesHandler(val repository: TodosRepository, val validator: Validator) {
    companion object {
        val idKey = "id"
        val log: Logger = LoggerFactory.getLogger("TodosRoutesHandler")
    }

    fun getTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
        val todoItemMono = repository.getTodo(serverRequest.pathVariable(idKey).toLong())
        return todoItemMono.flatMap { itm ->
            ok().json().bodyValue(itm)
        }
    }

    fun createTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
        val todoItemMono = serverRequest.bodyToMono<TodoItem>()

        return todoItemMono.flatMap { todoItem: TodoItem ->
            validateTodoItem(todoItem)
            repository.createTodo(todoItem)
        }
            .flatMap { cItm -> cItm.let { created(URI.create(cItm.id.toString())).build() } }


    }

    private fun validateTodoItem(todoItem: TodoItem) {
        val validationErrors = validator.validate(todoItem)
        log.info("Validation Errors $validationErrors")
        if (validationErrors.isNotEmpty()) {
            throw ValidationException(validationErrors.map { it.message }.joinToString("\n"))
        }
    }

    fun updateTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
        val todoItemMono = serverRequest.bodyToMono<TodoItem>()
        validator.validate(todoItemMono)
        val id = serverRequest.pathVariable(idKey).toLong()

        val updateResult: Mono<TodoItem> = todoItemMono.flatMap {
            validateTodoItem(it)
            repository.updateTodo(id, it)
        }
        log.info(updateResult.toString())
        return updateResult
            .flatMap { cItm ->
                ok().json().bodyValue(cItm)
            }
    }

    fun deleteTodo(serverRequest: ServerRequest): Mono<ServerResponse> =
        repository
            .deleteTodo(serverRequest.pathVariable(idKey).toLong())
            .flatMap { _ -> noContent().build() }

}

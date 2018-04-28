package com.github.amitsk.todos.repository

import com.github.amitsk.todos.ErrorCode
import com.github.amitsk.todos.TodoItem
import com.github.amitsk.todos.TodosException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

//TODO : COnvert all interfaces to Mono
interface TodosRepository {
  fun deleteTodo(id: Long): Mono<TodoItem>
  fun createTodo(todoItem: TodoItem): Mono<TodoItem>
  fun getTodo(id: Long): Mono<TodoItem>
  fun updateTodo(key: Long, todoItem: TodoItem): Mono<TodoItem>
}

@Component
class HashMapTodoRepository : TodosRepository {
  private val logger = LoggerFactory.getLogger(HashMapTodoRepository::class.java)

  private val COUNTER = AtomicLong()
  private val todos = ConcurrentHashMap<Long, TodoItem>()

  override fun deleteTodo(id: Long): Mono<TodoItem> {
    return Mono.justOrEmpty(todos.remove(id))
  }

  override fun createTodo(todoItem: TodoItem): Mono<TodoItem> {
    val key = COUNTER.incrementAndGet()
    return addTodo(todoItem, key)
  }

  private fun addTodo(todoItem: TodoItem, key: Long): Mono<TodoItem> {
    val responseTodoItem = todoItem.copy(id = key)
    logger.info("Key {}  Created and returned ", key)
    todos.put(key, responseTodoItem)
    return Mono.just(responseTodoItem)
  }

  override fun updateTodo(key: Long, todoItem: TodoItem): Mono<TodoItem> {
    val responseTodoItem = todoItem.copy(id = key)
    logger.info("Key {}  Updated and returned ", key)
    return if (todos.replace(key, responseTodoItem) == null) {
      Mono.error(TodosException(listOf(ErrorCode.TODO_NOT_FOUND)))
    } else {
      Mono.just(responseTodoItem)
    }
  }

  override fun getTodo(id: Long): Mono<TodoItem> {
    if (!todos.containsKey(id)) {
      logger.info("No........ Todo for $id")
      throw TodosException(listOf(ErrorCode.TODO_NOT_FOUND))
    }
    logger.info("Fetching Todo for $id")
    return Mono.justOrEmpty(todos.get(id))
  }

}


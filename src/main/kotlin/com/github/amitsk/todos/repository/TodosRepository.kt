package com.github.amitsk.todos.repository

import com.github.amitsk.todos.TODO_NOT_FOUND_ERROR
import com.github.amitsk.todos.TodoItem
import com.github.amitsk.todos.TodosException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

//TODO : COnvert all interfaces to Mono
interface TodosRepository {
  fun deleteTodo(id: Long)
  fun createTodo(todoItem: TodoItem): TodoItem
  fun getTodo(id: Long): Mono<TodoItem>
  fun updateTodo(key: Long, todoItem: TodoItem): TodoItem
}

@Component
class HashMapTodoRepository : TodosRepository {
  private val logger = LoggerFactory.getLogger(HashMapTodoRepository::class.java)

  private val COUNTER = AtomicLong()
  private val todos = ConcurrentHashMap<Long, TodoItem>()

  override fun deleteTodo(id: Long) {
    todos.remove(id)
  }

  override fun createTodo(todoItem: TodoItem): TodoItem {
    val key = COUNTER.incrementAndGet()
    return addTodo(todoItem, key)
  }

  private fun addTodo(todoItem: TodoItem, key: Long): TodoItem {
    val responseTodoItem = todoItem.copy(id = key)
    logger.info("Key {}  Created and returned ", key)
    todos.put(key, responseTodoItem)
    return responseTodoItem
  }

  override fun updateTodo(key: Long, todoItem: TodoItem): TodoItem {
    //validateKeyExists(key)
    val responseTodoItem = todoItem.copy(id = key)
    logger.info("Key {}  Updated and returned ", key)
    todos.put(key, responseTodoItem)
    return responseTodoItem
  }
//return Mono.justOrEmpty(this.people.get(id));
  override fun getTodo(id: Long): Mono<TodoItem> {
  if(! todos.containsKey(id)) {
    logger.info("No........ Todo for $id")
    throw TodosException(listOf(TODO_NOT_FOUND_ERROR))
  }
    logger.info("Fetching Todo for $id")
    return  Mono.justOrEmpty(todos.get(id))
  }

}


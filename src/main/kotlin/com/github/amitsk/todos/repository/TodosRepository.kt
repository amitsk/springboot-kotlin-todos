package com.github.amitsk.todos.repository

import com.github.amitsk.todos.TodoItem
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

interface TodosRepository {
  fun deleteTodo(id: Long)
  fun createTodo(todoItem: TodoItem): TodoItem
  fun getTodo(id: Long): TodoItem
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

  override fun getTodo(id: Long): TodoItem {
      todos.getOrElse(id, throw RuntimeException())
  }



}
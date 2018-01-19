package com.github.amitsk.todos

import com.github.amitsk.todos.repository.HashMapTodoRepository
// entry point for all assertThat methods and utility methods (e.g. entry)
import org.assertj.core.api.Assertions.*
import org.junit.Test


class TodosRepositoryTest {
  @Test
  fun `Great test to testing Hashmap repository`() {
    val repository = HashMapTodoRepository()
    val todo = repository.createTodo(TodoItem(100, "A", "B"))
    assertThat(todo).`as`("Created correctly ").isEqualTo(TodoItem(1, "A", "B"))
  }
}

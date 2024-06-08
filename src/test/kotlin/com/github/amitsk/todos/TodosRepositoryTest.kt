package com.github.amitsk.todos

import com.github.amitsk.todos.repository.HashMapTodoRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
// entry point for all assertThat methods and utility methods (e.g. entry)
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test


class TodosRepositoryTest {
  @Test
  fun `Great test to testing Hashmap repository`() {
    val repository = HashMapTodoRepository()
    val todo = repository.createTodo(TodoItem(100, "A", "B"))
    assertThat(todo).`as`("Created correctly ").isEqualTo(TodoItem(1, "A", "B"))
  }

  @Test
  fun `Test Some Json`() {
    val element = Json.parseToJsonElement("""
        {
            "name": "kotlinx.serialization",
            "forks": [{"votes": 42}, {"votes": 9000}, {}]
        }
    """).jsonObject
    assertThat(element).isNotNull()
  }


}

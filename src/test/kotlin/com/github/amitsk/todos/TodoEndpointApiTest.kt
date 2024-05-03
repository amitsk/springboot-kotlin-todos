package com.github.amitsk.todos

//
import com.github.amitsk.todos.TodoEndpointSpecData.path
import com.github.amitsk.todos.TodoEndpointSpecData.pathWithParam
import com.github.amitsk.todos.TodoEndpointSpecData.postPayload
import com.github.amitsk.todos.TodoEndpointSpecData.putPayload
import com.github.amitsk.todos.TodoEndpointSpecData.todoIdCreated
import io.kotest.core.spec.style.DescribeSpec
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import io.restassured.specification.RequestSpecification

import org.assertj.core.api.Assertions.*


object TodoEndpointSpecData {
  data class TodoRecord(val id: Long?, val name: String, val task: String)

  val path = "/todos"
  val pathWithParam = "/todos/{todoId}"
  val postPayload = "{ \"name\": \"TaskOne\", \"task\" : \"My first task\" }"
  val putPayload: (Long) -> String = { "{\"id\" : $it,  \"name\": \"TaskTwo\", \"task\" : \"My New task\" }" }
  var todoIdCreated: Long = 0
}


//https://github.com/rest-assured/rest-assured/wiki/Usage#kotlin
fun RequestSpecification.When(): RequestSpecification {
  return this.`when`()
}

class TodoEndpointSpec : DescribeSpec({
  describe("TODO Endpoint") {
    it("A TODO Item is created successfully") {
      val response =
          given().log().ifValidationFails()
              .request().contentType("application/json")
              .body(postPayload)
              .When()
              .post(path)
              .then()
              .statusCode(201).extract()
      todoIdCreated = response.header("Location").toLong()
    }

    it("A TODO Item can be queried successfully") {
      val response = given().log().ifValidationFails()
          .request()
          .contentType("application/json")
          .When()
          .pathParam("todoId", todoIdCreated)
          .get(pathWithParam)
          .then()
          .statusCode(200)
          .contentType(JSON).extract()
      val todoRecord = response.`as`(TodoEndpointSpecData.TodoRecord::class.java)
      assertThat(todoRecord.name).isEqualTo("TaskOne")
      assertThat(todoRecord.task).isEqualTo("My first task")
    }

    it("A TODO Item can be updated") {
      val response = given().log().ifValidationFails()
          .request().contentType("application/json")
          .When().pathParam("todoId", todoIdCreated)
          .body(putPayload(todoIdCreated))
          .put(pathWithParam)
          .then().statusCode(200).contentType(JSON).extract()
      val todoRecord = response.`as`(TodoEndpointSpecData.TodoRecord::class.java)
      assertThat(todoRecord.name).isEqualTo("TaskTwo")
      assertThat(todoRecord.task).isEqualTo("My New task")
    }

    it("A TODO Item can be deleted") {
      given().log().ifValidationFails()
          .request().contentType("application/json")
          .When()
          .pathParam("todoId", todoIdCreated)
          .delete(pathWithParam)
          .then()
          .statusCode(204)
    }
  }
})




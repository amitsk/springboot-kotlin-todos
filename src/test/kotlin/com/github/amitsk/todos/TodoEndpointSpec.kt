package com.nike.todoservice

//
import com.nike.todoservice.TodoEndpointSpecData.path
import com.nike.todoservice.TodoEndpointSpecData.pathWithParam
import com.nike.todoservice.TodoEndpointSpecData.postPayload
import com.nike.todoservice.TodoEndpointSpecData.putPayload
import com.nike.todoservice.TodoEndpointSpecData.todoIdCreated
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import io.restassured.specification.RequestSpecification
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
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

object TodoEndpointSpec : Spek({
  describe("TODO Endpoint") {
    on("A TODO Item is created successfully") {
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

    on("A TODO Item can be queried successfully") {
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

    on("A TODO Item can be updated") {
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

    on("A TODO Item can be deleted") {
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




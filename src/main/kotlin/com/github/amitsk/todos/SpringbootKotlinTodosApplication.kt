package com.github.amitsk.todos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringbootKotlinTodosApplication

fun main(args: Array<String>) {
    runApplication<SpringbootKotlinTodosApplication>(*args)
}

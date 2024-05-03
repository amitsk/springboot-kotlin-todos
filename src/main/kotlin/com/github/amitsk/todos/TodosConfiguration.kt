package com.github.amitsk.todos

import com.github.amitsk.todos.handlers.TodosRoutesHandler
import com.github.amitsk.todos.handlers.TodosWebExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.core.authorization.OAuth2ReactiveAuthorizationManagers
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.WebExceptionHandler
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.core.authorization.OAuth2ReactiveAuthorizationManagers.hasAnyScope


@Configuration
@EnableWebFluxSecurity
class TodosConfiguration() {
    @Bean
    fun todosRouter(apiHandler: TodosRoutesHandler) =
        router {
            (accept(MediaType.APPLICATION_JSON) and "/todos").nest {
                GET("/{id}", apiHandler::getTodo)
                PUT("/{id}", apiHandler::updateTodo)
                POST("/", apiHandler::createTodo)
                DELETE("/{id}", apiHandler::deleteTodo)
            }

        }

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun todosExceptionHandler(): WebExceptionHandler {
        return TodosWebExceptionHandler()
    }


    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            authorizeExchange {
//                authorize("/todos/**", authenticated)
//                authorize("/secure/**", hasAnyScope())
                authorize("/todos/**", permitAll)
            }
            authorizeExchange {
                authorize("/secure/**", authenticated)
            }

            oauth2ResourceServer {
                jwt { }
            }
        }
    }
}

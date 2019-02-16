package be.sourcedbvba.restbucks.order

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.router

@Configuration
class OrderWebConfiguration(private val orderRouterFunctions: OrderRouterFunctions): WebFluxConfigurer {
    @Bean
    fun orderRouter() = router {
        GET("/order").invoke(orderRouterFunctions::getOrders)
        HEAD("/order").invoke(orderRouterFunctions::headOrders)
        POST("/order").and(contentType(MediaType.APPLICATION_JSON)).invoke(orderRouterFunctions::createOrder)
        POST("/order/{id}/payment").invoke(orderRouterFunctions::payForOrder)
        POST("/order/{id}/delivery").invoke(orderRouterFunctions::deliverOrder)
        GET("/order/{id}/status").invoke(orderRouterFunctions::getOrderStatus)
        DELETE("/order/{id}").invoke(orderRouterFunctions::deleteOrder)
    }
}

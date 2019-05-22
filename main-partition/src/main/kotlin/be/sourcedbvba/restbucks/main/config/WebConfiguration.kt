package be.sourcedbvba.restbucks.main.config

import be.sourcedbvba.restbucks.order.api.CreateOrder
import be.sourcedbvba.restbucks.order.api.DeleteOrder
import be.sourcedbvba.restbucks.order.api.DeliverOrder
import be.sourcedbvba.restbucks.order.api.GetOrderStatus
import be.sourcedbvba.restbucks.order.api.GetOrders
import be.sourcedbvba.restbucks.order.infra.web.OrderRouterFactory
import be.sourcedbvba.restbucks.order.api.PayOrder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebConfiguration : WebFluxConfigurer {
    @Bean
    fun orderRouter(createOrder: CreateOrder,
                    getOrders: GetOrders,
                    getOrderStatus: GetOrderStatus,
                    deleteOrder: DeleteOrder,
                    deliverOrder: DeliverOrder,
                    payOrder: PayOrder) = OrderRouterFactory(createOrder,
            getOrders,
            getOrderStatus,
            deleteOrder,
            deliverOrder,
            payOrder).create()
}

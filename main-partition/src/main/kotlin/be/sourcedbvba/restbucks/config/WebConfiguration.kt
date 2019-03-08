package be.sourcedbvba.restbucks.config

import be.sourcedbvba.restbucks.order.CreateOrder
import be.sourcedbvba.restbucks.order.DeleteOrder
import be.sourcedbvba.restbucks.order.DeliverOrder
import be.sourcedbvba.restbucks.order.GetOrderStatus
import be.sourcedbvba.restbucks.order.GetOrders
import be.sourcedbvba.restbucks.order.OrderRouterFactory
import be.sourcedbvba.restbucks.order.PayOrder
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

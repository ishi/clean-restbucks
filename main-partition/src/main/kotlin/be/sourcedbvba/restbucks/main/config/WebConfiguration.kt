package be.sourcedbvba.restbucks.main.config

import be.sourcedbvba.restbucks.order.api.*
import be.sourcedbvba.restbucks.order.infra.web.createOrderRouter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebConfiguration : WebFluxConfigurer {
    @Bean
    fun orderRouter(createOrder: (CreateOrderRequest) -> CreateOrderResponse,
                    getOrders: () -> List<GetOrdersResponse>,
                    getOrderStatus: (GetOrderStatusRequest) -> GetOrderStatusResponse,
                    deleteOrder: (DeleteOrderRequest) -> Unit,
                    deliverOrder: (DeliverOrderRequest) -> Unit,
                    payOrder: (PayOrderRequest) -> Unit) = createOrderRouter(createOrder,
            getOrders,
            getOrderStatus,
            deleteOrder,
            deliverOrder,
            payOrder)
}

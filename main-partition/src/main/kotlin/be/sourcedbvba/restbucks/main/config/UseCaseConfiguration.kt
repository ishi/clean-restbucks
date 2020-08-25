package be.sourcedbvba.restbucks.main.config

import arrow.core.curry
import arrow.syntax.function.curried
import be.sourcedbvba.restbucks.order.api.CreateOrderRequest
import be.sourcedbvba.restbucks.order.api.CreateOrderResponse
import be.sourcedbvba.restbucks.order.api.DeleteOrderRequest
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.impl.*
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.transaction.annotation.Transactional

@Configuration
@ComponentScan(basePackages = ["be.sourcedbvba.restbucks"],
        includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION,
        value = [UseCase::class])])
internal class UseCaseConfiguration {
    @Autowired
    lateinit var getOrder: (OrderId) -> Order
    @Autowired
    lateinit var getOrders: () -> List<Order>

    @Bean
    @Transactional
    fun createOrder() = createOrderUseCase

    @Bean
    @Transactional
    fun deleteOrder() = deleteOrderUseCase.curried()(getOrder)

    @Bean
    fun getOrders() = getOrdersUseCase(getOrders)

    @Bean
    fun getOrderStatus() = getOrderStatusUseCase.curried()(getOrder)

    @Bean
    @Transactional
    fun deliverOrder() = deliverOrderUseCase.curried()(getOrder)

    @Bean
    @Transactional
    fun payOrder() = payOrderUseCase.curried()(getOrder)


}

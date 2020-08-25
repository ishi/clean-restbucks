package be.sourcedbvba.restbucks.main.config

import arrow.syntax.function.curried
import be.sourcedbvba.restbucks.order.infra.persistence.event.*
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.getOrder
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.getOrders
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["be.sourcedbvba.restbucks"])
@EntityScan(basePackages = ["be.sourcedbvba.restbucks"])
class PersistenceConfiguration {
    @Bean
    fun orderCreatedConsumerBean(orderRepository: OrderRepository) = orderCreatedConsumer.curried()(orderRepository::save)

    @Bean
    fun orderDeletedConsumerBean(orderRepository: OrderRepository) = orderDeletedConsumer.curried()(orderRepository::deleteById)

    @Bean
    fun orderDeliveredConsumerBean(orderRepository: OrderRepository) = orderDeliveredConsumer.curried()(orderRepository::save)(orderRepository::findById)

    @Bean
    fun orderPaidConsumerBean(orderRepository: OrderRepository) = orderPaidConsumer.curried()(orderRepository::save)(orderRepository::findById)

    @Bean
    fun orderGatewayGetOrder(orderRepository: OrderRepository) = getOrder.curried()(orderRepository::findById)

    @Bean
    fun orderGatewayGetOrders(orderRepository: OrderRepository) = { -> getOrders(orderRepository::findAll) }
}

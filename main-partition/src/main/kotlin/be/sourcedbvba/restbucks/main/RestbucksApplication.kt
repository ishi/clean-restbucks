package be.sourcedbvba.restbucks.main

import arrow.syntax.function.bind
import arrow.syntax.function.curried
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.EventPublisher
import be.sourcedbvba.restbucks.order.impl.*
import be.sourcedbvba.restbucks.order.infra.persistence.event.orderCreatedConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.event.orderDeletedConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.event.orderDeliveredConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.event.orderPaidConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.getOrder
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.getOrders
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import be.sourcedbvba.restbucks.order.infra.web.createOrderRouter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan

import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.PayloadApplicationEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ApplicationEventMulticaster
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.reactive.config.WebFluxConfigurer

@SpringBootApplication
class RestbucksApplication

@Configuration
@EnableJpaRepositories(basePackages = ["be.sourcedbvba.restbucks"])
@EntityScan(basePackages = ["be.sourcedbvba.restbucks"])
class Configuration(val orderRepository: OrderRepository,
                    val applicationEventMulticaster: ApplicationEventMulticaster,
                    val transactionTemplate: TransactionTemplate) : WebFluxConfigurer {
    init {
        EventPublisher.Locator.eventPublisher = { event ->
            applicationEventMulticaster.multicastEvent(PayloadApplicationEvent(this, event))
        }

        val eventConsumers = listOf(
                orderCreatedConsumer.curried()(orderRepository::save),
                orderDeletedConsumer.curried()(orderRepository::deleteById),
                orderDeliveredConsumer.curried()(orderRepository::save)(orderRepository::findById),
                orderPaidConsumer.curried()(orderRepository::save)(orderRepository::findById)
        )
        applicationEventMulticaster.addApplicationListener(ApplicationListener { event: PayloadApplicationEvent<*> ->
            if (event.payload is DomainEvent) {
                transactionTemplate.executeWithoutResult {
                    val payload = event.payload as DomainEvent
                    eventConsumers.forEach { it(payload) }
                }
            }
        })
    }

    @Bean
    fun orderRouter() = createOrderRouter(
            createOrderUseCase,
            getOrdersUseCase.bind(getOrders.bind(orderRepository::findAll)),
            getOrderStatusUseCase.curried()(getOrder.curried()(orderRepository::findById)),
            deleteOrderUseCase.curried()(getOrder.curried()(orderRepository::findById)),
            deliverOrderUseCase.curried()(getOrder.curried()(orderRepository::findById)),
            payOrderUseCase.curried()(getOrder.curried()(orderRepository::findById)))

}

fun main(args: Array<String>) {
    runApplication<RestbucksApplication>(*args)
}



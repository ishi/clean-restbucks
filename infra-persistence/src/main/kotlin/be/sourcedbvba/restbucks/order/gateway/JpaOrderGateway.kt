package be.sourcedbvba.restbucks.order.gateway

import be.sourcedbvba.restbucks.order.Order
import be.sourcedbvba.restbucks.order.OrderItem
import be.sourcedbvba.restbucks.order.OrderEntity
import be.sourcedbvba.restbucks.order.OrderItemEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

@Component
internal class JpaOrderGateway internal constructor(private val orderJpaRepository: OrderJpaRepository, @Qualifier("jpa") private val scheduler: Scheduler) : OrderGateway {
    override fun getOrder(orderId: String): Mono<Order> {
        return Mono.just(orderJpaRepository.getOne(orderId).toDomain())
    }

    override fun getOrders(): Flux<Order> {
        return Mono.fromCallable { orderJpaRepository.findAll().map { it.toDomain() } }
                .publishOn(scheduler)
                .flatMapIterable { it }.log()
    }

    internal fun OrderEntity.toDomain() : Order {
        return Order(id, customerName, status, items.map { it.toDomain() })
    }

    internal fun OrderItemEntity.toDomain() : OrderItem {
        return OrderItem(product, quantity, size, milk)
    }
}
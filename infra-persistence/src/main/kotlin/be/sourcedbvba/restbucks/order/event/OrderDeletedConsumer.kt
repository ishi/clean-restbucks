package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.OrderJpaRepository
import org.springframework.stereotype.Component

@Component
internal class OrderDeletedConsumer internal constructor(private val orderJpaRepository: OrderJpaRepository) : DomainEventConsumer<OrderDeleted> {
    override fun consume(event: OrderDeleted) {
        orderJpaRepository.deleteById(event.getId().value)
    }
}

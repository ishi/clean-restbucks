package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.OrderRepository
import org.springframework.stereotype.Component

@Component
internal class OrderDeletedConsumer internal constructor(private val orderRepository: OrderRepository) : DomainEventConsumer<OrderDeleted> {
    override fun consume(event: OrderDeleted) {
        orderRepository.deleteById(event.getId().value)
    }
}

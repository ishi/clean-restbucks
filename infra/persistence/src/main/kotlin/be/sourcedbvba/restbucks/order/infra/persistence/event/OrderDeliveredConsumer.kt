package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.domain.services.event.OrderDeleted
import be.sourcedbvba.restbucks.order.domain.services.event.OrderDelivered
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException
import java.util.*

val orderDeliveredConsumer = { save: (OrderEntity) -> OrderEntity,
                               findById: (String) -> Optional<OrderEntity>,
                               event: DomainEvent ->
    run {
        if(event is OrderDelivered) {
            val order = findById(event.getId().value).orElseThrow { IllegalArgumentException() }
            order.status = Status.DELIVERED
            save(order)
        }
    }
}

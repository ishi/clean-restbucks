package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.domain.services.event.*
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderItemEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import java.util.*

val orderDeletedConsumer = { deleteById: (String) -> Unit, event: DomainEvent ->
    run {
        if(event is OrderDeleted) {
            deleteById(event.getId().value)
        }
    }
}

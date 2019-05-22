package be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderEntity, String>

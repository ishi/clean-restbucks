package be.sourcedbvba.restbucks.order.gateway.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderEntity, String>

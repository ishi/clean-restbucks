package be.sourcedbvba.restbucks.order.gateway.jpa

import be.sourcedbvba.restbucks.Milk
import be.sourcedbvba.restbucks.Size
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class OrderItemEntity(
    @Id val id: String?,
    val product: String,
    val quantity: Int,
    @Enumerated val size: Size,
    @Enumerated val milk: Milk
)

package be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
data class OrderEntity(
        @Id val id: String,
        val customerName: String,
        @Enumerated var status: Status,
        val cost: BigDecimal,
        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    val items: List<OrderItemEntity>
)

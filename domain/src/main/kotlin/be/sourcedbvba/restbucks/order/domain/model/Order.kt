package be.sourcedbvba.restbucks.order.domain.model

import be.sourcedbvba.restbucks.order.shared.vocabulary.Milk
import be.sourcedbvba.restbucks.order.shared.vocabulary.Size
import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import be.sourcedbvba.restbucks.order.domain.services.event.OrderCreatedEvent
import be.sourcedbvba.restbucks.order.domain.services.event.OrderDeletedEvent
import be.sourcedbvba.restbucks.order.domain.services.event.OrderDeliveredEvent
import be.sourcedbvba.restbucks.order.domain.services.event.OrderPaidEvent
import java.math.BigDecimal
import java.util.Random

data class OrderId(val value: String)
typealias CustomerName = String

data class OrderItems(private val value: List<OrderItem>) {
    fun <T> map(mapper: ((OrderItem) -> T)) = value.map(mapper)
}

class Order(
        val id: OrderId,
        val customer: CustomerName,
        status: Status,
        val items: OrderItems
) {
    lateinit var cost: BigDecimal
        private set
    var status: Status = status
        private set

    private fun calculateCost() {
        cost = BigDecimal(Random().nextInt(20))
    }

    fun create() {
        calculateCost()
        return OrderCreatedEvent(this).sendEvent()
    }

    fun delete() {
        return OrderDeletedEvent(id).sendEvent()
    }

    @Throws(ExpectedOrderStatusException::class)
    fun pay() {
        if (status == Status.OPEN) {
            status = Status.PAID
            OrderPaidEvent(id).sendEvent()
        } else {
            throw ExpectedOrderStatusException(listOf(Status.OPEN))
        }
    }

    @Throws(ExpectedOrderStatusException::class)
    fun deliver() {
        if (status == Status.PAID) {
            OrderDeliveredEvent(id).sendEvent()
        } else {
            throw ExpectedOrderStatusException(listOf(Status.PAID))
        }
    }
}

typealias ProductName = String
typealias Quantity = Int
class OrderItem(val productName: ProductName, val quantity: Quantity, val size: Size, val milk: Milk)

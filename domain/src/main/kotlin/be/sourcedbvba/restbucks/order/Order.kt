package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Milk
import be.sourcedbvba.restbucks.Size
import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.order.event.*
import java.math.BigDecimal
import java.util.*

data class OrderId(val value: String)
typealias CustomerName = String

data class OrderItems(private val value: List<OrderItem>) {
    fun <T> map(mapper: ((OrderItem) -> T)) = value.map(mapper)
}

class Order(val id: OrderId,
                     val customer: CustomerName,
                     status: Status,
                     val items: OrderItems) {
    lateinit var cost : BigDecimal
        private set
    var status : Status = status
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

    fun pay() {
        if(status == Status.OPEN) {
            status = Status.PAID
            return OrderPaidEvent(id).sendEvent()
        } else {
            throw IllegalStateException("Order should be open in order to be paid")
        }
    }

    fun deliver() {
        if(status == Status.PAID) {
            return OrderDeliveredEvent(id).sendEvent()
        } else {
            throw IllegalStateException("Order has not been paid yet")
        }
    }
}

typealias ProductName = String
typealias Quantity = Int
class OrderItem(val productName: ProductName, val quantity: Quantity, val size: Size, val milk: Milk)
package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Milk
import be.sourcedbvba.restbucks.Size
import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.order.event.*
import java.math.BigDecimal
import java.util.*


class Order(val id: String,
                     val customer: String,
                     private var _status: Status,
                     val items: List<OrderItem>) {
    val cost : BigDecimal
        get() = calculateCost()
    val status : Status = _status

    private fun calculateCost() = BigDecimal(Random().nextInt(20))

    fun create() {
        return OrderCreatedEvent(this).sendEvent()
    }

    fun delete() {
        return OrderDeletedEvent(id).sendEvent()
    }

    fun pay() {
        if(_status == Status.OPEN) {
            _status = Status.PAID
            return OrderPaidEvent(id).sendEvent()
        } else {
            throw IllegalStateException("Order should be open in order to be paid")
        }
    }

    fun deliver() {
        if(_status == Status.PAID) {
            return OrderDeliveredEvent(id).sendEvent()
        } else {
            throw IllegalStateException("Order has not been paid yet")
        }
    }
}

class OrderItem(val product: String, val quantity: Int, val size: Size, val milk: Milk)
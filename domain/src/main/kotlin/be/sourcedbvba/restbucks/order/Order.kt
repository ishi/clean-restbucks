package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Milk
import be.sourcedbvba.restbucks.Size
import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.notification.Notification
import be.sourcedbvba.restbucks.domain.notification.Notifications
import be.sourcedbvba.restbucks.order.event.*
import java.math.BigDecimal
import java.util.*

data class OrderId(val value: String)
typealias CustomerName = String

data class OrderItems(private val value: List<OrderItem>) {
    fun <T> map(mapper: ((OrderItem) -> T)) = value.map(mapper)
}

interface Order {
    companion object {}
    val id: OrderId
    val customer: CustomerName
    val items: OrderItems
    val cost: BigDecimal
    val status: Status

    fun create()
    fun delete()
    fun pay()
    fun deliver()
}

class OrderImpl(override val id: OrderId,
                     override val customer: CustomerName,
                     status: Status,
                     override val items: OrderItems) : Order {
    override lateinit var cost : BigDecimal
        private set
    override var status : Status = status
        private set

    fun Order.Companion.createDefaultImplementation(id: OrderId,
                                                        customer: CustomerName,
                                                        status: Status,
                                                        items: OrderItems) : Order {
        return OrderImpl(id, customer, status, items)
    }

    private fun calculateCost() {
        cost = BigDecimal(Random().nextInt(20))
    }

    override fun create() {
        calculateCost()
        return OrderCreatedEvent(this).sendEvent()
    }

    override fun delete() {
        return OrderDeletedEvent(id).sendEvent()
    }

    @Throws(ExpectedOrderStatusException::class)
    override fun pay() {
        if(status == Status.OPEN) {
            status = Status.PAID
            OrderPaidEvent(id).sendEvent()
        } else {
            throw ExpectedOrderStatusException(listOf(Status.OPEN))
        }
    }

    @Throws(ExpectedOrderStatusException::class)
    override fun deliver() {
        if(status == Status.PAID) {
            OrderDeliveredEvent(id).sendEvent()
        } else {
            throw ExpectedOrderStatusException(listOf(Status.PAID))
        }
    }
}

interface OrderItem {
    companion object {}
    val productName: ProductName
    val quantity: Quantity
    val size: Size
    val milk: Milk
}

typealias ProductName = String
typealias Quantity = Int

class OrderItemImpl(
        override val productName: ProductName,
        override val quantity: Quantity,
        override val size: Size,
        override val milk: Milk) : OrderItem {
    fun OrderItem.Companion.createDefaultImplementation(productName: ProductName,
                                                        quantity: Quantity,
                                                        size: Size,
                                                        milk: Milk) : OrderItem {
        return OrderItemImpl(productName, quantity, size, milk)
    }
}


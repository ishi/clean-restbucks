package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.domain.transaction.TransactionFactory
import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import java.lang.Exception
import java.util.function.Supplier

@UseCase
internal class DeliverOrderImpl(val orderGateway: OrderGateway, private val transactionFactory: TransactionFactory) : DeliverOrder {
    override fun deliver(request: DeliverOrderRequest) {
        val transaction = transactionFactory.start()
        try {
            val order = orderGateway.getOrder(OrderId(request.orderId))
            order.deliver()
            transaction.commit()
        } catch (ex: Exception) {
            transaction.rollback()
        }
    }
}
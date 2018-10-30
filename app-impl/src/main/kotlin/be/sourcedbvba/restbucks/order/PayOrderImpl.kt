package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.domain.transaction.TransactionFactory
import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.function.Supplier

@UseCase
internal class PayOrderImpl(private val orderGateway: OrderGateway,
                            private val transactionFactory: TransactionFactory) : PayOrder {
    override fun pay(request: PayOrderRequest) {
        val transaction = transactionFactory.start()
        try {
            val order = orderGateway.getOrder(OrderId(request.orderId))
            order.pay()
            transaction.commit()
        } catch (ex: Exception) {
            transaction.rollback()
        }
    }
}
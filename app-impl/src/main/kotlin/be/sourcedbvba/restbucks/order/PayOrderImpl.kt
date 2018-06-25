package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.domain.transaction.TransactionalRunner
import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.function.Supplier

@UseCase
internal class PayOrderImpl(private val orderGateway: OrderGateway,
                            private val transactionalRunner: TransactionalRunner) : PayOrder {
    override fun pay(request: PayOrderRequest) {
        request.validate()
        transactionalRunner.runInTransaction(Supplier {
            val order = orderGateway.getOrder(OrderId(request.orderId))
            order.pay()
        })
    }

    private fun PayOrderRequest.validate() {

    }
}
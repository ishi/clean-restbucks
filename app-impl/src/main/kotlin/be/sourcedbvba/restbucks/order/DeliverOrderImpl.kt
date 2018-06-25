package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.domain.transaction.TransactionalRunner
import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.function.Supplier

@UseCase
internal class DeliverOrderImpl(val orderGateway: OrderGateway, private val transactionalRunner: TransactionalRunner) : DeliverOrder {
    override fun deliver(request: DeliverOrderRequest) {
        request.validate()
        transactionalRunner.runInTransaction(Supplier {
            val order = orderGateway.getOrder(OrderId(request.orderId))
            order.deliver()
        })
    }

    private fun DeliverOrderRequest.validate() {

    }
}
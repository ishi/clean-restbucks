package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.domain.transaction.TransactionalRunner
import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.function.Supplier

@UseCase
internal class DeleteOrderImpl(private val orderGateway: OrderGateway,
                               private val transactionalRunner: TransactionalRunner) : DeleteOrder {
    override fun delete(request: DeleteOrderRequest) {
        request.validate()
        transactionalRunner.runInTransaction(Supplier {
            val order = orderGateway.getOrder(OrderId(request.orderId))
            order.delete()
        })
    }

    private fun DeleteOrderRequest.validate() {

    }
}
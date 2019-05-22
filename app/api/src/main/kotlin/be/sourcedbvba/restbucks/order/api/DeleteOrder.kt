package be.sourcedbvba.restbucks.order.api

interface DeleteOrder {
    fun delete(request: DeleteOrderRequest)
}

data class DeleteOrderRequest(val orderId: String)

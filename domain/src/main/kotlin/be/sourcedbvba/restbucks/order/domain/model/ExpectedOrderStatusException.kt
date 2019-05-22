package be.sourcedbvba.restbucks.order.domain.model

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status

class ExpectedOrderStatusException(val expectedStatuses: List<Status>) : Exception("Order is in wrong status")

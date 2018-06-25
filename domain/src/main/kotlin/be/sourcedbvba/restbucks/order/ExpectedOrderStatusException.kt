package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status

class ExpectedOrderStatusException(val expectedStatuses: List<Status>) : Exception("Order is in wrong status")

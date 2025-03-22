package com.data.remote.request.order

data class DeleteAuthorizedPaymentProfileRq(
    val AuthorizedPaymentProfileID: Int,
    val CustomerID: Int
)
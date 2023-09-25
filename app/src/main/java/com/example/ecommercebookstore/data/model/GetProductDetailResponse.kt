package com.example.ecommercebookstore.data.model

data class GetProductDetailResponse(
    val message: String?,
    val product: Product,
    val status: Int?
)

package com.example.ecommercebookstore.data.model


data class GetProductsResponse(
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)
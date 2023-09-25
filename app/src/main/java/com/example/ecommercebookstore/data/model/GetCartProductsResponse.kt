package com.example.ecommercebookstore.data.model


import com.google.gson.annotations.SerializedName

data class GetCartProductsResponse(
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)
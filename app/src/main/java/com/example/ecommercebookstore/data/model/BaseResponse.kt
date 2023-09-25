package com.example.ecommercebookstore.data.model


import com.google.gson.annotations.SerializedName

data class BaseResponse(
    val message: String?,
    val status: Int?
)
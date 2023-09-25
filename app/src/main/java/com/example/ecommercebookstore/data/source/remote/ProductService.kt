package com.example.ecommercebookstore.data.source.remote


import com.example.ecommercebookstore.common.Constants.Endpoint.ADD_TO_CART
import com.example.ecommercebookstore.common.Constants.Endpoint.CLEAR_CART
import com.example.ecommercebookstore.common.Constants.Endpoint.DELETE_FROM_CART
import com.example.ecommercebookstore.common.Constants.Endpoint.GET_CART_PRODUCTS
import com.example.ecommercebookstore.common.Constants.Endpoint.GET_PRODUCTS
import com.example.ecommercebookstore.common.Constants.Endpoint.GET_PRODUCT_DETAIL
import com.example.ecommercebookstore.common.Constants.Endpoint.GET_SALE_PRODUCTS
import com.example.ecommercebookstore.common.Constants.Endpoint.SEARCH_PRODUCT
import com.example.ecommercebookstore.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @Headers("store: Dbookstore")
    @GET(GET_PRODUCTS)
    suspend fun getProducts(): GetProductsResponse

    @Headers("store: Dbookstore")
    @GET(GET_SALE_PRODUCTS)
    suspend fun getSaleProducts() : GetSaleProductsResponse

    @Headers("store: Dbookstore")
    @GET(GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Query("id") id: Int
    ) : GetProductDetailResponse

    @Headers("store: Dbookstore")
    @GET(SEARCH_PRODUCT)
    suspend fun searchProduct(
        @Query("query") query: String?
    ) : SearchProductResponse

    @Headers("store: Dbookstore")
    @POST(ADD_TO_CART)
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ) : BaseResponse

    @Headers("store: Dbookstore")
    @GET(GET_CART_PRODUCTS)
    suspend fun getCartProducts(
        @Query("userId") userId : String?
    ) : GetCartProductsResponse

    @Headers("store: Dbookstore")
    @POST(DELETE_FROM_CART)
    suspend fun deleteFromCart (
        @Body deleteFromCartRequest: DeleteFromCartRequest
    ) : BaseResponse

    @Headers("store: Dbookstore")
    @POST(CLEAR_CART)
    suspend fun clearCart(
        @Body clearCartRequest: ClearCartRequest
    ) : BaseResponse
}
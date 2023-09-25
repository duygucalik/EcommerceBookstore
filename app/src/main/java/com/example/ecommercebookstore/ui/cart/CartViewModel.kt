package com.example.ecommercebookstore.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebookstore.common.Resource
import com.example.ecommercebookstore.data.model.*
import com.example.ecommercebookstore.data.repository.ProductsRepository
import com.example.ecommercebookstore.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val userRepository: UserRepository
    ) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState

    private val _totalPriceAmount = MutableLiveData(0.0)
    val totalPriceAmount: LiveData<Double> = _totalPriceAmount

    fun getCartProducts(userId: String) {
        viewModelScope.launch {
            val result = productsRepository.getCartProducts(userId)

            when(result) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data(result.data)
                    _totalPriceAmount.value = result.data.sumOf {
                        if(it.saleState != true) {
                            it.price
                        } else {
                            it.salePrice
                        }
                    }
                }
                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                    resetTotalAmount()
                }
            }
        }
    }

    fun deleteFromCart(id: Int) {
        viewModelScope.launch {
            val deleteFromCartRequest = DeleteFromCartRequest(id)
            when(val result = productsRepository.deleteFromCart(deleteFromCartRequest)) {
                is Resource.Success -> {
                    _cartState.value = CartState.DeleteFromCart(result.data)
                    getCartProducts(userRepository.getFirebaseUserUid())
                }
                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            val clearCartRequest = ClearCartRequest(userId)
            when(val result = productsRepository.clearCart(clearCartRequest)) {
                is Resource.Success -> {
                    _cartState.value = CartState.ClearCart(result.data)
                    getCartProducts(userRepository.getFirebaseUserUid())
                }
                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }
    }

    fun increase(price: Double?) {
        _totalPriceAmount.value = price?.let { _totalPriceAmount.value?.plus(it) }
    }

    fun decrease(price: Double?) {
        _totalPriceAmount.value = price?.let { _totalPriceAmount.value?.minus(it) }
    }

    private fun resetTotalAmount() {
        _totalPriceAmount.value = 0.0
    }

}

sealed interface CartState {
    data class Data(val products: List<ProductUI>) : CartState
    data class Error(val throwable: Throwable) : CartState
    data class DeleteFromCart(val baseResponse: BaseResponse) : CartState
    data class ClearCart(val baseResponse: BaseResponse) : CartState
}
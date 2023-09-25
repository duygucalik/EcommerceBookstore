package com.example.ecommercebookstore.ui.success

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebookstore.common.Resource
import com.example.ecommercebookstore.data.model.BaseResponse
import com.example.ecommercebookstore.data.model.ClearCartRequest
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.data.repository.ProductsRepository
import com.example.ecommercebookstore.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SuccessViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _successState = MutableLiveData<SuccessState>()
    val successState: LiveData<SuccessState>
        get() = _successState

    private val _totalPriceAmount = MutableLiveData(0.0)
    val totalPriceAmount: LiveData<Double> = _totalPriceAmount

    private fun getCartProducts(userId: String) {
        viewModelScope.launch {
            val result = productsRepository.getCartProducts(userId)

            when(result) {
                is Resource.Success -> {
                    _successState.value = SuccessState.Data(result.data)
                    _totalPriceAmount.value = result.data.sumOf {
                        it.price ?: 0.0
                    }
                }
                is Resource.Error -> {
                    _successState.value = SuccessState.Error(result.throwable)
                    resetTotalAmount()
                }
            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            val clearCartRequest = ClearCartRequest(userId)
            when(val result = productsRepository.clearCart(clearCartRequest)) {
                is Resource.Success -> {
                    _successState.value = SuccessState.ClearCart(result.data)
                    getCartProducts(userRepository.getFirebaseUserUid())
                }
                is Resource.Error -> {
                    _successState.value = SuccessState.Error(result.throwable)
                }
            }
        }
    }

    private fun resetTotalAmount() {
        _totalPriceAmount.value = 0.0
    }
}

sealed interface SuccessState {
    data class Data(val products: List<ProductUI>) : SuccessState
    data class Error(val throwable: Throwable) : SuccessState
    data class ClearCart(val baseResponse: BaseResponse) : SuccessState
}

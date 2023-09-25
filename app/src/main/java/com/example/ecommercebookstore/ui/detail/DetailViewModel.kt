package com.example.ecommercebookstore.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebookstore.common.Resource
import com.example.ecommercebookstore.data.model.AddToCartRequest
import com.example.ecommercebookstore.data.model.BaseResponse
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState>
        get() = _detailState


    fun getProductDetail(id:Int) {
        viewModelScope.launch {

            when(val result = productsRepository.getProductDetail(id)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.Data(result.data)
                }
                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }
            }
        }
    }

    fun addToCart(addToCartRequest: AddToCartRequest) {
        viewModelScope.launch {
            when(val result = productsRepository.addToCart(addToCartRequest)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.AddToBag(result.data)
                }
                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }
            }
        }
    }
}

sealed interface DetailState {
    data class Data(val product: ProductUI) : DetailState
    data class Error(val throwable: Throwable) : DetailState
    data class AddToBag(val baseResponse: BaseResponse) : DetailState
}
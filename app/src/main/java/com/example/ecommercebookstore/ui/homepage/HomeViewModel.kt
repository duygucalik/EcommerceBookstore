package com.example.ecommercebookstore.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebookstore.common.Resource
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getProducts() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = productsRepository.getProducts()

            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun getSaleProducts() {
        viewModelScope.launch {
            val result = productsRepository.getSaleProducts()

            when(result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.SaleData(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

    fun addToFavorites(product: ProductUI) {
        viewModelScope.launch {
            productsRepository.addToFavorites(product)
        }
    }
}

sealed interface HomeState {
    object Loading: HomeState
    data class Data(val products: List<ProductUI>) : HomeState
    data class SaleData(val products: List<ProductUI>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
}
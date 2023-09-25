package com.example.ecommercebookstore.ui.search

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
class SearchViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel() {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
    get() = _searchState


    private var query=""
    fun searchProduct(query:String?) {
        viewModelScope.launch {
            when(val result = productsRepository.searchProduct(query)) {
                is Resource.Success -> {
                    _searchState.value = SearchState.Data(result.data)
                }
                is Resource.Error -> {
                    _searchState.value = SearchState.Error(result.throwable)
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

sealed interface SearchState {
    data class Data(val products: List<ProductUI>) : SearchState
    data class Error(val throwable: Throwable) : SearchState
}
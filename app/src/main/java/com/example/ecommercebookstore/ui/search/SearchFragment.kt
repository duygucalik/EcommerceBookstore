package com.example.ecommercebookstore.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.databinding.FragmentSearchBinding
import com.example.ecommercebookstore.ui.favorite.FavoritesAdapter
import com.example.ecommercebookstore.ui.homepage.ProductsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), FavoritesAdapter.FavoriteProductListener {

    private lateinit var binding : FragmentSearchBinding
    private val productAdapter by lazy { FavoritesAdapter(this) }
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearch.adapter = productAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.searchProduct(p0)
                return true
            }
        })

        observeData()
    }

    private fun observeData() {

        viewModel.searchState.observe(viewLifecycleOwner) {state ->

            when(state) {
                is SearchState.Data -> {
                    productAdapter.submitList(state.products)
                }
                is SearchState.Error -> {
                    Snackbar.make(requireView(), "There is no such a book!", 1000).show()
                }
            }
        }
    }

   /* override fun onProductClick(id: Int) {
        val action = SearchFragmentDirections.searchTodetail(id)
        findNavController().navigate(action)
    }*/

    override fun onFavButtonClick(product: ProductUI) {
         viewModel.addToFavorites(product)
    }
}
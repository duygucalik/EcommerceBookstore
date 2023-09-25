package com.example.ecommercebookstore.ui.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.common.gone
import com.example.ecommercebookstore.common.visible
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductsAdapter.ProductListener, SaleAdapter.ProductListener {

    private lateinit var binding : FragmentHomeBinding
    private val productsAdapter by lazy {ProductsAdapter(this)}
    private val saleAdapter by lazy { SaleAdapter(this) }
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllbooks.adapter = productsAdapter
        viewModel.getProducts()

        binding.rvSalebooks.adapter = saleAdapter
        viewModel.getSaleProducts()
        observeData()

        logOut()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onProductClick(id: Int) {
        val action = HomeFragmentDirections.homeToDetail(id)
        findNavController().navigate(action)
    }

    override fun onFavoriteButtonClick(product: ProductUI) {
        viewModel.addToFavorites(product)
    }

    private fun observeData() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is HomeState.Data -> {
                    productsAdapter.submitList(state.products)
                    binding.progressBar.gone()
                }
                is HomeState.SaleData -> {
                    saleAdapter.submitList(state.products)
                    binding.progressBar.gone()
                }
                is HomeState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                    binding.progressBar.gone()
                }
                is HomeState.Loading -> {
                    binding.progressBar.visible()
                }
            }
        }
    }

    override fun onSaleClick(id: Int) {
        val action = HomeFragmentDirections.homeToDetail(id)
        findNavController().navigate(action)
    }

    private fun logOut() {
        val showPopUp = PopupMenu(
            context,
            binding.ivSignOutIcon
        )

        showPopUp.menu.add(Menu.NONE, 0, 0, "Log Out")

        showPopUp.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                FirebaseAuth.getInstance().signOut()
                val action = HomeFragmentDirections.homeToSignin()
                findNavController().navigate(action)
            }
            false
        }

        binding.ivSignOutIcon.setOnClickListener {
            showPopUp.show()
        }
    }
}
package com.example.ecommercebookstore.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.common.gone
import com.example.ecommercebookstore.common.visible
import com.example.ecommercebookstore.databinding.FragmentCartBinding
import com.example.ecommercebookstore.ui.login.signIn.UserAuthViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.ProductListener {

    private lateinit var binding : FragmentCartBinding
    private val cartAdapter by lazy { CartAdapter(this) }
    private val viewModel by viewModels<CartViewModel>()
    private val authViewModel by viewModels<UserAuthViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCart.adapter = cartAdapter
        viewModel.getCartProducts(auth.currentUser?.uid.toString())

        binding.btnClearBasket.setOnClickListener {
            viewModel.clearCart(auth.currentUser?.uid.toString())
        }
        payButtonClick()
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is CartState.Data -> {
                    rvCart.visible()
                    cartAdapter.submitList(state.products)
                }
                is CartState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                    rvCart.gone()
                }
                is CartState.DeleteFromCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
                is CartState.ClearCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
            }
        }

        viewModel.totalPriceAmount.observe(viewLifecycleOwner) {
            totalPriceInCart.text = String.format("$ %.2f", it)
        }
    }

    override fun onCartItemClick(id: Int) {
        val action = CartFragmentDirections.cartTodetail(id)
        findNavController().navigate(action)
    }

    override fun onDeleteItemClick(id: Int) {
        viewModel.deleteFromCart(id)
    }

    override fun onIncreaseClick(price: Double?) {
        viewModel.increase(price)
    }

    override fun onDecreaseClick(price: Double?) {
        viewModel.decrease(price)
    }

    private fun payButtonClick() {

        binding.btnOrder.setOnClickListener {
            val action = CartFragmentDirections.cartTopayment()
            findNavController().navigate(action)
        }
    }
}
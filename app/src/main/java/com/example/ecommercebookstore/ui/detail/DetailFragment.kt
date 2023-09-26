package com.example.ecommercebookstore.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommercebookstore.common.loadImage
import com.example.ecommercebookstore.data.model.AddToCartRequest
import com.example.ecommercebookstore.data.model.Product
import com.example.ecommercebookstore.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding
    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductDetail(args.id)
        observeData()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddToCartDetail.setOnClickListener {
            val addToCartRequest = AddToCartRequest(auth.currentUser?.uid.toString(), args.id)
            viewModel.addToCart(addToCartRequest)
        }
    }

    private fun observeData() = with(binding) {

        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailState.Data -> {
                    ivDetail.loadImage(state.product.imageOne)
                    tvDetailTitle.text = state.product.title
                    tvDetailPrice.text = " ${state.product.price} ₺"
                    tvDetailCategories.text = state.product.category
                    tvDetailDesc.text = state.product.description
                    ratingBar.rating = ((state.product.rate)?.toFloat() ?: 1) as Float
                }

                is DetailState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }

                is DetailState.AddToBag -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
            }
        }
    }
}
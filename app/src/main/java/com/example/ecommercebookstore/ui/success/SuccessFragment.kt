package com.example.ecommercebookstore.ui.success

import android.os.Handler
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.R
import com.example.ecommercebookstore.common.visible
import com.example.ecommercebookstore.databinding.FragmentSuccessBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessFragment: Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    private val viewModel by viewModels<SuccessViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimSuccess()

        binding.goBackButton.setOnClickListener {
            onGoBackButtonClick()
            viewModel.clearCart(auth.currentUser?.uid.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root

    }

    private fun onGoBackButtonClick() = with(binding) {
        findNavController()
            .navigate(
                R.id.homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.cartFragment, true)
                    .build()
            )

    }

    private fun playAnimSuccess() = with(binding) {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            paymentSuccess.visible()
            paymentSuccess.playAnimation()
        }, 2000)
    }
}
package com.example.ecommercebookstore.ui.login.signUp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.R
import com.example.ecommercebookstore.databinding.FragmentSignupBinding
import com.example.ecommercebookstore.ui.login.signIn.UserAuthState
import com.example.ecommercebookstore.ui.login.signIn.UserAuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel by viewModels<UserAuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        with(binding) {
            btnSignUp.setOnClickListener {
                val email = etEmailSignup.text.toString()
                val password = etPasswordSignup.text.toString()

                if(email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signUpUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show()
                }
            }
            tvHaveaccount.setOnClickListener {
                findNavController().navigate(R.id.SignupToSignin)
            }
        }

    }


    private fun observeData() = with(binding) {

        viewModel.authState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is UserAuthState.Data -> {
                    findNavController().navigate(R.id.SignupToHome)
                }

                is UserAuthState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
}
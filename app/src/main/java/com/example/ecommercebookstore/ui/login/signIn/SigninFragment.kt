package com.example.ecommercebookstore.ui.login.signIn

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.R
import com.example.ecommercebookstore.databinding.FragmentSigninBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigninFragment : Fragment(R.layout.fragment_signin) {

    private lateinit var binding: FragmentSigninBinding
    private val viewModel by viewModels<UserAuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        viewModel.currentUser?.let {
            findNavController().navigate(R.id.SigninToHome)
        }

        with(binding) {
            btnSignIn.setOnClickListener {
                val email = etMail.text.toString()
                val password = etPassword.text.toString()

                if(email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show()
                }
            }
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.SigninToSignup)
            }
        }

    }

    private fun observeData() = with(binding) {

        viewModel.authState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is UserAuthState.Data -> {
                    findNavController().navigate(R.id.SigninToHome)
                }

                is UserAuthState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
}
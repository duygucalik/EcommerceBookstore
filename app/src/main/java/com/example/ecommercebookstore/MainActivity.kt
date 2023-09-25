package com.example.ecommercebookstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ecommercebookstore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_EcommerceBookstore)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment

        with(binding) {
            NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)

            navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

                when(destination.id) {
                    R.id.signinFragment, R.id.signUpFragment, R.id.detailFragment, R.id.paymentFragment, R.id.successFragment -> {
                        bottomNav.visibility = View.GONE
                    }
                    else -> {
                        bottomNav.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
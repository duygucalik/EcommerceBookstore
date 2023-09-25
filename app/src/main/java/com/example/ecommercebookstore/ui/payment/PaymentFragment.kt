package com.example.ecommercebookstore.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommercebookstore.R
import com.example.ecommercebookstore.common.CreditCardFormatter
import com.example.ecommercebookstore.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    private val monthList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val yearList = listOf(23, 24, 25, 26, 27, 28, 29, 30, 31, 32)

    private var monthValue = 0
    private var yearValue = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentMonth()
        paymentYear()
        paymentCreditCard()

        with(binding) {
            paymentButton.setOnClickListener {
                if(paymentName.text.isNotEmpty() && paymentAddress.text.isNotEmpty()
                    && paymentCvc.text.isNotEmpty() && paymentAddress.text.isNotEmpty()
                    && checkMonth(monthValue) && checkYear(yearValue)) {
                    findNavController().navigate(R.id.paymentTosuccess)
                } else {
                    Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun paymentMonth() = with(binding) {
        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, monthList)
        actPaymentMonth.setAdapter(monthAdapter)

        actPaymentMonth.setOnItemClickListener { _, _, position, _ ->
            monthValue = monthList[position]
        }
    }

    private fun paymentYear() = with(binding) {
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, yearList)
        actPaymentYear.setAdapter(yearAdapter)

        actPaymentYear.setOnItemClickListener { _, _, position, _ ->
            yearValue = yearList[position]
        }
    }

    private fun paymentCreditCard() = with(binding) {
        paymentCreditCard.addTextChangedListener(CreditCardFormatter())
    }

    private fun checkMonth(value: Int) : Boolean {
        return value in monthList
    }

    private fun checkYear(value: Int) : Boolean {
        return value in yearList
    }

}
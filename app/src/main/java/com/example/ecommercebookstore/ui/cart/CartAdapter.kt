package com.example.ecommercebookstore.ui.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebookstore.R
import com.example.ecommercebookstore.common.loadImage
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.databinding.ItemCartBinding

class CartAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, CartAdapter.CartItemViewHolder>(ProductDiffCallBack()) {

    class CartItemViewHolder(
        private val binding: ItemCartBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            var productCount = 1

            tvCartTitle.text = product.title
            ivCart.loadImage(product.imageOne)
            tvCartPrice.text = " ${product.price} ₺"

            if (product.saleState==true) {
                tvCartSale.text = " ${product.salePrice} ₺"
                tvCartPrice.setTextColor(Color.parseColor("#FF0000"))
                tvCartPrice.setBackgroundResource(R.drawable.strike_through)
            }

            btnDeleteCart.setOnClickListener {
                productListener.onDeleteItemClick(product.id)
            }

            root.setOnClickListener {
                productListener.onCartItemClick(product.id)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false), productListener
        )
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface ProductListener {
        fun onCartItemClick(id:Int)
        fun onDeleteItemClick(id:Int)
        fun onIncreaseClick(price:Double?)
        fun onDecreaseClick(price:Double?)
    }
}


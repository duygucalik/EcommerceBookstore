package com.example.ecommercebookstore.ui.homepage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebookstore.R
import com.example.ecommercebookstore.common.loadImage
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.databinding.ItemProductsBinding

class ProductsAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, ProductsAdapter.ProductsViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(
            ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false), productListener
        )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductsViewHolder(
        private val binding: ItemProductsBinding,
        private val productListener: ProductListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvName.text = product.title
            tvPrice.text = "${product.price} ₺"
            ivProduct.loadImage(product.imageOne)

            var isLiked = product.isFavorite

            btnFavorite.setOnClickListener {
                isLiked = !isLiked
                btnFavorite.apply {
                    if (isLiked) {
                        productListener.onFavoriteButtonClick(product)
                        playAnimation()
                    } else {
                        cancelAnimation()
                        progress = 0.0f
                    }
                }
            }

            if(product.isFavorite) {
                btnFavorite.playAnimation()
            }


            if (product.saleState) {
                tvSalePrice.text = " ${product.salePrice} ₺"
                tvPrice.setTextColor(Color.parseColor("#FF0000"))
                tvPrice.setBackgroundResource(R.drawable.strike_through)
            }

            root.setOnClickListener {
                productListener.onProductClick(product.id)
            }
        }
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
        fun onProductClick(id:Int)
        fun onFavoriteButtonClick(product: ProductUI)
    }

}
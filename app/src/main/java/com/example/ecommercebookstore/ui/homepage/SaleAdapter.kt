package com.example.ecommercebookstore.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebookstore.common.loadImage
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.databinding.ItemSaleBooksBinding

class SaleAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, SaleAdapter.SaleViewHolder>(ProductDiffCallBack()) {

    class SaleViewHolder(
        private val binding: ItemSaleBooksBinding,
        private val productListener: ProductListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvName.text = product.title
            tvPrice.text = " ${product.price} ₺"
            tvSalePrice.text = " ${product.salePrice} ₺"

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

            ivProduct.loadImage(product.imageOne)

            root.setOnClickListener {
                productListener.onSaleClick(product.id ?: 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder =
        SaleViewHolder(
            ItemSaleBooksBinding.inflate(LayoutInflater.from(parent.context), parent,false), productListener
        )

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
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
        fun onSaleClick(id:Int)
        fun onFavoriteButtonClick(product: ProductUI)
    }
}
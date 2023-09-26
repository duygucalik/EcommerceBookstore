package com.example.ecommercebookstore.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercebookstore.common.loadImage
import com.example.ecommercebookstore.data.model.ProductUI
import com.example.ecommercebookstore.databinding.ItemFavoriteBinding
import com.example.ecommercebookstore.databinding.ItemProductsBinding

class FavoritesAdapter(
    private val favoriteProductListener: FavoriteProductListener
) : ListAdapter<ProductUI, FavoritesAdapter.FavoriteProductViewHolder>(ProductDiffCallBack()) {

    class FavoriteProductViewHolder(
        private val binding: ItemFavoriteBinding,
        private val favoriteProductListener: FavoriteProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvCartTitle.text = product.title
            tvCartPrice.text = " ${product.price} ₺"
            ivCart.loadImage(product.imageOne)
            btnFavorite.playAnimation()

            btnFavorite.setOnClickListener {
                favoriteProductListener.onFavButtonClick(product)
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

    interface FavoriteProductListener {
        fun onFavButtonClick(product: ProductUI)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteProductViewHolder {
        return FavoriteProductViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false), favoriteProductListener
        )
    }

    override fun onBindViewHolder(
        holder: FavoriteProductViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}
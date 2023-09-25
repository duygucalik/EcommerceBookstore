package com.example.ecommercebookstore.data.source.local

import androidx.room.*
import com.example.ecommercebookstore.data.model.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(product: ProductEntity)

    @Query("SELECT * FROM favorite_products")
    suspend fun getFavorites(): List<ProductEntity>

    @Delete
    suspend fun deleteFavProduct(product: ProductEntity)

    @Query("SELECT title FROM favorite_products")
    suspend fun getFavoritesTitles(): List<String>
}
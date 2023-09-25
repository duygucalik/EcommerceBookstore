package com.example.ecommercebookstore.di

import com.example.ecommercebookstore.data.repository.ProductsRepository
import com.example.ecommercebookstore.data.source.local.ProductDao
import com.example.ecommercebookstore.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(movieService: ProductService, productDao: ProductDao): ProductsRepository =
        ProductsRepository(movieService, productDao)
}
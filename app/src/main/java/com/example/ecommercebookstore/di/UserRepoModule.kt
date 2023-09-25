package com.example.ecommercebookstore.di

import com.example.ecommercebookstore.data.repository.UserRepo
import com.example.ecommercebookstore.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserRepoModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideUserRepository(userRepository: UserRepository): UserRepo = userRepository
}
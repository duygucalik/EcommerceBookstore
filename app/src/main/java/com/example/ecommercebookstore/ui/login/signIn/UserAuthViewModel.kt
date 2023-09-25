package com.example.ecommercebookstore.ui.login.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercebookstore.common.Resource
import com.example.ecommercebookstore.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(private val userRepo: UserRepository) : ViewModel() {

    private var _authState = MutableLiveData<UserAuthState>()
    val authState: LiveData<UserAuthState>
        get() = _authState

    val currentUser: FirebaseUser?
        get() = userRepo.currentUser

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepo.signUpUser(email, password)

            if (result is Resource.Success) {
                _authState.value = UserAuthState.Data(result.data)
            } else if (result is Resource.Error) {
                _authState.value = UserAuthState.Error(result.throwable)
            }
        }
    }


    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepo.signInUser(email, password)

            if (result is Resource.Success) {
                _authState.value = UserAuthState.Data(result.data)
            } else if (result is Resource.Error) {
                _authState.value = UserAuthState.Error(result.throwable)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }


}

sealed interface UserAuthState {
    data class Data(val user: FirebaseUser) : UserAuthState
    data class Error(val throwable: Throwable) : UserAuthState
}
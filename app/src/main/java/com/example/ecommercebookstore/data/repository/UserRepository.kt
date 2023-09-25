package com.example.ecommercebookstore.data.repository

import com.example.ecommercebookstore.common.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class UserRepository  @Inject constructor (
    private val firebaseAuth: FirebaseAuth
) : UserRepo {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun getFirebaseUserUid(): String = firebaseAuth.currentUser?.uid.orEmpty()

    fun checkUserLogin(): Boolean = firebaseAuth.currentUser != null

    suspend fun signInUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    suspend fun signUpUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}
interface UserRepo {
    val currentUser: FirebaseUser?
}
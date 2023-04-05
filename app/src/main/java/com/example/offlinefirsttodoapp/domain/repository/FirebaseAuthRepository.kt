package com.example.offlinefirsttodoapp.domain.repository

import androidx.activity.result.ActivityResult
import com.example.offlinefirsttodoapp.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    val googleSignInClient: GoogleSignInClient

    suspend fun loginWithGoogle(
        result: ActivityResult
    ): Flow<Resource<FirebaseUser?>>

    fun getUser(): FirebaseUser?

    fun signOut()
}

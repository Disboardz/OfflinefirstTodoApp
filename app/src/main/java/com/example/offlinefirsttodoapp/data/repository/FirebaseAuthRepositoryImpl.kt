package com.example.offlinefirsttodoapp.data.repository


import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.repository.FirebaseAuthRepository
import com.example.offlinefirsttodoapp.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FirebaseAuthRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val googleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.oauth_client_id))
            .build()
    override val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)


    override suspend fun loginWithGoogle(result: ActivityResult): Flow<Resource<FirebaseUser?>> {
        return flow {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                coroutineScope {
                    val authResult = Firebase.auth.signInWithCredential(credential).await()
                    emit(Resource.Success(authResult.user))
                }
            } catch (e: ApiException) {
                Log.e("Google", "Exception cought in loginWithGoogle method: ${e.stackTraceToString()}")
                emit(Resource.Error(message = e.message ?: "Ocurrio un error con google"))
            }
        }
    }

    override fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}

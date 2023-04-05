package com.example.offlinefirsttodoapp.ui.screens.login

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinefirsttodoapp.domain.repository.FirebaseAuthRepository
import com.example.offlinefirsttodoapp.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
): ViewModel() {
    private var _state = MutableStateFlow(LoginViewModelState())
    val state = _state.asStateFlow()

    fun loginWithGoogle(activityResult: ActivityResult) {
        viewModelScope.launch {
            authRepository.loginWithGoogle(activityResult).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.update { state -> state.copy(user = result.data) }
                        Log.d("Login", result.data.toString())
                    }
                    is Resource.Error -> {
                        Log.d("Login", result.message!!)
                        _state.update { state -> state.copy(error = result.message) }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun getGoogleCredentials(): Intent {
        return authRepository.googleSignInClient.signInIntent
    }
}

data class LoginViewModelState(
    val user: FirebaseUser? = null,
    val error: String? = null
)

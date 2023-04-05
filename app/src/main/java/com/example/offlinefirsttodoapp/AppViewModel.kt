package com.example.offlinefirsttodoapp

import androidx.lifecycle.ViewModel
import com.example.offlinefirsttodoapp.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
): ViewModel() {
    private var _state = MutableStateFlow(AppViewModelState())
    val state = _state.asStateFlow()
}

data class AppViewModelState(
    val user: FirebaseUser? = null
)

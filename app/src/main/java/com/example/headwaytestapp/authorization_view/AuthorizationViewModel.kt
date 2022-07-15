package com.example.headwaytestapp.authorization_view

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject() constructor() : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth

    private val _authorizationUiState = MutableStateFlow<UiStateManager>(UiStateManager.InProgress)
    val authorizationUiState: StateFlow<UiStateManager> get() = _authorizationUiState.asStateFlow()

    fun authorization(email: String) {
        _authorizationUiState.value = UiStateManager.InProgress
        firebaseAuth = Firebase.auth

        val provider = OAuthProvider.newBuilder("github.com")
        provider.addCustomParameter("login", email)

        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    _authorizationUiState.value = UiStateManager.SuccessLogIn
                }
                .addOnFailureListener {
                    _authorizationUiState.value = UiStateManager.ErrorLogIn(it.message)
                }
        } else {
            _authorizationUiState.value = UiStateManager.OpenLogIn(provider)
        }
    }
}
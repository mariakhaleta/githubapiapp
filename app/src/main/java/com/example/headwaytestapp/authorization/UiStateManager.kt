package com.example.headwaytestapp.authorization

import com.google.firebase.auth.OAuthProvider

sealed class UiStateManager {
    object InProgress : UiStateManager()

    data class SuccessLogIn(val token: String) : UiStateManager()
    data class ErrorLogIn(val message: String?) : UiStateManager()

    data class OpenLogIn(val provider: OAuthProvider.Builder) : UiStateManager()
}
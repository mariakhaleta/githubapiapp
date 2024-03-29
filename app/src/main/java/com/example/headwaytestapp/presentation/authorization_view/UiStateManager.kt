package com.example.headwaytestapp.presentation.authorization_view

import com.google.firebase.auth.OAuthProvider

sealed class UiStateManager {
    object InProgress : UiStateManager()
    object SuccessLogIn : UiStateManager()

    data class ErrorLogIn(val message: String) : UiStateManager()
    data class OpenLogIn(val provider: OAuthProvider.Builder) : UiStateManager()
}
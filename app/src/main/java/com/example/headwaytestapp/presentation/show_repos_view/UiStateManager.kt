package com.example.headwaytestapp.presentation.show_repos_view

import com.example.headwaytestapp.data.dao.Repository


sealed class UiStateManager {
    object Loading: UiStateManager()
    object Initial: UiStateManager()
    object Empty: UiStateManager()

    data class Success(var data: List<Repository>): UiStateManager()
    data class Error(val message: String?): UiStateManager()
}
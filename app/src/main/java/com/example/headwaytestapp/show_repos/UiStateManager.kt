package com.example.headwaytestapp.show_repos

import com.example.headwaytestapp.dao.Repository


sealed class UiStateManager {
    object Loading: UiStateManager()

    data class Success(var data: List<Repository>): UiStateManager()
    object Empty: UiStateManager()
    data class Error(val message: String?): UiStateManager()
}
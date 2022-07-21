package com.example.headwaytestapp.domain


import com.example.headwaytestapp.presentation.show_repos_view.UiStateManager
import kotlinx.coroutines.flow.Flow

interface RepositorySearchUseCase {
    suspend fun getRepositoryListBy(searchKey: String, currentPage: Int): Flow<UiStateManager>
}
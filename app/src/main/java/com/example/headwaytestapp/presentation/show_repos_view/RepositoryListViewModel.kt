package com.example.headwaytestapp.presentation.show_repos_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headwaytestapp.domain.RepositorySearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryListViewModel @Inject constructor(private val useCase: RepositorySearchUseCase) :
    ViewModel() {

    private val _repoListUiState = MutableStateFlow<UiStateManager>(UiStateManager.Initial)
    val repoListUiState: StateFlow<UiStateManager> get() = _repoListUiState.asStateFlow()

    private var currentPage = 1

    fun searchRepos(searchKey: String) {
        _repoListUiState.value = UiStateManager.Loading
        viewModelScope.launch {
            useCase.getRepositoryListBy(searchKey, currentPage).collect {
                _repoListUiState.value = it
            }
        }
    }

    fun pagination(totalItemCount: Int?, dy: Int, latestVisibleItem: Int, currentSearchKey: String) {
        if (dy > 0) {
            if (totalItemCount != null) {
                if (latestVisibleItem == totalItemCount - 1) {
                    currentPage += 2
                    searchRepos(currentSearchKey)
                }
            }
        }
    }

    fun emptyResult() {
        _repoListUiState.value = UiStateManager.Success(emptyList())
        _repoListUiState.value = UiStateManager.Initial
    }
}
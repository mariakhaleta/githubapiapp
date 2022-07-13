package com.example.headwaytestapp.show_repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headwaytestapp.dao.Repository
import com.example.headwaytestapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryListViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    private val _repoListUiState = MutableStateFlow<UiStateManager>(UiStateManager.Loading)
    val repoListUiState: StateFlow<UiStateManager> get() = _repoListUiState.asStateFlow()

    private val _repoListState = MutableStateFlow<List<Repository>>(emptyList())

    fun searchRepos(searchKey: String, currentPage: Int) {
        viewModelScope.launch {
            val repository1 = async {
                mainRepository.searchRepos(searchKey, currentPage, 15)
            }
            val repository2 = async {
                mainRepository.searchRepos(searchKey, currentPage + 1, 15)
            }
            val finalList = (repository1.await().plus(repository2.await()))

            _repoListState.value = _repoListState.value.plus(finalList).sortedByDescending {
                it.stars
            }

            _repoListUiState.value = UiStateManager.Success(_repoListState.value)
            launch(Dispatchers.Default) {
                mainRepository.saveLatestRepos(_repoListState.value.subList(0, 19)) // save first 20 elements
            }
        }
    }
}
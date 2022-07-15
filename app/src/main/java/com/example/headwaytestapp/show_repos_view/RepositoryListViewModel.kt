package com.example.headwaytestapp.show_repos_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headwaytestapp.dao.Repository
import com.example.headwaytestapp.network.GithubSearchResponse
import com.example.headwaytestapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RepositoryListViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val _repoListUiState = MutableStateFlow<UiStateManager>(UiStateManager.Initial)
    val repoListUiState: StateFlow<UiStateManager> get() = _repoListUiState.asStateFlow()

    private val _repoListState = MutableStateFlow<List<Repository>>(emptyList())

    fun searchRepos(searchKey: String, currentPage: Int) {
        _repoListUiState.value = UiStateManager.Loading
        viewModelScope.launch {
            combineTransform(
                mainRepository.searchRepos(
                    searchKey,
                    currentPage,
                    15
                ), //search 30 repos in 2 request(15+15)
                mainRepository.searchRepos(searchKey, currentPage + 1, 15)
            ) { searchResult1, searchResult2 ->
                if (!searchResult1.isSuccessful) {
                    _repoListUiState.value = UiStateManager.Error(errorHandling(searchResult1))
                    return@combineTransform
                }

                if (!searchResult2.isSuccessful) {
                    _repoListUiState.value = UiStateManager.Error(errorHandling(searchResult2))
                    return@combineTransform
                }

                val list1: List<Repository>? = searchResult1.body()?.items
                val list2: List<Repository>? = searchResult2.body()?.items

                if (list1.isNullOrEmpty()) {
                    _repoListUiState.value = UiStateManager.Empty
                    return@combineTransform
                }

                if (list2.isNullOrEmpty()) {
                    _repoListUiState.value = UiStateManager.Empty
                    return@combineTransform
                }

                if (searchResult1.isSuccessful && searchResult2.isSuccessful) {
                    val newReposList = list1.plus(list2).sortedByDescending {
                        it.stars
                    }
                    _repoListState.value =
                        _repoListState.value.plus(newReposList)

                    launch(Dispatchers.Default) {
                        mainRepository.saveLatestRepos(
                            _repoListState.value.subList(
                                0,
                                19
                            )
                        ) // save first 20 elements
                    }

                    emit(_repoListState.value)
                }
            }.collect { repositoriesList ->
                _repoListUiState.value = if (repositoriesList.isNullOrEmpty()) {
                    UiStateManager.Empty
                } else {
                    UiStateManager.Success(repositoriesList)
                }
            }
        }
    }

    private fun errorHandling(errorResponse: Response<GithubSearchResponse>): String {
        return errorResponse.errorBody()?.string()?.let {
            JSONObject(it).getString("message")
        } ?: run {
            errorResponse.code().toString()
        }
    }

    fun emptyResult() {
        _repoListUiState.value = UiStateManager.Success(emptyList())
        _repoListState.value = emptyList()
        _repoListUiState.value = UiStateManager.Initial
    }
}
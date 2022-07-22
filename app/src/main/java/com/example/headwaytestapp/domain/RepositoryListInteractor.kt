package com.example.headwaytestapp.domain

import com.example.headwaytestapp.data.dao.Repository
import com.example.headwaytestapp.data.network.GithubSearchResponse
import com.example.headwaytestapp.presentation.show_repos_view.UiStateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RepositoryListInteractor @Inject constructor(private val mainRepository: MainRepository)
    : RepositorySearchUseCase {

    private val _repoListUiState = MutableStateFlow<UiStateManager>(UiStateManager.Initial)
    private val _repoListState = MutableStateFlow<List<Repository>>(emptyList())

    override suspend fun getRepositoryListBy(searchKey: String, currentPage: Int):
            Flow<UiStateManager> {
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

                coroutineScope {
                    launch(Dispatchers.Default) {
                        mainRepository.saveLatestRepos(
                            _repoListState.value.subList(
                                0,
                                19
                            )
                        ) // save first 20 elements
                    }
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
        return _repoListUiState
    }

    private fun errorHandling(errorResponse: Response<GithubSearchResponse>): String {
        return errorResponse.errorBody()?.string()?.let {
            JSONObject(it).getString("message")
        } ?: run {
            errorResponse.code().toString()
        }
    }
}
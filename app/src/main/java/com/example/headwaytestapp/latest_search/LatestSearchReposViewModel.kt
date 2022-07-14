package com.example.headwaytestapp.latest_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headwaytestapp.repository.MainRepository
import com.example.headwaytestapp.dao.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestSearchReposViewModel @Inject() constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _repoListState = MutableStateFlow<List<Repository>>(emptyList())
    val repoListState: StateFlow<List<Repository>> get() = _repoListState.asStateFlow()

    fun showLatestSearch() {
        viewModelScope.launch {
            mainRepository.showLatestRepos().collect {
                _repoListState.value = it
            }
        }
    }
}
package com.example.headwaytestapp.domain

import com.example.headwaytestapp.data.dao.Repository
import com.example.headwaytestapp.data.dao.RepositoryDAO
import com.example.headwaytestapp.data.network.GithubApi
import com.example.headwaytestapp.data.network.GithubSearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val githubApi: GithubApi,
    private val repositoryDAO: RepositoryDAO
) {
    suspend fun searchRepos(
        searchKey: String,
        pageIndex: Int,
        perPage: Int
    ): Flow<Response<GithubSearchResponse>> {
        return flow { emit(githubApi.searchRepos(searchKey, pageIndex, perPage)) }
    }

    fun saveLatestRepos(repositoriesList: List<Repository>) {
        repositoryDAO.deleteAllData()
        repositoryDAO.insertRepositories(repositoriesList)
    }

    fun showLatestRepos(): Flow<List<Repository>> {
        return repositoryDAO.loadLatestRepositoryList()
    }
}
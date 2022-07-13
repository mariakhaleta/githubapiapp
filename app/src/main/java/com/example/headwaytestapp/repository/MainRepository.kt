package com.example.headwaytestapp.repository

import com.example.headwaytestapp.dao.RepositoryDAO
import com.example.headwaytestapp.network.GithubApi
import com.example.headwaytestapp.dao.Repository
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val githubApi: GithubApi,
    private val repositoryDAO: RepositoryDAO
) {
    suspend fun searchRepos(searchKey: String, pageIndex: Int, perPage: Int): List<Repository> {
        return githubApi.searchRepos(searchKey, pageIndex, perPage).body()!!.items
    }

    fun saveLatestRepos(repositoriesList: List<Repository>) {
        repositoryDAO.deleteAllData()
        repositoryDAO.insertRepositories(repositoriesList)
    }

    fun showLatestRepos() : List<Repository> {
        return repositoryDAO.loadLatestRepositoryList()
    }
}
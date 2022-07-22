package com.example.headwaytestapp.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


const val GET_REPOS: String = "search/repositories"

interface GithubApi {
    @GET(GET_REPOS)
    suspend fun searchRepos(
        @Query("q") searchQuery: String,
        @Query("page") pageIndex: Int,
        @Query("per_page") perPage: Int
    ): Response<GithubSearchResponse>
}
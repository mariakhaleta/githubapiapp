package com.example.headwaytestapp.data.network

import com.example.headwaytestapp.data.dao.Repository
import com.google.gson.annotations.SerializedName

data class GithubSearchResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<Repository>
)
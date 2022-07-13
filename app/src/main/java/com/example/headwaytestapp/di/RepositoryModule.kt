package com.example.headwaytestapp.di

import com.example.headwaytestapp.dao.RepositoryDAO
import com.example.headwaytestapp.network.GithubApi
import com.example.headwaytestapp.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(githubApi: GithubApi, repositoryDAO: RepositoryDAO): MainRepository =
        MainRepository(githubApi, repositoryDAO)
}
package com.example.headwaytestapp.utils.di

import com.example.headwaytestapp.data.dao.RepositoryDAO
import com.example.headwaytestapp.data.network.GithubApi
import com.example.headwaytestapp.domain.MainRepository
import com.example.headwaytestapp.domain.RepositoryListInteractor
import com.example.headwaytestapp.domain.RepositorySearchUseCase
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

    @Provides
    fun provideUsecase(repositoryListInteractor: RepositoryListInteractor): RepositorySearchUseCase =
        repositoryListInteractor
}
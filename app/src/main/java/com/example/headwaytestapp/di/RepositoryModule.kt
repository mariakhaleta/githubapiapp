package com.example.headwaytestapp.di

import com.example.headwaytestapp.authorization.GithubApi
import com.example.headwaytestapp.show_repos.MainRepository
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
    fun provideMainRepository(githubApi: GithubApi): MainRepository = MainRepository(githubApi)
}
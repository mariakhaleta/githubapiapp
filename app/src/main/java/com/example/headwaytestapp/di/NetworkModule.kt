package com.example.headwaytestapp.di

import android.content.Context
import androidx.room.Room
import com.example.headwaytestapp.BuildConfig
import com.example.headwaytestapp.dao.AppDatabase
import com.example.headwaytestapp.network.GithubApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

const val BASE_URL: String = "https://api.github.com/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun providesGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build()
        )
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Singleton
    @Provides
    fun provideYourDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    ).build()

    @Singleton
    @Provides
    fun provideRepositoryDao(db: AppDatabase) = db.repositoryDAO()

}
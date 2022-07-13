package com.example.headwaytestapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class RepositoryDAO {

    @Query("DELETE FROM repositories_list")
    abstract fun deleteAllData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepositories(repositoriesList: List<Repository>)

    @Query("SELECT * FROM repositories_list")
    abstract fun loadLatestRepositoryList() : List<Repository>
}
package com.ycj1212.covid19vaccinationcentermap.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CenterDao {

    @Query("SELECT * FROM center WHERE id = :id")
    fun getCenter(id: Int): Flow<Center>

    @Query("SELECT * FROM center")
    fun getCenters(): Flow<List<Center>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(centers: List<Center>)
}

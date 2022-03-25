package com.ycj1212.covid19vaccinationcentermap.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CenterDao {

    @Query("SELECT * FROM center WHERE id = :id")
    suspend fun getCenter(id: Int): Center

    @Query("SELECT * FROM center")
    suspend fun getCenters(): List<Center>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(centers: List<Center>)
}

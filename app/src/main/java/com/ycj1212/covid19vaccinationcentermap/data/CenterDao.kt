package com.ycj1212.covid19vaccinationcentermap.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CenterDao {

    @Query("SELECT * FROM center")
    fun getCenters(): List<Center>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(centers: List<Center>)
}

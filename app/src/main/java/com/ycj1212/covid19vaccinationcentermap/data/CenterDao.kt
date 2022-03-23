package com.ycj1212.covid19vaccinationcentermap.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CenterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(centers: List<Center>)
}

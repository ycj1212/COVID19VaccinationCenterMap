package com.ycj1212.covid19vaccinationcentermap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [Center::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val centerDao: CenterDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "center.db").build()

        fun getInstance(@ApplicationContext context: Context): AppDatabase =
            instance ?: synchronized(this) {
                buildDatabase(context).also { instance = it }
            }
    }
}

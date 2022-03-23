package com.ycj1212.covid19vaccinationcentermap.di

import android.content.Context
import com.ycj1212.covid19vaccinationcentermap.data.AppDatabase
import com.ycj1212.covid19vaccinationcentermap.data.CenterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideCenterDao(appDatabase: AppDatabase): CenterDao =
        appDatabase.centerDao
}

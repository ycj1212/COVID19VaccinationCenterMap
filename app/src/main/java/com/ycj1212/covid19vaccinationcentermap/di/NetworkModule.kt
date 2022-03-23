package com.ycj1212.covid19vaccinationcentermap.di

import com.ycj1212.covid19vaccinationcentermap.api.VaccinationCenterLocationInfoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideVaccinationCenterService(): VaccinationCenterLocationInfoService =
        VaccinationCenterLocationInfoService.create()
}

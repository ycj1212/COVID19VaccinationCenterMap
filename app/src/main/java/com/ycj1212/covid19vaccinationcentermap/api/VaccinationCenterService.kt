package com.ycj1212.covid19vaccinationcentermap.api

import com.ycj1212.covid19vaccinationcentermap.BuildConfig
import com.ycj1212.covid19vaccinationcentermap.data.CenterResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface VaccinationCenterLocationInfoService {
    @Headers("Accept: application/json")
    @GET("15077586/v1/centers")
    suspend fun getCenterList(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int = 10,
        @Query("serviceKey") serviceKey: String = BuildConfig.VACCINATION_CENTER_API_KEY
    ): CenterResponse

    companion object {
        private const val BASE_URL = "https://api.odcloud.kr/api/"

        fun create(): VaccinationCenterLocationInfoService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder().addInterceptor(logger).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VaccinationCenterLocationInfoService::class.java)
        }
    }
}

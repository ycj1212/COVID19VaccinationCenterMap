package com.ycj1212.covid19vaccinationcentermap.data

import com.ycj1212.covid19vaccinationcentermap.api.VaccinationCenterLocationInfoService

class CenterRepository (
    private val service: VaccinationCenterLocationInfoService,
) {
    suspend fun getCenterList(page: Int): List<Center> {
        if (page !in FIRST_PAGE..LAST_PAGE) return emptyList()
        val response = service.getCenterList(page)
        return response.data
    }

    companion object {
        const val FIRST_PAGE = 1
        const val LAST_PAGE = 10
    }
}

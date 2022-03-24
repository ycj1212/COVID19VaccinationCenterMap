package com.ycj1212.covid19vaccinationcentermap.data

import com.ycj1212.covid19vaccinationcentermap.api.VaccinationCenterLocationInfoService
import javax.inject.Inject

class CenterRepository @Inject constructor(
    private val service: VaccinationCenterLocationInfoService,
    private val centerDao: CenterDao
) {
    suspend fun getCenterList(page: Int): List<Center> {
        if (page !in FIRST_PAGE..LAST_PAGE) return emptyList()
        val response = service.getCenterList(page)
        return response.data
    }

    fun saveCenterList(centerList: List<Center>) {
        centerDao.insert(centerList)
    }

    fun getCenters(): List<Center> = centerDao.getCenters()

    companion object {
        const val FIRST_PAGE = 1
        const val LAST_PAGE = 10
    }
}

package com.ycj1212.covid19vaccinationcentermap.data

import com.ycj1212.covid19vaccinationcentermap.api.VaccinationCenterLocationInfoService
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CenterRepository @Inject constructor(
    private val service: VaccinationCenterLocationInfoService,
    private val centerDao: CenterDao
) {
    suspend fun loadAndSaveCenters() {
        (FIRST_PAGE..LAST_PAGE).forEach { page ->
            flow {
                emit(service.getCenterList(page).data)
            }.catch {
                emit(emptyList())
            }.collect { centerList ->
                centerDao.insert(centerList)
            }
        }
    }

    fun getCenters(): Flow<List<Center>> = centerDao.getCenters()

    fun getCenter(id: Int): Flow<Center> = centerDao.getCenter(id)

    companion object {
        const val FIRST_PAGE = 1
        const val LAST_PAGE = 10
    }
}

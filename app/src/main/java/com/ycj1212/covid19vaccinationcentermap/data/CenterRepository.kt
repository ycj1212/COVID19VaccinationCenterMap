package com.ycj1212.covid19vaccinationcentermap.data

import com.ycj1212.covid19vaccinationcentermap.api.VaccinationCenterLocationInfoService
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CenterRepository @Inject constructor(
    private val service: VaccinationCenterLocationInfoService,
    private val centerDao: CenterDao
) {
    /**
     * API를 통해 1페이지(page)에 10개(perPage)씩 순서대로 10개 페이지 호출(총 100개)하고 데이터베이스에 저장합니다.
     */
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

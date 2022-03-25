package com.ycj1212.covid19vaccinationcentermap.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycj1212.covid19vaccinationcentermap.data.Center
import com.ycj1212.covid19vaccinationcentermap.data.CenterRepository
import com.ycj1212.covid19vaccinationcentermap.data.InfoWindowUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: CenterRepository
) : ViewModel() {
    val centerList: Flow<List<Center>> = repository.getCenters()

    private val _selectedMarkerInfo = MutableStateFlow<InfoWindowUiState?>(null)
    val selectedMarkerInfo: StateFlow<InfoWindowUiState?> = _selectedMarkerInfo

    private val _isVisibleWindow = MutableStateFlow(false)
    val isVisibleWindow: StateFlow<Boolean> = _isVisibleWindow

    /**
     * 마커를 클릭한 경우 정보안내창의 가시성을 처리합니다.
     *
     * @param id 클릭한 마커에 해당하는 예방접종센터 id
     */
    fun clickMarker(id: Int) {
        if (selectedMarkerInfo.value?.id == id && isVisibleWindow.value) {
            hideInfoWindow()
        } else {
            showInfoWindow(id)
        }
    }

    /**
     * 정보안내창을 보이도록 처리합니다.
     * 마커를 클릭한 경우 호출됩니다.
     *
     * @param id 클릭한 마커에 해당하는 예방접종센터 id
     */
    private fun showInfoWindow(id: Int) {
        viewModelScope.launch {
            repository.getCenter(id).collect { center ->
                _isVisibleWindow.value = true
                _selectedMarkerInfo.value = InfoWindowUiState(
                    id = center.id,
                    address = center.address,
                    centerName = center.centerName,
                    facilityName = center.facilityName,
                    phoneNumber = center.phoneNumber,
                    updatedAt = center.updatedAt
                )
            }
        }
    }

    /**
     * 정보안내창을 숨깁니다.
     * 선택한 마커를 재선택하는 경우, 마커가 아닌 지도를 클릭한 경우, 정보안내창이 보이고 있을 때 뒤로가기 버튼을 클릭한 경우에 호출됩니다.
     */
    fun hideInfoWindow() {
        _isVisibleWindow.value = false
    }
}

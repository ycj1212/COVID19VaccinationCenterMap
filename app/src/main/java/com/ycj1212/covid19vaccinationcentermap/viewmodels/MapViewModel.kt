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

    fun clickMarker(id: Int) {
        if (selectedMarkerInfo.value?.id == id && isVisibleWindow.value) {
            hideInfoWindow()
        } else {
            showInfoWindow(id)
        }
    }

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

    fun hideInfoWindow() {
        _isVisibleWindow.value = false
    }
}

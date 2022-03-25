package com.ycj1212.covid19vaccinationcentermap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycj1212.covid19vaccinationcentermap.data.Center
import com.ycj1212.covid19vaccinationcentermap.data.CenterRepository
import com.ycj1212.covid19vaccinationcentermap.data.InfoWindowUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: CenterRepository
) : ViewModel() {
    private val _centerList = MutableLiveData<List<Center>>()
    val centerList: LiveData<List<Center>> = _centerList

    private val _selectedMarkerInfo = MutableLiveData<InfoWindowUiState>()
    val selectedMarkerInfo: LiveData<InfoWindowUiState> = _selectedMarkerInfo

    private val _isVisibleWindow = MutableLiveData<Boolean>()
    val isVisibleWindow: LiveData<Boolean> = _isVisibleWindow

    fun getCenters() {
        viewModelScope.launch(Dispatchers.IO) {
            val centers = repository.getCenters()

            withContext(Dispatchers.Main) {
                _centerList.value = centers
            }
        }
    }

    fun clickMarker(id: Int) {
        if (selectedMarkerInfo.value?.id == id && isVisibleWindow.value == true) {
            hideInfoWindow()
        } else {
            showInfoWindow(id)
        }
    }

    private fun showInfoWindow(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val center = repository.getCenter(id)

            withContext(Dispatchers.Main) {
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

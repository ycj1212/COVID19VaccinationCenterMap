package com.ycj1212.covid19vaccinationcentermap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycj1212.covid19vaccinationcentermap.data.Center
import com.ycj1212.covid19vaccinationcentermap.data.CenterRepository
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val centers = repository.getCenters()

            withContext(Dispatchers.Main) {
                _centerList.value = centers
            }
        }
    }
}

package com.ycj1212.covid19vaccinationcentermap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycj1212.covid19vaccinationcentermap.data.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: CenterRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            loadAndSave()
        }
    }

    private suspend fun loadAndSave() {
        (1..10).forEach { page ->
            val result = repository.getCenterList(page)
            repository.saveCenterList(result)
        }
    }
}

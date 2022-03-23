package com.ycj1212.covid19vaccinationcentermap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycj1212.covid19vaccinationcentermap.data.CenterRepository
import kotlinx.coroutines.launch

class SplashViewModel (
    private val repository: CenterRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            (1..10).forEach { page ->
                repository.getCenterList(page)
            }
        }
    }
}

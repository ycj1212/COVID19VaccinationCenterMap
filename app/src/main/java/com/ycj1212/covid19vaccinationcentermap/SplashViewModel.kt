package com.ycj1212.covid19vaccinationcentermap

import android.os.SystemClock
import androidx.lifecycle.*
import com.ycj1212.covid19vaccinationcentermap.data.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: CenterRepository
) : ViewModel() {
    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _isProgressDone = MutableLiveData(false)
    val isProgressDone: LiveData<Boolean> = _isProgressDone

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val loadAndSaveJob = launch {
                loadAndSave()
            }

            process1(loadAndSaveJob)
        }
    }

    private suspend fun loadAndSave() {
        (1..10).forEach { page ->
            val result = repository.getCenterList(page)
            repository.saveCenterList(result)
        }
    }

    /**
     * 2초에 걸쳐 [_progress] 값(1~100)을 차례대로 변경한다.
     * [job]이 완료되지 않았다면 완료될 때까지 80에서 대기한다.
     * 그 이후의 작업은 [process2]에서 진행한다.
     *
     * @param job API를 통해 1페이지(page)에 10개(perPage) 씩 순서대로 10개 페이지 호출(총 100개)하여 데이터 저장
     */
    private suspend fun process1(job: Job) {
        val startTime = SystemClock.elapsedRealtime()
        var currTime = SystemClock.elapsedRealtime()

        while (currTime < startTime + 2000) {
            val currProgress = (currTime - startTime).toInt() / 20 + 1

            if (currProgress >= 80 && job.isActive) {
                // 저장이 완료될 때까지 80%에서 대기
                job.join()

                // 0.7초에 걸쳐 100%로 만듬
                process2()
                break
            }

            withContext(Dispatchers.Main) {
                _progress.value = currProgress
            }

            currTime = SystemClock.elapsedRealtime()
        }

        withContext(Dispatchers.Main) {
            _isProgressDone.value = true
        }
    }

    /**
     * 0.7초에 걸쳐 [_progress] 값(80~100)을 차례대로 변경한다.
     */
    private suspend fun process2() {
        val startTime = SystemClock.elapsedRealtime()
        var currTime = SystemClock.elapsedRealtime()

        while (currTime < startTime + 700) {
            val currProgress2 = ((currTime - startTime).toDouble() / 3.5 + 1).toInt()

            withContext(Dispatchers.Main) {
                _progress.value = 80 + currProgress2
            }

            currTime = SystemClock.elapsedRealtime()
        }
    }
}

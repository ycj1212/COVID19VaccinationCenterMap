package com.ycj1212.covid19vaccinationcentermap

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ycj1212.covid19vaccinationcentermap.databinding.ActivitySplashBinding
import com.ycj1212.covid19vaccinationcentermap.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
        binding.viewmodel = splashViewModel

        handleNetworkState()

        lifecycleScope.launch {
            splashViewModel.isProgressDone.collect() { isProgressDone ->
                if (isProgressDone) {
                    navigateToMapUi()
                }
            }
        }
    }

    /**
     * 네트워크 연결 상태에 따라 진행 여부를 결정합니다.
     * 네트워크에 연결되어 있는 경우 API 데이터를 호출합니다.
     * 연결되어 있지 않은 경우 토스트 메시지와 함께 앱을 종료합니다.
     */
    private fun handleNetworkState() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (cm.activeNetwork == null) {
                Toast.makeText(this, "네트워크 연결 상태를 확인해주세요.", Toast.LENGTH_LONG).show()
                finish()
            }
            cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    splashViewModel.start()
                }

                override fun onLost(network: Network) {
                    runOnUiThread {
                        Toast.makeText(
                            this@SplashActivity,
                            "네트워크 연결 상태를 확인해주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    finish()
                }
            })
        } else {
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork?.isConnectedOrConnecting == true
            if (!isConnected) {
                Toast.makeText(this, "네트워크 연결 상태를 확인해주세요.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                splashViewModel.start()
            }
        }
    }

    private fun navigateToMapUi() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }
}

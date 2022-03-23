package com.ycj1212.covid19vaccinationcentermap

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ycj1212.covid19vaccinationcentermap.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.viewmodel = splashViewModel
        binding.lifecycleOwner = this

        splashViewModel.isProgressDone.observe(this) { isProgressDone ->
            if (isProgressDone) {
                navigateToMapUi()
            }
        }
    }

    private fun navigateToMapUi() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }
}

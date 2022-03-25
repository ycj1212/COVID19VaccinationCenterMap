package com.ycj1212.covid19vaccinationcentermap

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ycj1212.covid19vaccinationcentermap.databinding.ActivitySplashBinding
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

        lifecycleScope.launch {
            splashViewModel.isProgressDone.collect() { isProgressDone ->
                if (isProgressDone) {
                    navigateToMapUi()
                }
            }
        }
    }

    private fun navigateToMapUi() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }
}

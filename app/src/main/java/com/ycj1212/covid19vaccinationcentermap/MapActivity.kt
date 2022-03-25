package com.ycj1212.covid19vaccinationcentermap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.ycj1212.covid19vaccinationcentermap.databinding.ActivityMapBinding
import com.ycj1212.covid19vaccinationcentermap.viewmodels.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationManager: LocationManager

    private val mapViewModel: MapViewModel by viewModels()

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location == null) {
                requestCurrentLocation()
                return@registerForActivityResult
            }

            naverMap.moveCamera(
                CameraUpdate.scrollAndZoomTo(LatLng(location), 15.0)
                    .animate(CameraAnimation.Linear)
            )
        } else {
            Toast.makeText(this, "현재 위치를 확인하려면 권한 수락이 필요합니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.lifecycleOwner = this
        binding.viewmodel = mapViewModel

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        (supportFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }).getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        naverMap.setOnMapClickListener { _, _ ->
            mapViewModel.hideInfoWindow()
        }

        binding.btnMyLocation.setOnClickListener {
            requestCurrentLocation()
        }

        lifecycleScope.launch {
            mapViewModel.centerList.collect { centerList ->
                centerList.forEach { center ->
                    Marker().apply {
                        position = LatLng(center.lat.toDouble(), center.lng.toDouble())
                        captionText = center.centerName
                        map = naverMap
                        icon = if (center.centerType == "지역") MarkerIcons.GREEN else MarkerIcons.RED
                        setOnClickListener {
                            mapViewModel.clickMarker(center.id)
                            naverMap.setContentPadding(0, 0, 0, binding.infoWindow.height)
                            naverMap.moveCamera(
                                CameraUpdate.scrollAndZoomTo(position, 15.0)
                                    .animate(CameraAnimation.Linear)
                            )
                            true
                        }
                    }
                }
            }
        }
    }

    private fun requestCurrentLocation() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onBackPressed() {
        if (mapViewModel.isVisibleWindow.value) {
            mapViewModel.hideInfoWindow()
        } else {
            super.onBackPressed()
        }
    }
}

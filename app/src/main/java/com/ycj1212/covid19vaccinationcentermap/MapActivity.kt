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
        // 권한 요청을 수락한 경우
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
        }
        // 권한 요청을 거절한 경우
        else {
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

        // 마커가 아닌 지도를 클릭한 경우 정보안내창을 숨깁니다.
        naverMap.setOnMapClickListener { _, _ ->
            mapViewModel.hideInfoWindow()
        }

        // 내 위치 버튼을 클릭한 경우 현재 내 위치로 이동합니다.
        binding.btnMyLocation.setOnClickListener {
            requestCurrentLocation()
        }

        lifecycleScope.launch {
            // DB에서 예방접종센터 목록을 읽어 각 센터에 해당하는 마커를 생성합니다.
            mapViewModel.centerList.collect { centerList ->
                centerList.forEach { center ->
                    Marker().apply {
                        position = LatLng(center.lat.toDouble(), center.lng.toDouble())
                        captionText = center.centerName
                        map = naverMap
                        // 만약 센터 유형이 "지역"인 경우 초록색, "중앙/권역"인 경우 빨간색으로 마커 색상을 설정합니다.
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

    /**
     * 이 함수는 위치 권한을 요청하는 [locationPermissionRequest]를 수행합니다.
     * 내 위치 버튼을 클릭한 경우에만 호출됩니다.
     */
    private fun requestCurrentLocation() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * 뒤로가기 버튼을 클릭했을 때 정보안내창이 보이는 경우 정보안내창을 숨깁니다.
     */
    override fun onBackPressed() {
        if (mapViewModel.isVisibleWindow.value) {
            mapViewModel.hideInfoWindow()
        } else {
            super.onBackPressed()
        }
    }
}

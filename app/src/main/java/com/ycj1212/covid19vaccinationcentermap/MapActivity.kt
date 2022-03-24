package com.ycj1212.covid19vaccinationcentermap

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.ycj1212.covid19vaccinationcentermap.databinding.ActivityMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var naverMap: NaverMap
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.lifecycleOwner = this

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        naverMap.setOnMapClickListener { _, _ ->
//            TODO("선택된 마커 없음으로 처리")
        }

        mapViewModel.getCenters()
        mapViewModel.centerList.observe(this) { centerList ->
            centerList.forEach { center ->
                Marker().apply {
                    position = LatLng(center.lat.toDouble(), center.lng.toDouble())
                    captionText = center.centerName
                    map = naverMap
                    setOnClickListener {
//                        TODO("화면 중앙으로 카메라 이동")
//                        TODO("정보 안내창에 표시")
//                        TODO("선택 상태 반영")
                        true
                    }
//                    TODO("centerType에 따라 마커 색상 구분")
                }
            }
        }
    }
}

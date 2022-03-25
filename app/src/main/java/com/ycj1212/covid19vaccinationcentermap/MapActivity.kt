package com.ycj1212.covid19vaccinationcentermap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
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

        mapViewModel.getCenters()
        mapViewModel.centerList.observe(this) { centerList ->
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

        mapViewModel.selectedMarkerInfo.observe(this) { infoWindowUiState ->
            binding.tvCenterName.text = infoWindowUiState.centerName
            binding.tvFacilityName.text = infoWindowUiState.facilityName
            binding.tvAddress.text = infoWindowUiState.address
            binding.tvPhoneNumber.text = infoWindowUiState.phoneNumber
            binding.tvUpdatedAt.text = infoWindowUiState.updatedAt
        }

        mapViewModel.isVisibleWindow.observe(this) { isVisible ->
            binding.infoWindow.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }
}

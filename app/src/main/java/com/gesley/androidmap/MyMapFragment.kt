package com.gesley.androidmap

import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class MyMapFragment : SupportMapFragment() {

    private var googleMap : GoogleMap? = null

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MapsViewModel::class.java)
    }

    override fun getMapAsync(callback: OnMapReadyCallback?) {
        super.getMapAsync {
            googleMap = it
            setUpMap()
            callback?.onMapReady(googleMap)
        }
    }

    private fun setUpMap() {
        googleMap?.run {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isMapToolbarEnabled = false
            uiSettings.isZoomControlsEnabled = true
        }

        viewModel.getMapState().observe(
            this, { mapState ->
                if (mapState != null) {
                    updateMap(mapState)
                }
            })
    }

    private fun updateMap(mapState: MapsViewModel.MapState) {
        googleMap?.run {
            clear()
            val origin = mapState.origin
            if (origin != null) {
                addMarker(
                    MarkerOptions().position(origin).title("Local atual")
                )
                animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 17.0f))
            }
        }
    }
}
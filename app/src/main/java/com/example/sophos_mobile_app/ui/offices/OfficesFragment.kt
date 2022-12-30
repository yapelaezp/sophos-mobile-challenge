package com.example.sophos_mobile_app.ui.offices

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sophos_mobile_app.databinding.FragmentOfficesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfficesFragment : Fragment() {

    private var _binding: FragmentOfficesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mMapView = binding.mvOffices
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
        setComponents()
        return root
    }

    private fun setComponents() {
        mMapView.getMapAsync{googleMap ->
            val sydney = LatLng(-34.0, 151.0)
            googleMap.addMarker(
                MarkerOptions().position(sydney).title("Marker Title")
                    .snippet("Marker Description")
            )

            // For zooming automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(15f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}
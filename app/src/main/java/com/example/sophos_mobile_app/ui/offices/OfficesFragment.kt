package com.example.sophos_mobile_app.ui.offices

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.databinding.FragmentOfficesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfficesFragment : Fragment() {

    private var _binding: FragmentOfficesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private val officesViewModel: OfficesViewModel by viewModels()
    private var offices: List<Office>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mapView = binding.mvOffices
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        setComponents()
        setListeners()
        setMapView()
        observeViewModel()
        return root
    }

    private fun observeViewModel() {
        officesViewModel.offices.observe(viewLifecycleOwner){ offices ->
            this.offices = offices
            setMapView()
        }
    }

    private fun setListeners() {
        binding.toolbarOfficesScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack(R.id.menuFragmentDestination, false)
        }
    }

    private fun setComponents() {
        //Set Toolbar
        binding.toolbarOfficesScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
        //Load cities
        officesViewModel.getOffices()
    }

    @SuppressLint("MissingPermission") // Permissions are granted upon create this fragment
    private fun setMapView() {
        //Set Mapview
        mapView.getMapAsync{ googleMap ->
            googleMap.isMyLocationEnabled = true
            // For zooming automatically to the location of the marker
/*            val cameraPosition = CameraPosition.Builder().target().zoom(15f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))*/
            this.offices?.forEach { office ->
                try {
                    println(office)
                    googleMap.addMarker(MarkerOptions()
                        .position(LatLng(office.latitude.toDouble(),office.longitude.toDouble()))
                        .title(office.name)
                    )
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}
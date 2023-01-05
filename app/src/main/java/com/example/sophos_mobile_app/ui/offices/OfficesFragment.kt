package com.example.sophos_mobile_app.ui.offices

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.MainActivity
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.databinding.FragmentOfficesBinding
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.utils.AppLanguage
import com.example.sophos_mobile_app.utils.UserDataStore
import com.example.sophos_mobile_app.utils.dataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class OfficesFragment : Fragment() {

    private var _binding: FragmentOfficesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private val officesViewModel: OfficesViewModel by viewModels()
    private var offices: List<Office>? = null
    private val appLanguage = AppLanguage()
    private lateinit var userDataStore: UserDataStore
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(-33.8523341, 151.2106085) // TODO ("Put Medellin as Default")
    private var locationPermissionGranted: Boolean? = null
    private val args: OfficesFragmentArgs by navArgs()
    private var lastKnownLocation: Location? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mapView = binding.mvOffices
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        observeViewModel()
        setListeners()
        setComponents()
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
            findNavController().popBackStack()
        }
        binding.toolbarOfficesScreen.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_language -> {
                    lifecycleScope.launch { appLanguage.changeLanguage() }
                    true
                }
                R.id.action_main_menu -> {
                    findNavController().popBackStack(R.id.menuFragmentDestination, false)
                    true
                }
                R.id.action_send_docs -> {
                    navigateToSendDocs()
                    true
                }
                R.id.action_see_docs -> {
                    navigateToSeeDocs()
                    true
                }
                R.id.action_logout -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        logout()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToSeeDocs() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStore.getDataStorePreferences().collect{ userPreferences ->
                withContext(Dispatchers.Main){
                    val action = OfficesFragmentDirections.actionOfficesFragmentDestinationToViewDocumentsFragmentDestination(userPreferences.email)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun navigateToSendDocs() {
        val action = OfficesFragmentDirections.actionOfficesFragmentDestinationToSendDocumentsFragmentDestination()
        findNavController().navigate(action)
    }

    private fun setComponents() {
        //Get user datastore instance
        userDataStore = UserDataStore(requireContext())
        //Get permision value
        locationPermissionGranted = args.hasLocationPermission
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())        //Set Toolbar
        binding.toolbarOfficesScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
        //Load cities
        officesViewModel.getOffices()
        //Set toolbar
        binding.toolbarOfficesScreen.menu.findItem(R.id.action_offices).isVisible = false
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                binding.toolbarOfficesScreen.menu.findItem(R.id.action_language).title = "Español"
            } else{
                binding.toolbarOfficesScreen.menu.findItem(R.id.action_language).title = "English"
            }
        }
    }

    private suspend fun logout() {
        withContext(Dispatchers.IO) {
            requireContext().dataStore.edit { preferences ->
                preferences[stringPreferencesKey(LoginFragment.EMAIL)] = ""
                preferences[stringPreferencesKey(LoginFragment.PASSWORD)] = ""
                preferences[stringPreferencesKey(LoginFragment.NAME)] = ""
            }
        }
        withContext(Dispatchers.Main) {
            val action = OfficesFragmentDirections.actionOfficesFragmentDestinationToLoginFragmentDestination()
            findNavController().navigate(action)
        }
    }

    private fun setMapView() {
        //Set Mapview
        mapView.getMapAsync{ googleMap ->
            // For zooming automatically to the location of the marker
/*            val cameraPosition = CameraPosition.Builder().target().zoom(15f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))*/
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI(googleMap)

            // Get the current location of the device and set the position of the map.
            getDeviceLocation(googleMap)
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

    @SuppressLint("MissingPermission") // Permissions are granted upon create this fragment
    private fun updateLocationUI(map: GoogleMap) {
        try {
            if (locationPermissionGranted == true) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(map: GoogleMap) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted == true) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}
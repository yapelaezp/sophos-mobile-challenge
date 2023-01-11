package com.example.sophos_mobile_app.ui.offices

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.MainActivity
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.databinding.BackgroundPopupMenuBinding
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
    private lateinit var userDataStore: UserDataStore
    private val appLanguage = AppLanguage()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(-33.8523341, 151.2106085) // TODO ("Put Medellin as Default")
    private var locationPermissionGranted: Boolean? = null
    private val args: OfficesFragmentArgs by navArgs()
    private var lastKnownLocation: Location? = null
    private val popupBinding by lazy { BackgroundPopupMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow

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
        officesViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResponseStatus.Error -> {
                    binding.pbOffices.visibility = View.GONE
                    showErrorDialog(status.messageId)
                }
                is ResponseStatus.Loading -> binding.pbOffices.visibility = View.VISIBLE
                is ResponseStatus.Success -> binding.pbOffices.visibility = View.GONE
            }
        }
    }

    private fun setListeners() {
        binding.toolbarOfficesScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
        }
        popupBinding.actionSendDocs.setOnClickListener {
            navigateToSendDocs()
            popupWindow.dismiss()
        }
        popupBinding.actionSeeDocs.setOnClickListener {
            navigateToSeeDocs()
            popupWindow.dismiss()
        }
        popupBinding.actionMode.setOnClickListener {
            setAppMode()
            popupWindow.dismiss()
        }
        popupBinding.actionLanguage.setOnClickListener {
            lifecycleScope.launch { appLanguage.changeLanguage() }
            popupWindow.dismiss()
        }
        popupBinding.actionLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) { logout() }
            popupWindow.dismiss()
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
        findNavController().navigate(R.id.sendDocumentsFragmentDestination)
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
        popupBinding.actionOffices.visibility = View.GONE
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                popupBinding.actionLanguage.text = "Español"
            } else{
                popupBinding.actionLanguage.text = "English"
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStore.getDataStorePreferences().collect { userPreferences ->
                println(userPreferences.darkMode)
                if (!userPreferences.darkMode){
                    withContext(Dispatchers.Main){
                        popupBinding.actionMode.text = getString(R.string.night_mode)
                    }
                } else{
                    withContext(Dispatchers.Main){
                        popupBinding.actionMode.text = getString(R.string.day_mode)
                    }
                }
            }
        }
        binding.ivMenuScreenOverflowIcon.setOnClickListener {
            showPopupWindow(it)
        }
        popupWindow = PopupWindow(popupBinding.root,
            ListPopupWindow.WRAP_CONTENT,
            ListPopupWindow.WRAP_CONTENT
        )
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
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.menuFragmentDestination, true).build()
            findNavController().navigate(R.id.loginFragmentDestination, null, navOptions = navOptions)
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

    private fun setAppMode() {
        lifecycleScope.launch(Dispatchers.IO){
            userDataStore.getDataStorePreferences().collect{ userPreferences ->
                val appCompatActivity = activity as AppCompatActivity
                if (!userPreferences.darkMode){
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        appCompatActivity.delegate.applyDayNight()
                        popupBinding.actionMode.text = getString(R.string.day_mode)
                    }
                    userDataStore.saveModePreference(darkMode = true)
                } else {
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        popupBinding.actionMode.text = getString(R.string.night_mode)
                        appCompatActivity.delegate.applyDayNight()
                    }
                    userDataStore.saveModePreference(darkMode = false)
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

    private fun showErrorDialog(messageId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_message)
            .setMessage(messageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /** Dissmiss dialog **/ }
            .create()
            .show()
    }

    private fun showPopupWindow(anchor: View){
        if (popupWindow.isShowing){
            popupWindow.dismiss()
        } else {
            popupWindow.apply {
                isOutsideTouchable = true
            }
            popupWindow.showAsDropDown(anchor)
        }
    }
}
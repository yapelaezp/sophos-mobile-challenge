package com.example.sophos_mobile_app.ui.offices

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.databinding.FragmentOfficesBinding
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.ui.login.Logout
import com.example.sophos_mobile_app.ui.menu.MenuFragmentDirections
import com.example.sophos_mobile_app.utils.AppLanguage
import com.example.sophos_mobile_app.utils.UserDataStore
import com.example.sophos_mobile_app.utils.dataStore
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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
        val action = OfficesFragmentDirections.actionOfficesFragmentDestinationToSendDocumentsFragmentDestination(null)
        findNavController().navigate(action)
    }

    private fun setComponents() {
        //Get user datastore instance
        userDataStore = UserDataStore(requireContext())
        //Set Toolbar
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
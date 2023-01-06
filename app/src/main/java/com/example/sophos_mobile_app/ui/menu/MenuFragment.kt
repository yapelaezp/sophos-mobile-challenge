package com.example.sophos_mobile_app.ui.menu

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentMenuBinding
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.utils.AppLanguage
import com.example.sophos_mobile_app.utils.UserDataStore
import com.example.sophos_mobile_app.utils.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private val args: MenuFragmentArgs by navArgs()

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val appLanguage = AppLanguage()
    private lateinit var userDataStore: UserDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        userDataStore = UserDataStore(requireContext())
        setComponents()
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.btnMenuScreenSendDocs.setOnClickListener {
            findNavController().navigate(R.id.sendDocumentsFragmentDestination)
        }
        binding.btnMenuScreenSeeDocs.setOnClickListener {
            navigateToSeeDocs()
        }
        binding.btnMenuScreenOffices.setOnClickListener {
            navigateToOffices()
        }
        binding.toolbarMenuScreen.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        logout()
                    }
                    true
                }
                R.id.action_language -> {
                    lifecycleScope.launch { appLanguage.changeLanguage() }
                    true
                }
                R.id.action_mode -> {
                    setAppMode()
                    true
                }
                R.id.action_see_docs -> {
                    navigateToSeeDocs()
                    true
                }
                R.id.action_offices -> {
                    navigateToOffices()
                    true
                }
                R.id.action_send_docs -> {
                    navigateToSendDocs()
                    true
                }
                else -> false
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
                        binding.toolbarMenuScreen.menu.findItem(R.id.action_mode).title = getString(R.string.day_mode)
                    }
                    userDataStore.saveModePreference(darkMode = true)
                } else {
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        binding.toolbarMenuScreen.menu.findItem(R.id.action_mode).title = getString(R.string.night_mode)
                        appCompatActivity.delegate.applyDayNight()
                    }
                    userDataStore.saveModePreference(darkMode = false)
                }
            }
        }
    }

    private fun navigateToSeeDocs() {
        val action =
            MenuFragmentDirections.actionMenuFragmentDestinationToViewDocumentsFragmentDestination(
                args.userEmail
            )
        findNavController().navigate(action)
    }

    private fun navigateToSendDocs() {
        findNavController().navigate(R.id.sendDocumentsFragmentDestination)
    }

    private fun navigateToOffices() {
        val action =
            MenuFragmentDirections.actionMenuFragmentDestinationToPermissionsFragment(Manifest.permission.ACCESS_COARSE_LOCATION)
        findNavController().navigate(action)
    }

    private fun setComponents() {
        //Set toolbar
        binding.toolbarMenuScreen.title = args.userName
        binding.toolbarMenuScreen.menu.findItem(R.id.action_main_menu).isVisible = false
        binding.toolbarMenuScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                binding.toolbarMenuScreen.menu.findItem(R.id.action_language).title = "Español"
            } else{
                binding.toolbarMenuScreen.menu.findItem(R.id.action_language).title = "English"
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStore.getDataStorePreferences().collect { userPreferences ->
                println(userPreferences.darkMode)
                if (!userPreferences.darkMode){
                    withContext(Dispatchers.Main){
                        binding.toolbarMenuScreen.menu.findItem(R.id.action_mode).title = getString(R.string.night_mode)
                    }
                } else{
                    withContext(Dispatchers.Main){
                        binding.toolbarMenuScreen.menu.findItem(R.id.action_mode).title = getString(R.string.day_mode)
                    }
                }
            }
        }
    }

    private suspend fun logout() {
        withContext(Dispatchers.IO){
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

}
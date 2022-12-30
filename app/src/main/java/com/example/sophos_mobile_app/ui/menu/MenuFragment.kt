package com.example.sophos_mobile_app.ui.menu

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentMenuBinding
import com.example.sophos_mobile_app.ui.login.LoginFragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        setComponents()
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.btnMenuScreenSendDocs.setOnClickListener {
            val action = MenuFragmentDirections.actionToSendDocumentsFragmentDestination(null)
            findNavController().navigate(action)
        }
        binding.btnMenuScreenSeeDocs.setOnClickListener {
            val action =
                MenuFragmentDirections.actionMenuFragmentDestinationToViewDocumentsFragmentDestination(
                    args.userEmail
                )
            findNavController().navigate(action)
        }
        binding.btnMenuScreenOffices.setOnClickListener {
            val action =
                MenuFragmentDirections.actionMenuFragmentDestinationToPermissionsFragment(Manifest.permission.ACCESS_COARSE_LOCATION)
            findNavController().navigate(action)
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
                    println("Action language")
                    true
                }
                R.id.action_mode -> {
                    println("Action mode")
                    true
                }
                R.id.action_see_docs -> {
                    println("See docs")
                    true
                }
                R.id.action_offices -> {
                    println("Action offices")
                    true
                }
                else -> false
            }
        }
    }

    private fun setComponents() {
        //Set toolbar
        binding.toolbarMenuScreen.title = args.userName
        binding.toolbarMenuScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
    }

    private suspend fun logout() {
        context?.dataStore?.edit { preferences ->
            println("UP in menu fragment $preferences")
            preferences[stringPreferencesKey(LoginFragment.EMAIL)] = ""
            preferences[stringPreferencesKey(LoginFragment.PASSWORD)] = ""
            preferences[stringPreferencesKey(LoginFragment.NAME)] = ""
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), "Data cleared successfully", Toast.LENGTH_SHORT).show()
            val action =
                MenuFragmentDirections.actionMenuFragmentDestinationToLoginFragmentDestination()
            findNavController().navigate(action)
        }
    }

}
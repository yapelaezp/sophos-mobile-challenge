package com.example.sophos_mobile_app.ui.documents

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentSeeDocumentsBinding
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.utils.AppLanguage
import com.example.sophos_mobile_app.utils.UserDataStore
import com.example.sophos_mobile_app.utils.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SeeDocumentsFragment : Fragment() {

    private val args: SeeDocumentsFragmentArgs by navArgs()

    private var _binding: FragmentSeeDocumentsBinding? = null
    private val binding get() = _binding!!
    private val appLanguage = AppLanguage()
    private lateinit var userDataStore: UserDataStore

    private val seeDocumentsViewModel: SeeDocumentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeDocumentsBinding.inflate(inflater, container, false)
        userDataStore = UserDataStore(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
 /*       binding.toolbarViewDocumentsScreen.setOnMenuItemClickListener { menuItem ->
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
                R.id.action_offices -> {
                    navigateToOffices()
                    true
                }
                R.id.action_logout -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        logout()
                    }
                    true
                }
                R.id.action_mode -> {
                    setAppMode()
                    true
                }
                else -> false
            }
        }*/
        binding.toolbarViewDocumentsScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        seeDocumentsViewModel.documents.observe(viewLifecycleOwner){ documents ->
            binding.rvViewDocuments.adapter = DocumentDetailAdapter(documents){ registerId ->
                seeDocumentsViewModel.getDocumentDetail(registerId)
            }
        }
        seeDocumentsViewModel.imageBitmap.observe(viewLifecycleOwner){ imageBitmap ->
            binding.ivViwDocumentsAttached.setImageBitmap(imageBitmap)
        }
    }

    private fun setComponents() {
        //Get document list
        seeDocumentsViewModel.getDocuments(args.email)
        //Set Toolbar
/*        binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_see_docs).isVisible = false
        binding.toolbarViewDocumentsScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_language).title = "Español"
            } else{
                binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_language).title = "English"
            }
        }*/
        //Set Recycler View
        binding.rvViewDocuments.adapter = DocumentDetailAdapter(emptyList()){}
    }

    private fun setAppMode() {
        lifecycleScope.launch(Dispatchers.IO){
            userDataStore.getDataStorePreferences().collect{ userPreferences ->
                val appCompatActivity = activity as AppCompatActivity
                if (!userPreferences.darkMode){
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        appCompatActivity.delegate.applyDayNight()
                        binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_mode).title = getString(R.string.day_mode)
                    }
                    userDataStore.saveModePreference(darkMode = true)
                } else {
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_mode).title = getString(R.string.night_mode)
                        appCompatActivity.delegate.applyDayNight()
                    }
                    userDataStore.saveModePreference(darkMode = false)
                }
            }
        }
    }

    private fun navigateToSendDocs() {
        findNavController().navigate(R.id.sendDocumentsFragmentDestination)
    }

    private fun navigateToOffices() {
        val action = SeeDocumentsFragmentDirections.actionViewDocumentsFragmentDestinationToPermissionsFragment(Manifest.permission.ACCESS_COARSE_LOCATION)
        findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
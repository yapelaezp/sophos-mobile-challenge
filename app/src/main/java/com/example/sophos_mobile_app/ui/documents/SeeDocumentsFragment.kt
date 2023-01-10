package com.example.sophos_mobile_app.ui.documents

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.PopupWindow
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
import com.example.sophos_mobile_app.databinding.BackgroundPopupMenuBinding
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
    private val popupBinding by lazy { BackgroundPopupMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow
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
        binding.toolbarViewDocumentsScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
        }
        popupBinding.actionSendDocs.setOnClickListener {
            navigateToSendDocs()
            popupWindow.dismiss()
        }
        popupBinding.actionOffices.setOnClickListener {
            navigateToOffices()
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
        popupBinding.actionSeeDocs.visibility = View.GONE
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                popupBinding.actionLanguage.text = "Español"
            } else{
                popupBinding.actionLanguage.text = "English"
            }
        }
        binding.ivSeeDocsScreenOverflowIcon.setOnClickListener {
            showPopupWindow(it)
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
        popupWindow = PopupWindow(popupBinding.root,
            ListPopupWindow.WRAP_CONTENT,
            ListPopupWindow.WRAP_CONTENT
        )
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
                        popupBinding.actionSeeDocs.text = getString(R.string.day_mode)
                    }
                    userDataStore.saveModePreference(darkMode = true)
                } else {
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        appCompatActivity.delegate.applyDayNight()
                        popupBinding.actionSeeDocs.text = getString(R.string.night_mode)
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
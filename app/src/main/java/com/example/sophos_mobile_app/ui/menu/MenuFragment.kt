package com.example.sophos_mobile_app.ui.menu

import android.Manifest
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.RoomDatabase
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.SophosMobileApp
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase_Impl
import com.example.sophos_mobile_app.databinding.BackgroundPopupMenuBinding
import com.example.sophos_mobile_app.databinding.FragmentMenuBinding
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.utils.AppLanguage
import com.example.sophos_mobile_app.utils.DATABASE_NAME
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
    private val popupBinding by lazy { BackgroundPopupMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow
    private lateinit var userDataStore: UserDataStore
    private lateinit var db: SophosAppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        userDataStore = UserDataStore(requireContext())
        setComponents()
        setListeners()
        println("POKEEEEEEEEEEEEEEMON MM")

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
        popupBinding.actionSendDocs.setOnClickListener {
            navigateToSendDocs()
            popupWindow.dismiss()
        }
        popupBinding.actionSeeDocs.setOnClickListener {
            navigateToSeeDocs()
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
            logout()
            popupWindow.dismiss()
        }
    }

    private fun setAppMode() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStore.getDataStorePreferences().collect { userPreferences ->
                val appCompatActivity = activity as AppCompatActivity
                if (!userPreferences.darkMode) {
                    withContext(Dispatchers.Main) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        appCompatActivity.delegate.applyDayNight()
                        popupBinding.actionMode.text = getString(R.string.day_mode)
                    }
                    userDataStore.saveModePreference(darkMode = true)
                } else {
                    withContext(Dispatchers.Main) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        popupBinding.actionMode.text = getString(R.string.night_mode)
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
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()) {
                popupBinding.actionLanguage.text = "Español"
            } else {
                popupBinding.actionLanguage.text = "English"
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStore.getDataStorePreferences().collect { userPreferences ->
                println(userPreferences.darkMode)
                if (!userPreferences.darkMode) {
                    withContext(Dispatchers.Main) {
                        popupBinding.actionMode.text = getString(R.string.night_mode)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        popupBinding.actionMode.text = getString(R.string.day_mode)
                    }
                }
            }
        }
        popupWindow = PopupWindow(popupBinding.root, WRAP_CONTENT, WRAP_CONTENT)

        binding.ivMenuScreenOverflowIcon.setOnClickListener {
            showPopupWindow(it)
        }
    }

    private fun showPopupWindow(anchor: View) {
        if (popupWindow.isShowing) {
            popupWindow.apply {
                showAsDropDown(anchor)
                dismiss()
            }
        }
        popupWindow.apply {
            isOutsideTouchable = true
            showAsDropDown(anchor)
        }
    }

    private fun logout() {
        lifecycleScope.launch(Dispatchers.IO) {
            requireContext().dataStore.edit { preferences ->
                preferences[stringPreferencesKey(LoginFragment.EMAIL)] = ""
                preferences[stringPreferencesKey(LoginFragment.PASSWORD)] = ""
                preferences[stringPreferencesKey(LoginFragment.NAME)] = ""
            }
            SophosAppDatabase.getDatabase(requireContext()).clearAllTables()
        }
        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.menuFragmentDestination, true).build()
        findNavController().navigate(
            R.id.loginFragmentDestination,
            null,
            navOptions = navOptions
        )
    }

}
package com.example.sophos_mobile_app.ui.documents

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase_Impl
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.databinding.BackgroundPopupMenuBinding
import com.example.sophos_mobile_app.databinding.FragmentSendDocumentsBinding
import com.example.sophos_mobile_app.ui.camera.CameraViewModel
import com.example.sophos_mobile_app.ui.camera.GalleryViewModel
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SendDocumentsFragment : Fragment() {

    private var _binding: FragmentSendDocumentsBinding? = null
    private val binding get() = _binding!!
    private val sendDocumentViewModel: SendDocumentsViewModel by viewModels()
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private val cameraViewModel: CameraViewModel by activityViewModels()
    private val appLanguage = AppLanguage()
    private val popupBinding by lazy { BackgroundPopupMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow
    private var imageBase64: String? = null
    private lateinit var userDataStore: UserDataStore
    private lateinit var arrayAdapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendDocumentsBinding.inflate(inflater, container, false)
        userDataStore = UserDataStore(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        setListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        sendDocumentViewModel.cities.observe(viewLifecycleOwner) { offices ->
            val cities = offices.toMutableList()
            val cityTitle = getString(R.string.city)
            cities.add(0, cityTitle)
            arrayAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, cities)
            binding.spDocumentScreenCity.adapter = arrayAdapter
        }
        sendDocumentViewModel.statusPost.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResponseStatus.Error -> {
                    binding.pbSendDocs.visibility = View.GONE
                    showErrorDialog(status.messageId)
                }
                is ResponseStatus.Loading -> binding.pbSendDocs.visibility = View.VISIBLE
                is ResponseStatus.Success -> {
                    binding.pbSendDocs.visibility = View.GONE
                    showMessage(getString(R.string.add_new_doc_success))
                }
            }
        }
        galleryViewModel.imageBase64.observe(viewLifecycleOwner) { imageBase64 ->
            this.imageBase64 = imageBase64
        }
        cameraViewModel.imageBase64.observe(viewLifecycleOwner) { imageBase64 ->
            this.imageBase64 = imageBase64
        }
    }

    private fun setListeners() {
        binding.btnDocumentScreenSend.setOnClickListener {
            val docType = binding.spDocumentScreenIdType.selectedItem.toString()
            val docNumber = binding.etvDocumentScreenDocNumber.text.toString().trim()
            val names = binding.etvDocumentScreenNames.text.toString().trim()
            val lastnames = binding.etvDocumentScreenLastnames.text.toString().trim()
            val email = binding.etvDocumentScreenEmail.text.toString().trim()
            val city = binding.spDocumentScreenCity.selectedItem.toString()
            val attachedType = binding.etvDocumentScreenAttachedType.text.toString().trim()
            val areFieldsValid = validateFields(
                docType,
                docNumber,
                names,
                lastnames,
                email,
                city,
                attachedType,
                this.imageBase64
            )
            if (areFieldsValid.first) {
                sendDocumentViewModel.createNewDocument(
                    docType,
                    docNumber,
                    names,
                    lastnames,
                    city,
                    email,
                    attachedType,
                    imageBase64!!
                )
            } else {
                Toast.makeText(requireContext(), areFieldsValid.second, Toast.LENGTH_LONG).show()
            }
        }
        binding.btnDocumentScreenUploadDoc.setOnClickListener {
            val action =
                SendDocumentsFragmentDirections.actionSendDocumentsFragmentToPermissionsFragment(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            navigate(action)
        }
        binding.ivDocumentScreenAddPhoto.setOnClickListener {
            showSelectPhotoOptionDialog()
        }
        binding.toolbarDocumentScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
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

    private fun showSelectPhotoOptionDialog() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle(getString(R.string.select_picture_option))
        val pictureDialogItem = arrayOf(
            getString(R.string.select_from_gallery),
            getString(R.string.capture_photo_from_gallery)
        )
        pictureDialog.setItems(pictureDialogItem) { _, option ->
            when (option) {
                0 -> {
                    val action =
                        SendDocumentsFragmentDirections.actionSendDocumentsFragmentToPermissionsFragment(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    navigate(action)
                }
                1 -> {
                    val action =
                        SendDocumentsFragmentDirections.actionSendDocumentsFragmentToPermissionsFragment(
                            android.Manifest.permission.CAMERA
                        )
                    navigate(action)
                }
            }
        }
        pictureDialog.show()
    }

    private fun validateFields(
        docType: String?, docNumber: String?, names: String?, lastnames: String?,
        email: String?, city: String?, attachedType: String, imageBase64: String?
    ): Pair<Boolean, String> {
        if (Validation.isFieldEmpty(docType) || docType == getString(R.string.doc_type_title)) {
            return Pair(false, getString(R.string.id_type_requirement))
        }
        if (Validation.isFieldEmpty(docNumber)) {
            return Pair(false, getString(R.string.doc_number_requirement))
        }
        if (Validation.isFieldEmpty(names)) {
            return Pair(false, getString(R.string.name_requirement))
        }
        if (Validation.isFieldEmpty(lastnames)) {
            return Pair(false, getString(R.string.lastname_requirement))
        }
        if (Validation.isFieldEmpty(email)) {
            return Pair(false, getString(R.string.email_requirement))
        }
        if (Validation.isFieldEmpty(city) || city == getString(R.string.city)) {
            return Pair(false, getString(R.string.city_requirement))
        }
        if (Validation.isFieldEmpty(attachedType)) {
            return Pair(false, getString(R.string.attached_type_requirement))
        }
        if (Validation.isFieldEmpty(imageBase64)) {
            return Pair(false, getString(R.string.add_photo_requirement))
        }
        return Pair(true, "")
    }

    private fun setComponents() {
        // Set cities spinner
        sendDocumentViewModel.getOffices()
        //Set doc type spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.doc_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spDocumentScreenIdType.adapter = adapter
        }
        //Set email field
        lifecycleScope.launch(Dispatchers.IO){
            userDataStore.getDataStorePreferences().collect{ userPreferences ->
                withContext(Dispatchers.Main){
                    binding.etvDocumentScreenEmail.setText(userPreferences.email)
                }
            }
        }
        //Set Toolbar
        binding.toolbarDocumentScreen.title = getString(R.string.go_back)
        binding.toolbarDocumentScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
        }
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()) {
                popupBinding.actionLanguage.text = "Español"
            } else {
                popupBinding.actionLanguage.text = "English"
            }
        }
        binding.ivSendDocsScreenOverflowIcon.setOnClickListener {
            showPopupWindow(it)
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
        //Set toolbar popup
        popupBinding.actionSendDocs.visibility = View.GONE
        popupWindow = PopupWindow(
            popupBinding.root,
            ListPopupWindow.WRAP_CONTENT,
            ListPopupWindow.WRAP_CONTENT
        )
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToOffices() {
        val action =
            SendDocumentsFragmentDirections.actionSendDocumentsFragmentToPermissionsFragment(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        findNavController().navigate(action)
    }

    private fun navigateToSeeDocs() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStore.getDataStorePreferences().collect() { userPreferences ->
                withContext(Dispatchers.Main) {
                    println(userPreferences.email)
                    val action =
                        SendDocumentsFragmentDirections.actionSendDocumentsFragmentDestinationToViewDocumentsFragmentDestination(
                            userPreferences.email
                        )
                    findNavController().navigate(action)
                }
            }
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
        activity?.deleteDatabase(DATABASE_NAME)
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.menuFragmentDestination, true).build()
        findNavController().navigate(R.id.loginFragmentDestination, null, navOptions = navOptions)
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

    private fun showErrorDialog(messageId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_message)
            .setMessage(messageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /** Dissmiss dialog **/ }
            .create()
            .show()
    }

    private fun showPopupWindow(anchor: View) {
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        } else {
            popupWindow.apply {
                isOutsideTouchable = true
            }
            popupWindow.showAsDropDown(anchor)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
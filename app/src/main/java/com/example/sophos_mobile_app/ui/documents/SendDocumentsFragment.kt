package com.example.sophos_mobile_app.ui.documents

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentSendDocumentsBinding
import com.example.sophos_mobile_app.ui.camera.CameraViewModel
import com.example.sophos_mobile_app.ui.camera.GalleryViewModel
import com.example.sophos_mobile_app.ui.login.LoginFragment
import com.example.sophos_mobile_app.ui.menu.MenuFragmentDirections
import com.example.sophos_mobile_app.utils.AppLanguage
import com.example.sophos_mobile_app.utils.UserDataStore
import com.example.sophos_mobile_app.utils.Validation
import com.example.sophos_mobile_app.utils.dataStore
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
    private lateinit var userDataStore: UserDataStore
    private var imageBase64: String? = null


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
        val cities = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        sendDocumentViewModel.cities.observe(viewLifecycleOwner) { offices ->
            cities.addAll(offices)
            binding.spDocumentScreenCity.adapter = cities
        }
        sendDocumentViewModel.status.observe(viewLifecycleOwner){ postNewDocStatus ->
            if (postNewDocStatus){
                this.imageBase64 = null
                showMessage(getString(R.string.add_new_doc_success))
            } else {
                showMessage(getString(R.string.add_new_doc_failed))
            }
        }
        galleryViewModel.imageBase64.observe(viewLifecycleOwner){ imageBase64 ->
            this.imageBase64 = imageBase64
        }
        cameraViewModel.imageBase64.observe(viewLifecycleOwner){ imageBase64 ->
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
        binding.toolbarDocumentScreen.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_language -> {
                    lifecycleScope.launch { appLanguage.changeLanguage() }
                    true
                }
                R.id.action_main_menu -> {
                    findNavController().popBackStack(R.id.menuFragmentDestination, false)
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
        if (Validation.isFieldEmpty(docType)) {
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
        if (Validation.isFieldEmpty(city)) {
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
        // Load cities into city spinner
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

        //Set toolbar
        binding.toolbarDocumentScreen.title = getString(R.string.go_back)
        binding.toolbarDocumentScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
        binding.toolbarDocumentScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarDocumentScreen.menu.findItem(R.id.action_send_docs).isVisible = false
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()) {
                binding.toolbarDocumentScreen.menu.findItem(R.id.action_language).title = "Español"
            } else {
                binding.toolbarDocumentScreen.menu.findItem(R.id.action_language).title = "English"
            }
        }
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

    private suspend fun logout() {
        withContext(Dispatchers.IO) {
            requireContext().dataStore.edit { preferences ->
                preferences[stringPreferencesKey(LoginFragment.EMAIL)] = ""
                preferences[stringPreferencesKey(LoginFragment.PASSWORD)] = ""
                preferences[stringPreferencesKey(LoginFragment.NAME)] = ""
            }
        }
        withContext(Dispatchers.Main) {
            findNavController().navigate(R.id.loginFragmentDestination)
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
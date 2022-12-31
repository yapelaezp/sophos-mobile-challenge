package com.example.sophos_mobile_app.ui.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentSendDocumentsBinding
import com.example.sophos_mobile_app.utils.Validation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDocumentsFragment : Fragment() {

    private var _binding: FragmentSendDocumentsBinding? = null
    private val binding get() = _binding!!
    private val sendDocumentViewModel: SendDocumentsViewModel by viewModels()
    private val args: SendDocumentsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendDocumentsBinding.inflate(inflater, container, false)
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
    }

    private fun setListeners() {
        binding.btnDocumentScreenSend.setOnClickListener {
            val docType = binding.spDocumentScreenIdType.selectedItem.toString()
            val docNumber = binding.etvDocumentScreenDocNumber.text.toString()
            val names = binding.etvDocumentScreenNames.text.toString()
            val lastnames = binding.etvDocumentScreenLastnames.text.toString()
            val email = binding.etvDocumentScreenEmail.text.toString()
            val city = binding.spDocumentScreenCity.selectedItem.toString()
            val attachedType = binding.etvDocumentScreenAttachedType.text.toString()
            val photoBase64 = args.imageBase64
            val areFieldsValid = validateFields(docType, docNumber, names, lastnames, email, city, attachedType, photoBase64)
            if (areFieldsValid.first) {
                sendDocumentViewModel.createNewDocument(
                    docType,
                    docNumber,
                    names,
                    lastnames,
                    city,
                    email,
                    attachedType,
                    photoBase64!!
                )
            } else {
                Toast.makeText(requireContext(), areFieldsValid.second, Toast.LENGTH_LONG).show()
            }
        }
        binding.btnDocumentScreenUploadDoc.setOnClickListener {
            val action =
                SendDocumentsFragmentDirections.actionSendDocumentsFragmentToPermissionsFragment(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
            navigate(action)
        }
        binding.ivDocumentScreenAddPhoto.setOnClickListener {
            showSelectPhotoOptionDialog()
        }
        binding.toolbarDocumentScreen.getChildAt(1).setOnClickListener {
            findNavController().popBackStack()
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
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
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
        if (Validation.isFieldEmpty(docType)){
            return Pair(false, getString(R.string.id_type_requirement))
        }
        if (Validation.isFieldEmpty(docNumber)){
            return Pair(false, getString(R.string.doc_number_requirement))
        }
        if (Validation.isFieldEmpty(names)){
            return Pair(false, getString(R.string.name_requirement))
        }
        if (Validation.isFieldEmpty(lastnames)){
            return Pair(false, getString(R.string.lastname_requirement))
        }
        if (Validation.isFieldEmpty(email)){
            return Pair(false, getString(R.string.email_requirement))
        }
        if (Validation.isFieldEmpty(city)){
            return Pair(false, getString(R.string.city_requirement))
        }
        if (Validation.isFieldEmpty(attachedType)){
            return Pair(false, getString(R.string.attached_type_requirement))
        }
        if (Validation.isFieldEmpty(imageBase64)){
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
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
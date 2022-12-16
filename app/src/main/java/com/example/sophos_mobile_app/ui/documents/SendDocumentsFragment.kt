package com.example.sophos_mobile_app.ui.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentSendDocumentsBinding
import com.example.sophos_mobile_app.utils.Validation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendDocumentsFragment : Fragment() {

    private var _binding: FragmentSendDocumentsBinding? = null
    private val binding get() = _binding!!

    private val sendDocumentViewModel: SendDocumentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendDocumentsBinding.inflate(inflater, container, false)
        setComponents()
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.btnDocumentScreenSend.setOnClickListener {
            val docType = binding.spDocumentScreenDocType.selectedItem.toString()
            val docNumber = binding.etvDocumentScreenDocNumber.text.toString()
            val names = binding.etvDocumentScreenNames.text.toString()
            val lastnames = binding.etvDocumentScreenLastnames.text.toString()
            val email = binding.etvDocumentScreenEmail.text.toString()
            val city = "Tokyo"
            //val city = binding.spDocumentScreenCity.selectedItem.toString()
            if (validateFields(docType, docNumber, names, lastnames, email, city)) {
                sendDocumentViewModel.createNewDocument(
                    docType,
                    docNumber,
                    names,
                    lastnames,
                    city,
                    email,
                    attachedType = "Photo",
                    attached = "123"
                )
            }
        }
    }

    private fun validateFields(
        docType: String, docNumber: String, names: String,
        lastnames: String, email: String, city: String
    ): Boolean {
        if (Validation.isFieldEmpty(docType)) {
            return false
        }
        if (Validation.isFieldEmpty(docNumber)) {
            return false
        }
        if (Validation.isFieldEmpty(names)) {
            return false
        }
        if (Validation.isFieldEmpty(lastnames)) {
            return false
        }
        if (Validation.isFieldEmpty(email)) {
            return false
        }
        if (Validation.isFieldEmpty(city)) {
            return false
        }
        return true
    }

    private fun setComponents() {
        val spinner = binding.spDocumentScreenDocType
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.doc_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

}
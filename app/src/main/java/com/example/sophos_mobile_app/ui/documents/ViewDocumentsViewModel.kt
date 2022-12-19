package com.example.sophos_mobile_app.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewDocumentsViewModel @Inject constructor(private val documentRepository: DocumentRepository): ViewModel() {

    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>>
        get() = _documents

    fun getDocuments(email: String){
        viewModelScope.launch {
            _documents.value = documentRepository.getDocumentByUserEmail(email)
        }
    }
}
package com.example.sophos_mobile_app.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendDocumentsViewModel @Inject constructor(private val documentRepository: DocumentRepository) :
    ViewModel() {

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
        get() = _status

    fun createNewDocument(
        idType: String,
        identification: String,
        name: String,
        lastname: String,
        city: String,
        email: String,
        attachedType: String,
        attached: String
    ) {
        viewModelScope.launch {
            documentRepository.createNewDocument(
                idType, identification, name, lastname, city, email, attachedType, attached
            )
        }
    }
}
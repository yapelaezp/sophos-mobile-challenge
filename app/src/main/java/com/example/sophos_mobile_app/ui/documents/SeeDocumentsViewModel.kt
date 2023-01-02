package com.example.sophos_mobile_app.ui.documents

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail
import com.example.sophos_mobile_app.data.repository.DocumentRepository
import com.example.sophos_mobile_app.utils.ImageConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeeDocumentsViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val imageConverter: ImageConverter
    ): ViewModel() {

    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>>
        get() = _documents

    private val _imageBitmap = MutableLiveData<Bitmap>()
    val imageBitmap: LiveData<Bitmap>
        get() = _imageBitmap

    fun getDocuments(email: String){
        viewModelScope.launch {
            _documents.value = documentRepository.getDocumentByUserEmail(email)
        }
    }

    fun getDocumentDetail(registerId: String){
        viewModelScope.launch {
            val response = documentRepository.getDocumentDetail(registerId).firstOrNull()
            if (response != null){
                _imageBitmap.value = imageConverter.base64ToBitmap(response.attached)
            }
        }
    }
}
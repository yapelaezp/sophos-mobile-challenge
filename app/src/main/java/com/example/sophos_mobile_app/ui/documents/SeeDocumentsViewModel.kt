package com.example.sophos_mobile_app.ui.documents

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Document
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

    private val _documents = MutableLiveData<List<Document>?>()
    val documents: LiveData<List<Document>?>
        get() = _documents

    private val _imageBitmap = MutableLiveData<Bitmap>()
    val imageBitmap: LiveData<Bitmap>
        get() = _imageBitmap

    private val _status = MutableLiveData<ResponseStatus<Any>>()
    val status: LiveData<ResponseStatus<Any>> get() = _status

    private val _statusImage = MutableLiveData<ResponseStatus<Any>>()
    val statusImage: LiveData<ResponseStatus<Any>> get() = _statusImage

    fun getDocuments(email: String){
        _status.postValue(ResponseStatus.Loading())
        viewModelScope.launch {
            when(val response = documentRepository.getDocumentsByUserEmail(email)){
                 is ResponseStatus.Success -> {
                    _documents.postValue(response.data)
                    _status.postValue(ResponseStatus.Success(response.data))
                }
                is ResponseStatus.Error -> {
                    _status.postValue(ResponseStatus.Error(response.messageId))
                }
                else -> {}
            }
        }
    }

    fun getImageFromDocument(registerId: String){
        _statusImage.postValue(ResponseStatus.Loading())
        viewModelScope.launch {
            when(val response = documentRepository.getDocumentDetail(registerId)){
                is ResponseStatus.Success -> {
                    val imgAttached = response.data.firstOrNull()?.attached
                    if (imgAttached != null){
                        _imageBitmap.postValue(imageConverter.base64ToBitmap(imgAttached))
                        _statusImage.postValue(ResponseStatus.Success(response.data))
                    }
                }
                is ResponseStatus.Error -> {
                    _statusImage.postValue(ResponseStatus.Error(response.messageId))
                }
                else -> {}
            }
        }
    }
}
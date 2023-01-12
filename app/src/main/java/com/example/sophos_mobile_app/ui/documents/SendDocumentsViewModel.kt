package com.example.sophos_mobile_app.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.repository.DocumentRepository
import com.example.sophos_mobile_app.data.repository.OfficeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendDocumentsViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val officeRepository: OfficeRepository
) :
    ViewModel() {

    private val _statusPost = MutableLiveData<ResponseStatus<Boolean>>()
    val statusPost: LiveData<ResponseStatus<Boolean>>
        get() = _statusPost

    private val _cities = MutableLiveData<Set<String>>()
    val cities: LiveData<Set<String>>
        get() = _cities

    private val _statusOffice = MutableLiveData<ResponseStatus<Any>>()
    val statusOffice: LiveData<ResponseStatus<Any>>
        get() = _statusOffice

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
        _statusPost.value = ResponseStatus.Loading()
        viewModelScope.launch {
            val response = documentRepository.createNewDocument(
                idType, identification, name, lastname, city, email, attachedType, attached
            )
            when (response) {
                is ResponseStatus.Success -> {
                    _statusPost.postValue(ResponseStatus.Success(response.data))
                }
                is ResponseStatus.Error -> {
                    _statusPost.postValue(ResponseStatus.Error(response.messageId))
                }
                else -> {}
            }
        }
    }

    fun getOffices() {
        _statusOffice.value = ResponseStatus.Loading()
        viewModelScope.launch {
            val response = officeRepository.getOffices()
            when (response) {
                is ResponseStatus.Success -> {
                    _cities.postValue(response.data.map { office -> office.city }.toSet())
                    _statusOffice.postValue(ResponseStatus.Success(response.data))
                }
                is ResponseStatus.Error -> {
                    _statusOffice.postValue(ResponseStatus.Error(response.messageId))
                }
                else -> {}
            }
        }
    }

}
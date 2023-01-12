package com.example.sophos_mobile_app.ui.offices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.data.repository.OfficeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficesViewModel @Inject constructor(
    private val officesRepository: OfficeRepository
) : ViewModel() {

    private val _offices = MutableLiveData<List<Office>?>()
    val offices: LiveData<List<Office>?>
        get() = _offices

    private val _status = MutableLiveData<ResponseStatus<Any>>()
    val status: LiveData<ResponseStatus<Any>> get() = _status

    fun getOffices() {
        _status.postValue(ResponseStatus.Loading())
        viewModelScope.launch {
            when(val response =  officesRepository.getOffices()){
                is ResponseStatus.Success -> {
                    _offices.postValue(response.data)
                    _status.postValue(ResponseStatus.Success(response.data))
                }
                is ResponseStatus.Error -> {
                    _status.value = ResponseStatus.Error(response.messageId)
                }
                else -> {}
            }
        }
    }


}
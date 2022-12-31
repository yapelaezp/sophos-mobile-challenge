package com.example.sophos_mobile_app.ui.offices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.data.repository.OfficeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficesViewModel @Inject constructor(private val officesRepository: OfficeRepository): ViewModel() {

    private val _offices = MutableLiveData<List<Office>>()
    val offices: LiveData<List<Office>>
        get() = _offices

    fun getOffices(){
        viewModelScope.launch {
            _offices.value = officesRepository.getOffices()
        }
    }
}
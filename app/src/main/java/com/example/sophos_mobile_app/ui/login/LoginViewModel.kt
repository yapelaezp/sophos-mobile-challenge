package com.example.sophos_mobile_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.source.remote.api.dto.UserDto
import com.example.sophos_mobile_app.data.model.User
import com.example.sophos_mobile_app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel(){

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val _status = MutableLiveData<ResponseStatus<Any>>()
    val status: LiveData<ResponseStatus<Any>> get() = _status

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _status.value = ResponseStatus.Loading()
            when(val response = userRepository.getUserById(email, password)){
                is ResponseStatus.Success -> {
                    _user.postValue(response.data)
                    _status.postValue(ResponseStatus.Success(response.data))
                }
                is ResponseStatus.Error -> {
                    _status.postValue(ResponseStatus.Error(response.messageId))
                }
                else -> {}
            }
        }
    }
}
package com.example.sophos_mobile_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.api.ProvideRetrofit
import com.example.sophos_mobile_app.data.api.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel(){

    private val _user = MutableLiveData<UserDto>()
    val user: LiveData<UserDto>
        get() = _user

    private val retrofit = ProvideRetrofit

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = retrofit.api.getUserById(email, password)
            _user.value = response
        }
    }
}
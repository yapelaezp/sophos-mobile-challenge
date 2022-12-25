package com.example.sophos_mobile_app.ui.login

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos_mobile_app.data.model.User
import com.example.sophos_mobile_app.data.repository.UserRepository
import com.example.sophos_mobile_app.data.repository.UserRepositoryImpl
import com.example.sophos_mobile_app.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel(){

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = userRepository.getUserById(email, password)
            _user.value = response
        }
    }


}
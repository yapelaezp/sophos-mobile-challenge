package com.example.sophos_mobile_app.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sophos_mobile_app.MainCoroutineRule
import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.User
import com.example.sophos_mobile_app.data.repository.FakeUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class LoginViewModelTest {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        val fakeUserRepository = FakeUserRepository()
        loginViewModel = LoginViewModel(fakeUserRepository)
    }

    @Test
    fun login_successUserCredentials(){
        //Arrange
        val email = "alejo51120@gmail.com"
        val password = "123456"
        loginViewModel.login(email, password)
        //Action
        val expected = User("129", "Yesid Alejandro", "Pelaez Posada",
            true, false)
        val actual = loginViewModel.user.value
        //Assert
        assertEquals(expected, actual)
    }

    @Test
    fun login_userDoesNotExists(){
        //Arrange
        val email = "naruto@gmail.com"
        val password = "123456"
        //Action
        val actual = loginViewModel.user.value
        val expected = null
        //Assert
        assertEquals(expected, actual)
    }


}
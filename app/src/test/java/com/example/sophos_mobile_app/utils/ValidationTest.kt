package com.example.sophos_mobile_app.utils

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class ValidationTest{

    @Test
    fun testIsFieldEmpty(){
        //Arrange
        val email = "conan@yahoo.com"
        val password = ""
        val city = "Berlin"

        //Action

        val actual = Validation.isFieldNullOrEmpty(email,password, city)
        //Assert
        assertEquals(Pair(false, ""), actual)
    }

    @Test
    fun testIsEmailValid(){
        //Arrange
        val actual = Validation.isEmailValid("pikachu@yahoo.com")
        //Assert
        assertTrue(actual)
    }

    @Test
    fun testIsEmailInvalid(){
        //Arrange
        val actual = Validation.isEmailValid("pikachu.com")
        //Assert
        assertFalse(actual)
    }


}
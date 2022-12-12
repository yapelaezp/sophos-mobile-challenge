package com.example.sophos_mobile_app.utils

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class ValidationTest{

    @Test
    fun testIsFieldEmpty(){
        //Arrange
        val actual = Validation.isFieldEmpty("")
        //Assert
        assertTrue(actual)
    }

    @Test
    fun testIsFieldNull(){
        //Arrange
        val actual = Validation.isFieldEmpty(null)
        //Assert
        assertTrue(actual)
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
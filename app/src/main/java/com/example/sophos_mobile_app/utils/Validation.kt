package com.example.sophos_mobile_app.utils

import androidx.core.util.PatternsCompat

class Validation {
    companion object {

        fun isFieldEmpty(input: String?): Boolean{
            return input.isNullOrEmpty()
        }

        fun isEmailValid(email: String): Boolean {
            val emailPattern = PatternsCompat.EMAIL_ADDRESS
            return emailPattern.matcher(email).matches()
        }

    }
}
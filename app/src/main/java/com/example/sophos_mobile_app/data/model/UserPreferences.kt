package com.example.sophos_mobile_app.data.model

data class UserPreferences(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val biometricIntention: Boolean = false,
    val biometricEmail: String = "",
    val biometricPassword: String = ""
)

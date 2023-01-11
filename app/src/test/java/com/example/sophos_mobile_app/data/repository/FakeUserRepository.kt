package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.User

class FakeUserRepository : UserRepository {

    override suspend fun getUserById(email: String, password: String): ResponseStatus<User> {
        return if (email == "alejo51120@gmail.com" && password == "123456") {
            ResponseStatus.Success(
                User(
                    "129", "Yesid Alejandro", "Pelaez Posada",
                    true, false
                )
            )
        } else {
            ResponseStatus.Success(
                User(
                    null, null, null,
                    false, null
                )
            )
        }
    }
}




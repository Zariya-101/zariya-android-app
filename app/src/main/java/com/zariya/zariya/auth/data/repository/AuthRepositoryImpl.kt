package com.zariya.zariya.auth.data.repository

import com.zariya.zariya.app
import com.zariya.zariya.auth.domain.repository.AuthRepository
import io.realm.kotlin.mongodb.Credentials

class AuthRepositoryImpl: AuthRepository {

    override suspend fun createAccount(email: String, password: String) {
        app.emailPasswordAuth.registerUser(email, password)
    }

    override suspend fun login(email: String, password: String) {
        app.login(Credentials.emailPassword(email, password))
    }
}
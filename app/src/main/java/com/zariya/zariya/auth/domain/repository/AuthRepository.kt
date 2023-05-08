package com.zariya.zariya.auth.domain.repository

interface AuthRepository {

    suspend fun createAccount(email: String, password: String)

    suspend fun login(email: String, password: String)
}
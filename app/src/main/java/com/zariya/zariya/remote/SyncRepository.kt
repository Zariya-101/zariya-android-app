package com.zariya.zariya.remote

import com.zariya.zariya.auth.data.model.Customers


interface SyncRepository {

    suspend fun createCustomer(customer: Customers)
}
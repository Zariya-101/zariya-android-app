package com.zariya.zariya.remote

import android.util.Log
import com.zariya.zariya.app
import com.zariya.zariya.auth.data.model.Customers
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.SyncException
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.sync.SyncSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncRepositoryImpl : SyncRepository {

    private lateinit var realm: Realm
    private lateinit var config: SyncConfiguration
    private val currentUser: User
        get() = app.currentUser!!

    init {
        try {
            config = SyncConfiguration.Builder(currentUser, setOf(Customers::class))
                .initialSubscriptions { realm ->
                    add(realm.query<Customers>(), SubscriptionType.Customers.name)
                }
                .errorHandler { session: SyncSession, error: SyncException ->
                    Log.e("SyncRepositoryImpl", "Error in Sync: $error")
                }
                .waitForInitialRemoteData()
                .build()

            realm = Realm.open(config)

            CoroutineScope(Dispatchers.Main).launch {
                realm.subscriptions.waitForSynchronization()
            }
        } catch (e: Exception) {
            Log.e("SyncRepositoryImpl", e.toString())
        }
    }

    override suspend fun createCustomer(customer: Customers) {
        customer.owner_id = currentUser.id
        realm.write {
            copyToRealm(customer)
        }
    }
}

enum class SubscriptionType {
    MINE, ALL, Customers
}
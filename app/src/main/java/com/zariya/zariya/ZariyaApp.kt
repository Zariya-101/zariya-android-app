package com.zariya.zariya

import android.app.Application
import android.util.Log
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import io.realm.kotlin.syncSession
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.mongo.result.InsertOneResult
import io.realm.mongodb.sync.Subscription
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bson.Document
import org.bson.types.ObjectId
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import kotlin.coroutines.CoroutineContext

const val AppId = "application-0-lsfzx"

class ZariyaApp : Application(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

//    lateinit var user: User

    override fun onCreate() {
        super.onCreate()

        job = Job()

        try {
            Realm.init(this@ZariyaApp)

            val app = App(AppConfiguration.Builder(AppId).build())
            val credentials: Credentials = Credentials.anonymous()
            app.loginAsync(credentials) {
                if (it.isSuccess) {
                    Log.v("QUICKSTART", "Successfully authenticated anonymously.")
                    try {
                        val user: User? = app.currentUser()

                        // Working: via mongoCollection instance
//                        val mongoClient = user?.getMongoClient("mongodb-atlas")
//                        val db = mongoClient?.getDatabase("zariya-android-app")
//
//                        val mongoCollection = db?.getCollection("users")
//                        mongoCollection?.insertOne(Document("userId", "user12345"))?.getAsync(
//                            App.Callback<InsertOneResult> {
//                                if (it.isSuccess) {
//                                    Log.v("QUICKSTART", "Data stored")
//                                } else {
//                                    Log.e("QUICKSTART", "Data storing failed")
//                                }
//                            })

                        // Not Working: via Partition/Flexible Sync method

                        val userId = "user123456"
                        val partitionValue: String = userId
                        val config = SyncConfiguration.Builder(user)
                            .allowQueriesOnUiThread(true)
                            .allowWritesOnUiThread(true)
//                            .name(partitionValue)
                            .initialSubscriptions { realm, subscriptions ->
                                subscriptions.add(
                                    Subscription.create(
                                        "subscriptionName",
                                        realm.where(Users::class.java)
                                            .equalTo("userId", "user12345")
                                    )
                                )
                            }
                            .errorHandler { session, error ->
                                Log.e("QUICKSTART", "$session $error")
                            }
                            .build()
                        val backgroundThreadRealm = Realm.getInstance(config
//                            , object : Realm.Callback() {
//                            override fun onSuccess(realm: Realm) {
//                                Log.v("QUICKSTART", realm.toString())
////                                addChangeListenerToRealm(realm)
////
////                                val users = Users(_name = "Vivek", _phone = "9643014226")
////                                realm.executeTransaction { txnRealm ->
////                                    txnRealm.insert(users)
////                                }
////
////                                realm.close()
//                            }
//                        }
                        )

                        addChangeListenerToRealm(backgroundThreadRealm)

                        val users = Users(_name = "Vivek", _phone = "9643014226")
                        backgroundThreadRealm.executeTransaction { txnRealm ->
                            txnRealm.insert(users)
                        }
                        backgroundThreadRealm.close()
                        Log.v("QUICKSTART", "Successfully updated data.")
                    } catch (e: Exception) {
                        Log.e("QUICKSTART", e.toString())
                    }
                } else {
                    Log.e("QUICKSTART", "Failed to log in. Error: ${it.error}")
                }
            }
        } catch (e: Exception) {
            Log.e("QUICKSTART", e.toString())
        }
    }

    fun addChangeListenerToRealm(realm: Realm) {
        // all tasks in the realm
        val users: RealmResults<Users> = realm.where<Users>().findAllAsync()
        users.addChangeListener(OrderedRealmCollectionChangeListener<RealmResults<Users>> { collection, changeSet ->
            // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
            val deletions = changeSet.deletionRanges
            for (i in deletions.indices.reversed()) {
                val range = deletions[i]
                Log.v(
                    "QUICKSTART",
                    "Deleted range: ${range.startIndex} to ${range.startIndex + range.length - 1}"
                )
            }
            val insertions = changeSet.insertionRanges
            for (range in insertions) {
                Log.v(
                    "QUICKSTART",
                    "Inserted range: ${range.startIndex} to ${range.startIndex + range.length - 1}"
                )
            }
            val modifications = changeSet.changeRanges
            for (range in modifications) {
                Log.v(
                    "QUICKSTART",
                    "Updated range: ${range.startIndex} to ${range.startIndex + range.length - 1}"
                )
            }
        })
    }
}

open class Users(_userId: String = "", _name: String = "", _phone: String = "") : RealmObject() {
    @PrimaryKey
    var _id = ObjectId()
    var name = _name
    var phone = _phone
    var userId = _userId
}
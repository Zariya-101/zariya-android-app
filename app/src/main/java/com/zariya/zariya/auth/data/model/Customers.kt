package com.zariya.zariya.auth.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Customers() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var name: String = ""
    var owner_id: String = ""
    var phone: String = ""
    var dob: String = ""
    var countryCode = ""
}
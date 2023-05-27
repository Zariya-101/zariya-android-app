package com.zariya.zariya.auth.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class User(
    var id: String? = "",
    var name: String? = "",
    var phone: String? = "",
    var email: String? = "",
    var dob: String? = "",
    var countryCode: String? = "",
    var fcmToken: String? = "",
    var role: String? = ""
) : Parcelable {
    @get:Exclude
    var isNew: Boolean? = false

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        isNew = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(dob)
        parcel.writeString(countryCode)
        parcel.writeString(fcmToken)
        parcel.writeValue(isNew)
        parcel.writeValue(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
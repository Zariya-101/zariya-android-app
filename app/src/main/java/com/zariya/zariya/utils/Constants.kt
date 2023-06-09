package com.zariya.zariya.utils

import java.text.SimpleDateFormat

//Collections
const val COL_USERS = "users"
const val COL_ACTORS = "actors"
const val COL_AGENCIES = "agencies"
const val COL_VOLUNTEERS = "volunteers"
const val COL_CASTING_CALLS = "castingCalls"

//Fields
const val ID = "id"
const val FCM_TOKEN = "fcmToken"
const val PHONE = "phone"
const val EMAIL = "email"
const val USER_ID = "userId"
const val ROLE = "role"
const val LOCATION = "location"
const val WORKS_FOR = "worksFor"

const val RC_SIGN_IN = 123

//Storage Folders
const val FOL_IMAGES = "images"
const val FOL_ACTORS = "actors"
const val FOL_AGENCIES = "agencies"
const val FOL_CASTING_CALL = "castingCalls"

//User Roles
const val ACTOR = "actor"
const val AGENCY = "agency"
const val VOLUNTEER = "volunteer"

//Image Upload Type
const val ACTOR_PROFILE_IMAGE = "actor_profile_image"
const val AGENCY_PROFILE_IMAGE = "agency_profile_image"
const val CASTING_CALL_IMAGE = "casting_call_image"

const val PAID = "PAID"
const val UNPAID = "UNPAID"

val timestampFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
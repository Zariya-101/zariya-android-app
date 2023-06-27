package com.zariya.zariya.utils

import java.text.SimpleDateFormat

//Collections
const val COL_USERS = "users"
const val COL_ACTORS = "actors"
const val COL_AGENCIES = "agencies"
const val COL_VOLUNTEERS = "volunteers"
const val COL_CASTING_CALLS = "castingCalls"
const val COL_WORKSHOPS = "workshops"
const val COL_WORKSHOP_LIKES = "workshopLikes"

//Fields
const val ID = "id"
const val FCM_TOKEN = "fcmToken"
const val PHONE = "phone"
const val EMAIL = "email"
const val USER_ID = "userId"
const val ROLE = "role"
const val LOCATION = "location"
const val WORKS_FOR = "worksFor"
const val TYPE = "type"
const val LIKES = "likes"
const val WORKSHOP_ID = "workshopId"

const val RC_SIGN_IN = 123

//Storage Folders
const val FOL_IMAGES = "images"
const val FOL_ACTORS = "actors"
const val FOL_AGENCIES = "agencies"
const val FOL_CASTING_CALL = "castingCalls"
const val FOL_PROFILE_PIC = "profilePic"
const val FOL_COVER_PIC = "coverPic"

//User Roles
const val ACTOR = "actor"
const val AGENCY = "agency"
const val VOLUNTEER = "volunteer"

//Image Upload Type
const val ACTOR_PROFILE_IMAGE = "actor_profile_image"
const val AGENCY_PROFILE_IMAGE = "agency_profile_image"
const val CASTING_CALL_IMAGE = "casting_call_image"
const val USER_PROFILE_PIC = "user_profile_pic"
const val USER_COVER_PIC = "user_cover_pic"

const val PAID = "PAID"
const val UNPAID = "UNPAID"

//Workshop Types
const val ONLINE = "Online"
const val OFFLINE = "Offline"
const val PRODUCTIONS = "Productions"

val timestampFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
package com.vezdehod.vkmobi

import org.json.JSONObject

class User(var id: Long = 0, val firstName: String = "", val lastName: String = "", val photoUrl: String = ""){
    companion object{
        fun parse(json: JSONObject): User {
            return User(
                id = json.optLong("id", 0),
                firstName = json.optString("first_name", ""),
                lastName = json.optString("last_name", ""),
                photoUrl = json.optString("photo_200", "")
            )
        }
    }
}

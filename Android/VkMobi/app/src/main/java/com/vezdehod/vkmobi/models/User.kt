package com.vezdehod.vkmobi.models

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.vezdehod.vkmobi.R
import org.json.JSONObject
import java.net.ContentHandler
import java.security.AccessControlContext

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

    fun loadPhoto(context: Context, view: ImageView){
        Picasso.with(context)
            .load(photoUrl)
            .placeholder(R.drawable.warning)
            .error(R.drawable.placeholder)
            .into(view)
    }
}

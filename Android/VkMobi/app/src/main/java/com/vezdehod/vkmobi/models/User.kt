package com.vezdehod.vkmobi.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.vezdehod.vkmobi.R
import org.json.JSONObject
import java.lang.Exception

class User(var id: Long = 0, val firstName: String? = "", val lastName: String? = "", val photoUrl: String? = ""){
    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        firstName = parcel.readString(),
        lastName = parcel.readString(),
        photoUrl = parcel.readString()
    ) {}

    fun loadPhoto(context: Context, view: ImageView){
        try {
            Picasso.with(context)
                .load(photoUrl)
                .placeholder(R.drawable.warning)
                .error(R.drawable.placeholder)
                .into(view)
        }
        catch (e: Exception){}
    }

    companion object  {

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

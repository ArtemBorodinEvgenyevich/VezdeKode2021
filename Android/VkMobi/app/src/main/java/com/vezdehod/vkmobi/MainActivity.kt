package com.vezdehod.vkmobi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import com.vk.api.sdk.utils.VKUtils
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var token: VKAccessToken
    private lateinit var avatarView: ImageView
    private lateinit var fullNameView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        avatarView = findViewById(R.id.avatar)
        fullNameView = findViewById(R.id.fullName)

        VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                // User passed authorization
                this@MainActivity.token = token;

                Log.i("VkMobi", "Success")

                VK.execute(VKUsersRequest(), object: VKApiCallback<List<VKUser>> {
                    override fun success(result: List<VKUser>) {
                        var user = result[0]

                        fullNameView.text = user.firstName + " " + user.lastName

                        Log.i("VkMobi", result[0].firstName)
                    }
                    override fun fail(error: VKApiExecutionException) {
                        Log.i("VkMobi", error.errorMsg.toString())
                    }
                })
            }

            override fun onLoginFailed(errorCode: Int) {
                Log.i("VkMobi", "Fail")
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

class VKUsersRequest: VKRequest<List<VKUser>> {
    constructor(uids: IntArray = intArrayOf()): super("users.get") {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", "photo_200")
    }

    override fun parse(r: JSONObject): List<VKUser> {
        val users = r.getJSONArray("response")
        val result = ArrayList<VKUser>()
        for (i in 0 until users.length()) {
            result.add(VKUser.parse(users.getJSONObject(i)))
        }
        return result
    }
}

class VKUser(val id: Long = 0, val firstName: String = "", val lastName: String = "", val photo: String = "", val deactivated: Boolean = false) :Parcelable {
    constructor(parcel: Parcel) : this(parcel.readLong(),parcel.readString()!!,parcel.readString()!!,parcel.readString()!!,parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photo)
        parcel.writeByte(if (deactivated) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VKUser> {
        override fun createFromParcel(parcel: Parcel): VKUser {
            return VKUser(parcel)
        }

        override fun newArray(size: Int): Array<VKUser?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject)
                = VKUser(id = json.optLong("id", 0),
            firstName = json.optString("first_name", ""),
            lastName = json.optString("last_name", ""),
            photo = json.optString("photo_200", ""),
            deactivated = json.optBoolean("deactivated", false))
    }
}
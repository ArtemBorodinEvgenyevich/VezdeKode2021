package com.vezdehod.vkmobi

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import android.widget.*
import com.vezdehod.vkmobi.models.User
import com.vezdehod.vkmobi.models.UserRequest
import com.vk.api.sdk.utils.VKUtils
import com.vk.api.sdk.utils.VKUtils.getCertificateFingerprint


class MainActivity : AppCompatActivity(), Listener {
    private lateinit var token: VKAccessToken
    private lateinit var avatarView: ImageView
    private lateinit var fullNameView: TextView
    private lateinit var friendsView: ListView
    private lateinit var counterView: TextView
    private lateinit var adapter: FriendsAdapter
    private var friends: ArrayList<User> = ArrayList()

    private var api: ApiWrap = ApiWrap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFields()

        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.vk)))

        VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
    }

    private fun initFields(){
        avatarView = findViewById(R.id.avatar)
        fullNameView = findViewById(R.id.fullName)
        friendsView = findViewById(R.id.listFriends)
        counterView = findViewById(R.id.friendsCountView)

        //adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, friends)
        adapter = FriendsAdapter(this, friends)
        friendsView.adapter = adapter

        api.addListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                this@MainActivity.token = token;

                Log.i(Common.appLogTag, "Login success")

                api.loadUser()
                api.loadFriends()
            }

            override fun onLoginFailed(errorCode: Int) {
                Log.i(Common.appLogTag, "Fail")
            }
        }

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun update(users: ArrayList<User>) {
        //adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        adapter = FriendsAdapter(this, users)
        friendsView.adapter = adapter
        adapter.notifyDataSetChanged()
        counterView.text = "Количество: ${users.size}"
    }

    override fun update(user: User) {
        fullNameView.text = "${user.firstName} ${user.lastName}"
        user.loadPhoto(this, avatarView)
    }
}

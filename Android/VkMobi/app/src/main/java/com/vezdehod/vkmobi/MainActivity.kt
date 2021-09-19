package com.vezdehod.vkmobi

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
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
import android.widget.TextView
import android.graphics.Bitmap

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap


class MainActivity : AppCompatActivity(), Listener {
    private lateinit var token: VKAccessToken
    private lateinit var avatarView: ImageView
    private lateinit var fullNameView: TextView
    private lateinit var friendsView: ListView
    private lateinit var counterView: TextView
    private lateinit var adapter: FriendsAdapter
    private  var isLoggedIn: Boolean = false
    private var friends: ArrayList<User> = ArrayList()

    private var api: ApiWrap = ApiWrap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(Common.appLogTag, "OnCreate")
        initFields()

        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.vk)))

        if(savedInstanceState?.containsKey("isLoggedIn") == true)
            isLoggedIn = savedInstanceState.getBoolean("isLoggedIn")

        if(!isLoggedIn) {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
            isLoggedIn = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i(Common.appLogTag, "OnSavedInstance")

        outState.putString("name", fullNameView.text.toString())
        outState.putSerializable("friends", friends)
        outState.putParcelable("photo", avatarView.drawable?.toBitmap())
        outState.putString("count", counterView.text.toString())
        outState.putBoolean("isLoggedIn", isLoggedIn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        
        Log.i(Common.appLogTag, "OnRestoreInstance")

        fullNameView.text = savedInstanceState.getString("name")
        friends = savedInstanceState.getSerializable("friends") as ArrayList<User>
        avatarView.setImageBitmap(savedInstanceState.getParcelable<Bitmap>("photo"))
        counterView.text = savedInstanceState.getString("count")
        isLoggedIn = savedInstanceState.getBoolean("isLoggedIn")
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

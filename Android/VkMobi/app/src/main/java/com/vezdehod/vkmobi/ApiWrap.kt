package com.vezdehod.vkmobi

import android.util.Log
import com.vezdehod.vkmobi.models.FriendsRequest
import com.vezdehod.vkmobi.models.User
import com.vezdehod.vkmobi.models.UserRequest
import com.vk.api.sdk.VK

class ApiWrap: Listener {
    private val listeners = arrayListOf<Listener>()

    fun addListener(listener: Listener){
        listeners.add(listener)
    }

    fun loadFriends(){
        val users = arrayListOf<String>()
        val callback = FriendsCallback(users)
        callback.addListener(this)
        VK.execute(FriendsRequest(), callback)
    }

    fun loadUser(){
        val callback = UserCallback()
        callback.addListener(this)
        VK.execute(UserRequest(), callback)
    }

    override fun update(users: ArrayList<String>) {
        for(listener in listeners){
            listener.update(users)
        }
    }

    override fun update(user: User) {
        for(listener in listeners){
            listener.update(user)
        }
    }
}


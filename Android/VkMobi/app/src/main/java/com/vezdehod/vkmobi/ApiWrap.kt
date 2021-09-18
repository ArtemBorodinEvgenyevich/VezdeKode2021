package com.vezdehod.vkmobi

import android.util.Log
import com.vezdehod.vkmobi.models.FriendsRequest
import com.vezdehod.vkmobi.models.User
import com.vezdehod.vkmobi.models.UserRequest
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

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

class UserCallback(): VKApiCallback<List<User>>{
    private val listeners = arrayListOf<Listener>()

    fun addListener(listener: Listener){
        listeners.add(listener)
    }

    override fun fail(error: VKApiExecutionException) {
        Log.i(Common.appLogTag, error.errorMsg.toString())
    }

    override fun success(result: List<User>) {
        val user = result.first()
        for(listener in listeners){
            listener.update(user)
        }
    }
}

class FriendsCallback(val users: ArrayList<String>): VKApiCallback<List<User>>{
    private val listeners = arrayListOf<Listener>()

    fun addListener(listener: Listener){
        listeners.add(listener)
    }

    override fun fail(error: VKApiExecutionException) {
        Log.i(Common.appLogTag, error.errorMsg.toString())
    }

    override fun success(result: List<User>) {
        for (friend in result) {
            users.add(friend.firstName)
        }
        for(listener in listeners){
            listener.update(users)
        }
    }
}

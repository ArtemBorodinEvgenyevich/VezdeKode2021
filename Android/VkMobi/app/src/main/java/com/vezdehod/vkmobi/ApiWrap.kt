package com.vezdehod.vkmobi

import android.util.Log
import com.vezdehod.vkmobi.models.FriendsRequest
import com.vezdehod.vkmobi.models.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class ApiWrap: Listener {
    private val listeners = arrayListOf<Listener>()

    fun addListener(listener: Listener){
        listeners.add(listener)
    }

    fun loadUsers(){
        val users = arrayListOf<String>()
        val callback = UserCallback(users)
        callback.addListener(this)
        VK.execute(FriendsRequest(), callback)
    }

    override fun update(users: ArrayList<String>) {
        for(listener in listeners){
            listener.update(users)
        }
    }
}

class UserCallback(val users: ArrayList<String>): VKApiCallback<List<User>>{
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

package com.vezdehod.vkmobi

import android.util.Log
import com.vezdehod.vkmobi.models.User
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class FriendsCallback(private val users: ArrayList<User>): VKApiCallback<List<User>>, EventProvider {
    private val listeners = arrayListOf<Listener>()

    override fun addListener(listener: Listener){
        listeners.add(listener)
    }

    override fun fail(error: VKApiExecutionException) {
        Log.i(Common.appLogTag, error.errorMsg.toString())
    }

    override fun success(result: List<User>) {
        for (friend in result) {
            users.add(friend)
        }
        for(listener in listeners){
            listener.update(users)
        }
    }
}
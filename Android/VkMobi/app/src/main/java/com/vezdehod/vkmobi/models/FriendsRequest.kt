package com.vezdehod.vkmobi.models

import com.vezdehod.vkmobi.Methods
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class FriendsRequest : VKRequest<List<User>>(Methods.GET_FRIENDS.method) {
    private val fieldsKey = "fields"
    private val nickname = "nickname"
    private val response = "response"
    private val items = "items"

    init {
        addParam(fieldsKey, nickname)
    }

    override fun parse(r: JSONObject): List<User> {
        val obj = r.getJSONObject(response)
        val users = obj.getJSONArray(items)
        val result = ArrayList<User>()
        for (i in 0 until users.length()) {
            result.add(User.parse(users.getJSONObject(i)))
        }
        return result
    }
}

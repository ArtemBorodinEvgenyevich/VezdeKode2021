package com.vezdehod.vkmobi.models

import com.vezdehod.vkmobi.Methods
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class UserRequest(uids: IntArray = intArrayOf()) : VKRequest<List<User>>(Methods.GET_USERS.method) {
    private val userIdsKey = "user_ids"
    private val fieldsKey = "fields"
    private val photoValue = "photo_200"
    private val response = "response"

    init {
        if (uids.isNotEmpty()) {
            addParam(userIdsKey, uids.joinToString(","))
        }
        addParam(fieldsKey, photoValue)
    }

    override fun parse(json: JSONObject): List<User> {
        val users = json.getJSONArray(response)
        val result = ArrayList<User>()
        for (i in 0 until users.length()) {
            val jsonObject = users.getJSONObject(i)
            val user = User.parse(jsonObject)
            result.add(user)
        }
        return result
    }
}

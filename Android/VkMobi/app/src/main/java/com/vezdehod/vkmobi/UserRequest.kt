package com.vezdehod.vkmobi

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class UserRequest(uids: IntArray = intArrayOf()) : VKRequest<List<User>>("users.get") {
    init {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", "photo_200")
    }

    override fun parse(r: JSONObject): List<User> {
        val users = r.getJSONArray("response")
        val result = ArrayList<User>()
        for (i in 0 until users.length()) {
            result.add(User.parse(users.getJSONObject(i)))
        }
        return result
    }
}

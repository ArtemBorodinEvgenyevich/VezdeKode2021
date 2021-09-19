package com.vezdehod.vkmobi

import com.vezdehod.vkmobi.models.User

interface Listener{
    fun update(users: ArrayList<User>)
    fun update(user: User)
}

interface EventProvider{
    fun addListener(listener: Listener)
}
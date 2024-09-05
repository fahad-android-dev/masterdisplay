package com.orbits.masterdisplay.helper.interfaces

import com.google.gson.JsonObject

interface MessageListener {
    fun onMessageJsonReceived(jsonObject: JsonObject)
}
package com.orbits.masterdisplay.mvvm.main.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class ServiceListDataModel(
    val id: String ?= null,
    val name: String ?= null,
    val counterType: String ?= null,
    val counterId: String ?= null,
    val token: String ?= null,
    val keypadToken: String ?= null,
    val serviceId: String ?= null,
):java.io.Serializable

fun parseServiceJsonData(jsonObject: JsonObject): List<ServiceListDataModel> {
    val itemsList = mutableListOf<ServiceListDataModel>()
    val itemsArray: JsonArray = jsonObject.getAsJsonArray("items")

    for (element: JsonElement in itemsArray) {
        val itemObject = element.asJsonObject
        val item = Gson().fromJson(itemObject, ServiceListDataModel::class.java)
        itemsList.add(item)
    }

    return itemsList
}

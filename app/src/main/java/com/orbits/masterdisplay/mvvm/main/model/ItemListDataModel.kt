package com.orbits.masterdisplay.mvvm.main.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class ItemListDataModel(
    val id: String ?= null,
    val name: String ?= null,
    val counterType: String ?= null,
    val counterId: String ?= null,
    val token: String ?= null,
    val keypadToken: String ?= null,
    val serviceId: String ?= null,
):java.io.Serializable

fun parseJsonData(jsonObject: JsonObject): List<ItemListDataModel> {
    val itemsList = mutableListOf<ItemListDataModel>()
    val itemsArray: JsonArray = jsonObject.getAsJsonArray("transactions")

    for (element: JsonElement in itemsArray) {
        val itemObject = element.asJsonObject
        val item = Gson().fromJson(itemObject, ItemListDataModel::class.java)
        itemsList.add(item)
    }

    return itemsList
}

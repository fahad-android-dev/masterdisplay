package com.orbits.masterdisplay.mvvm.main.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.orbits.masterdisplay.helper.WebSocketClient
import com.orbits.masterdisplay.helper.interfaces.MessageListener
import com.orbits.masterdisplay.helper.interfaces.ReconnectionListener
import com.orbits.masterdisplay.mvvm.main.model.ItemListDataModel
import com.orbits.masterdisplay.mvvm.main.model.ServiceListDataModel
import com.orbits.masterdisplay.mvvm.main.model.parseJsonData
import com.orbits.masterdisplay.mvvm.main.model.parseServiceJsonData

class MainViewModel : ViewModel() , MessageListener {


    var isConnected: Boolean = false

    var webSocketClient: WebSocketClient? = null
    var dataModel: JsonObject? = null
    var dataList = ArrayList<ItemListDataModel>()
    var serviceList = ArrayList<ServiceListDataModel>()
    private var reconnectionListener :  ReconnectionListener?= null
    var isNext = false

    fun setReconnectionListener(listener: ReconnectionListener) {
        this.reconnectionListener = listener
    }


    fun connectWebSocket(ipAddress: String, port: String,services : String? = "") {
        webSocketClient = WebSocketClient("ws://$ipAddress:$port",this)
        webSocketClient?.connect()

        println("Here is websocket connected")

        isConnected = true
        val jsonObject = JsonObject()
        jsonObject.addProperty("masterDisplayConnection", "masterDisplayConnection")
        if (!services.isNullOrEmpty()) jsonObject.addProperty("services", services)

        if (isConnected) {
            webSocketClient?.sendMessage(jsonObject)
            Log.d("WebSocketViewModel", "Message sent: $jsonObject")
        } else {
            Log.e("WebSocketViewModel", "WebSocket is not connected, cannot send message.")
        }

    }



    override fun onCleared() {
        super.onCleared()
        isConnected = false
        webSocketClient?.disconnect() // Clean up WebSocket connection
    }

    override fun onMessageJsonReceived(jsonObject: JsonObject) {

        println("here is msg $jsonObject")

        isNext = jsonObject.has(",msg")

        if (jsonObject.has("transaction")){
            dataModel = jsonObject
            println("here is data with token $dataModel")

        }
        else {
            val services = parseServiceJsonData(jsonObject)
            println("Parsed services items: $services")
            serviceList.clear()
            serviceList.addAll(services)

            val transactions = parseJsonData(jsonObject)
            println("Parsed items: $transactions")
            dataList.clear()
            dataList.addAll(transactions)
            reconnectionListener?.onConnectionRestarted()
        }

    }

    fun sendReConnectionMessage(services : String? = "") {
        val jsonObject = JsonObject()
        jsonObject.addProperty("masterReconnection", "masterReconnection")
        if (!services.isNullOrEmpty()) jsonObject.addProperty("services", services)

        webSocketClient?.sendMessage(jsonObject)
        Log.d("Connection Message", "Sending message: $jsonObject")
    }





}
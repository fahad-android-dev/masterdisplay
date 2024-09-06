package com.orbits.masterdisplay.mvvm.main.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.orbits.masterdisplay.helper.WebSocketClient
import com.orbits.masterdisplay.helper.interfaces.MessageListener
import com.orbits.masterdisplay.helper.interfaces.ReconnectionListener
import com.orbits.masterdisplay.mvvm.main.model.ItemListDataModel
import com.orbits.masterdisplay.mvvm.main.model.parseJsonData

class MainViewModel : ViewModel() , MessageListener {


    var isConnected: Boolean = false

    var webSocketClient: WebSocketClient? = null
    var dataModel: JsonObject? = null
    var dataList = ArrayList<ItemListDataModel>()
    private var reconnectionListener :  ReconnectionListener?= null

    fun setReconnectionListener(listener: ReconnectionListener) {
        this.reconnectionListener = listener
    }


    fun connectWebSocket(ipAddress: String, port: String) {
        webSocketClient = WebSocketClient("ws://$ipAddress:$port",this)
        webSocketClient?.connect()

        println("Here is websocket connected")

        isConnected = true
        val jsonObject = JsonObject()
        jsonObject.addProperty("masterDisplayConnection", "masterDisplayConnection")

        if (isConnected) {
            webSocketClient?.sendMessage(jsonObject)
            Log.d("WebSocketViewModel", "Message sent: $jsonObject")
        } else {
            Log.e("WebSocketViewModel", "WebSocket is not connected, cannot send message.")
        }

    }


    fun sendMessage(message: JsonObject) {
        webSocketClient?.sendMessage(message)
    }


    override fun onCleared() {
        super.onCleared()
        isConnected = false
        webSocketClient?.disconnect() // Clean up WebSocket connection
    }

    override fun onMessageJsonReceived(jsonObject: JsonObject) {

        println("here is msg $jsonObject")


        if (jsonObject.has("transaction")){
            dataModel = jsonObject
            println("here is data with token $dataModel")

        }else {
            val transactions = parseJsonData(jsonObject)
            println("Parsed items: $transactions")
            dataList.clear()
            dataList.addAll(transactions)

            reconnectionListener?.onConnectionRestarted()
        }

    }

    fun sendReConnectionMessage() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("masterReconnection", "masterReconnection")

        webSocketClient?.sendMessage(jsonObject)
        Log.d("Connection Message", "Sending message: $jsonObject")
    }





}
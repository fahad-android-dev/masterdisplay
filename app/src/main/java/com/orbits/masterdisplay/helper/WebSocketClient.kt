package com.orbits.masterdisplay.helper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.orbits.masterdisplay.helper.interfaces.MessageListener
import okhttp3.*
import okio.ByteString

class WebSocketClient(private val serverUrl: String, private val messageListener: MessageListener) {

    private var webSocket: WebSocket? = null

    fun connect() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(serverUrl)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // WebSocket connection established
                println("WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // Received text message
                println("Received message: $text")

                // Notify listener or perform other actions
                val jsonObject = Gson().fromJson(text, JsonObject::class.java)
                messageListener.onMessageJsonReceived(jsonObject)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Received binary message
                println("Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // WebSocket is closing
                println("WebSocket closing: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // WebSocket connection error
                println("WebSocket connection error: ${t.message}")
            }
        })
    }

    fun sendMessage(jsonObject : JsonObject) {
        webSocket?.send(jsonObject.toString())
    }

    private fun reconnect() {
        // Optionally add a delay or retry mechanism here
        connect()
    }

    fun disconnect() {
        println("here is WebSocket disconnected")
        webSocket?.close(1000, null)
    }
}

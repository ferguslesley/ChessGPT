package com.example.chessgpt

import android.content.Context
import com.example.chessgpt.openai.sendRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Callable

class OpenAi(private val userMessages: MutableList<String>, private val aiMessages: MutableList<String>, private val context: Context) : Callable<String> {
    override fun call(): String {
        return sendRequest(createMessagesArray(userMessages, aiMessages), context)
    }

    private fun createMessagesArray(userMessages: MutableList<String>, aiMessages: MutableList<String>): JSONArray {
        val jsonArray = JSONArray()

        val maxLength = maxOf(userMessages.size, aiMessages.size)

        for (i in 0 until maxLength) {
            val userMessage = JSONObject()
            if (i < userMessages.size) {
                userMessage.put("role", "user")
                userMessage.put("content", userMessages[i])
                jsonArray.put(userMessage)
            }
            val aiMessage = JSONObject()
            if (i < aiMessages.size) {
                aiMessage.put("role", "assistant")
                aiMessage.put("content", aiMessages[i])
                jsonArray.put(aiMessage)
            }
        }

        return jsonArray
    }
}
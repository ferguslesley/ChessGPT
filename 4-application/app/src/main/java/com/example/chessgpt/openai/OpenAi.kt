package com.example.chessgpt.openai

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Callable

class OpenAi(private val userMessages: MutableList<String>, private val aiMessages: MutableList<String>, private val context: Context) : Callable<String> {
    override fun call(): String {
        return sendRequest(createMessagesArray(userMessages, aiMessages), context)
    }

    private fun createMessagesArray(userMessages: MutableList<String>, aiMessages: MutableList<String>): JSONArray {
        val jsonArray = JSONArray()
        val acceptableLength = 5
        val maxLength = maxOf(userMessages.size, aiMessages.size)
        var start = 1

        // Reduce context token count
        if (maxLength > acceptableLength) {
            start = maxLength - acceptableLength
        }

        // Always include the initial prompt
        val prompt = JSONObject()
        prompt.put("role", "user")
        prompt.put("content", userMessages[0])
        jsonArray.put(prompt)

        // Ignores the "ok" response by the ai, that's fine.

        for (i in start until maxLength) {
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
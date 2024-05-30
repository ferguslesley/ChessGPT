package com.example.chessgpt.openai

import android.content.Context
import android.util.Log
import com.example.chessgpt.data.db
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ApiServiceImpl : ApiService {
    override fun sendRequest(messages: JSONArray, context: Context): String {
        val apiKey = getApiKey()
        val model = "gpt-3.5-turbo-0125"
        val maxTokens = 10

        val tokenManager = TokenManager(context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE))

        if (!tokenManager.hasTokensLeft()) {
            return "Error: No more tokens left for today!"
        }
        val url = URL("https://api.openai.com/v1/chat/completions")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "Bearer $apiKey")

        connection.doOutput = true
        val requestBody = JSONObject()
        requestBody.put("model", model)
        requestBody.put("messages", messages)
        requestBody.put("max_tokens", maxTokens)

        val writer = OutputStreamWriter(connection.outputStream)
        writer.write(requestBody.toString())
        writer.flush()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            // Deduct tokens used to make request from user's quota
            tokenManager.deductTokens(extractUsage(response.toString()).toInt())
            Log.i("OpenAI", "Messages: $messages")
            Log.i("OpenAI", "Response: ${extractContent(response.toString())}")
            return extractContent(response.toString())
        } else {
            val errorStream = connection.errorStream
            val errorMessage = if (errorStream != null) {
                BufferedReader(InputStreamReader(errorStream)).use(BufferedReader::readText)
            } else {
                "Unknown error"
            }
            Log.e("OpenAI", "Error: $errorMessage")
            return "Error: $errorMessage"
        }
    }

    private fun extractContent(jsonString: String): String {
        try {
            val jsonObject = JSONObject(jsonString)
            val choicesArray = jsonObject.getJSONArray("choices")
            if (choicesArray.length() > 0) {
                val firstChoice = choicesArray.getJSONObject(0)
                val messageObject = firstChoice.getJSONObject("message")
                return messageObject.getString("content")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun extractUsage(jsonString: String): String {
        try {
            val jsonObject = JSONObject(jsonString)
            val usages = jsonObject.getJSONObject("usage")
            return usages.getString("total_tokens")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getApiKey(): String {
        return db.userDao().getAll().last().apiKey
    }
}
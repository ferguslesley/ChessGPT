package com.example.chessgpt.openai

import android.content.Context
import org.json.JSONArray

interface ApiService {
    fun sendRequest(messages: JSONArray, context: Context): String
}
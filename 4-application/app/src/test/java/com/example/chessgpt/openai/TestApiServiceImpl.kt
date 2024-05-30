package com.example.chessgpt.openai

import android.content.Context
import org.json.JSONArray

class TestApiServiceImpl : ApiService {
    override fun sendRequest(messages: JSONArray, context: Context): String {
        return "pawn e7 -> e5"
    }

}
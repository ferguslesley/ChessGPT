package com.example.chessgpt.openai

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Calendar

class TokenManager(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val PREF_KEY_TOKEN_COUNT = "token_count"
        private const val MAX_TOKENS_PER_DAY = 20000
    }

    fun deductTokens(tokens: Int) {
        val currentTokenCount = getCurrentTokenCount()
        saveTokenCount(currentTokenCount + tokens)
    }

    private fun getCurrentTokenCount(): Int {
        val currentTime = System.currentTimeMillis()
        val lastTime = sharedPreferences.getLong("last_time", 0)
        if (isSameDay(currentTime, lastTime)) {
            return sharedPreferences.getInt(PREF_KEY_TOKEN_COUNT, 0)
        } else {
            sharedPreferences.edit {
                putLong("last_time", currentTime)
                putInt(PREF_KEY_TOKEN_COUNT, 0)
            }
        }
        return 0
    }

    private fun saveTokenCount(tokenCount: Int) {
        sharedPreferences.edit {
            putInt(PREF_KEY_TOKEN_COUNT, tokenCount)
        }
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = time1
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = time2

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    fun hasTokensLeft(): Boolean {
        return getCurrentTokenCount() < MAX_TOKENS_PER_DAY
    }

    fun getTokensLeft(): Int {
        return MAX_TOKENS_PER_DAY - getCurrentTokenCount()
    }


}
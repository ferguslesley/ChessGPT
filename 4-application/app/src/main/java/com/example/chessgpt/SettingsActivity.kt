package com.example.chessgpt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chessgpt.data.db
import com.example.chessgpt.openai.getApiKey
import com.example.chessgpt.user.User
import com.example.chessgpt.user.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    private lateinit var apiKeyEditText: EditText
    private lateinit var editButton: Button
    private lateinit var backButton: Button
    private lateinit var apiKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        apiKeyEditText = findViewById(R.id.api_key_input)
        editButton = findViewById(R.id.edit_button)
        backButton = findViewById(R.id.back_button)
        lifecycleScope.launch(Dispatchers.IO) {
            apiKey = getApiKey()
            withContext(Dispatchers.Main) {
                updateEditText()
            }
        }

        editButton.setOnClickListener {
            if (apiKeyEditText.isEnabled) {
                // User clicked Save button
                onSaveButtonClick()
            } else {
                // User clicked Edit button
                onEditButtonClick()
            }
        }

        apiKeyEditText.setOnClickListener {
            onEditButtonClick()
        }

        backButton.setOnClickListener {
            // Send data to main activity where it will update the database with the new info
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("API_KEY", apiKeyEditText.text.toString().trim())
            }
            startActivity(intent)
        }

    }

    private fun onSaveButtonClick() {
        apiKey = apiKeyEditText.text.toString().trim()
        apiKeyEditText.isEnabled = false
        editButton.text = getString(R.string.edit_button_text)
    }

    private fun onEditButtonClick() {
        apiKeyEditText.isEnabled = true
        apiKeyEditText.requestFocus()
        editButton.text = getString(R.string.save_button_text)
    }

    private fun updateEditText() {
        if (apiKey.isNotEmpty()) {
            apiKeyEditText.setText(apiKey)
            apiKeyEditText.isEnabled = false
            editButton.text = getString(R.string.edit_button_text)
        } else {
            apiKeyEditText.setText("")  // Only show text hint
            apiKeyEditText.isEnabled = true
            apiKeyEditText.hint = getString(R.string.api_key)
            editButton.text = getString(R.string.save_button_text)
        }
    }


}
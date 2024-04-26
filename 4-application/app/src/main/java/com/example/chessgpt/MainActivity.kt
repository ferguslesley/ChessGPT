package com.example.chessgpt

import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.chessgpt.data.buildDb
import com.example.chessgpt.data.db
import com.example.chessgpt.user.User
import com.example.chessgpt.user.UserDao
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Properties
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: MyViewAdapter
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.view_pager)
        adapter = MyViewAdapter(this)
        viewPager.adapter = adapter

        // Setup tab layout
        tabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Board"
                1 -> "Stats"
                else -> null
            }
        }.attach()

        setupDb()

    }

    private fun saveSettings() {
        val apiKey = intent.getStringExtra("API_KEY")
        if (!apiKey.isNullOrEmpty()) {
            viewModel.setApiKey(apiKey)
            updateUser()
        }
    }

    private fun setupDb() {
        lifecycleScope.launch(Dispatchers.IO) {
            buildDb(applicationContext)
            val userDao: UserDao = db.userDao()
            val lifecycleOwner = this@MainActivity
            val users: List<User> = userDao.getAll()
            val user: User
            // Get the latest user (I don't know what's best practice for this)
            Log.i("Database", "New user = ${users.isEmpty()}")
            Log.i("Database", "Users: $users")
            if (users.isNotEmpty()) {
                user = users.last()
            } else {
                user = User(0, 0, 0, "")
                userDao.newUser(user)
            }

            withContext(Dispatchers.Main) {
                if (user.apiKey.isEmpty()) {
                    showApiKeyAlert()
                }
                // Create view model to pass data to fragments
                viewModel = ViewModelProvider(lifecycleOwner)[UserViewModel::class.java]
                viewModel.setUser(user)
                saveSettings()  // Save settings from settings activity, if any
            }
        }
    }

    private fun updateUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userDao: UserDao = db.userDao()
            userDao.updateUser(viewModel.getUser()!!)
        }
    }

    fun win() {
        Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
        viewModel.incrementWins()
        updateUser()
    }

    fun lose() {
        Toast.makeText(this, "You Lose! :(", Toast.LENGTH_SHORT).show()
        viewModel.incrementLosses()
        updateUser()
    }

    fun showApiKeyAlert() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Please enter your OpenAI API key")
        val input = EditText(this)
        input.setText(getString(R.string.api_key_alert_text, viewModel.currentUser.value!!.apiKey))
        alertDialog.setView(input)
        alertDialog.setPositiveButton("Done") { dialog, _ ->
            val apiKey = input.text.toString().trim()
            saveApiKey(apiKey)
            dialog.dismiss()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun showTokenLimitAlert() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Sorry, token limit reached!")
        alertDialog.setMessage("You've used all your tokens for the day. They will refresh tomorrow!")
        alertDialog.setPositiveButton("Buy more tokens (w.i.p.)") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("Ok :(") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun saveApiKey(apiKey: String) {
            viewModel.setApiKey(apiKey)
            updateUser()
    }
}

// In your shared ViewModel
class UserViewModel : ViewModel() {
    val currentUser = MutableLiveData<User>()

    fun setUser(user: User) {
        currentUser.value = user
    }

    fun getUser(): User? {
        return currentUser.value
    }

    fun incrementWins() {
        val currentUserVal = currentUser.value
        currentUserVal?.let {
            it.wins++
            currentUser.value = it
        }
    }

    fun incrementLosses() {
        val currentUserVal = currentUser.value
        currentUserVal?.let {
            it.losses++
            currentUser.value = it
        }
    }

    fun setApiKey(givenApiKey: String) {
        val currentUserVal = currentUser.value
        currentUserVal?.let {
            it.apiKey = givenApiKey
            currentUser.value = it
        }
    }


}
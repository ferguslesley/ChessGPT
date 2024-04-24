package com.example.chessgpt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                user = User(0, 0, 0)
                userDao.newUser(user)
            }

            withContext(Dispatchers.Main) {

                // Create view model to pass data to fragments
                viewModel = ViewModelProvider(lifecycleOwner)[UserViewModel::class.java]
                viewModel.setUser(user)
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


}
package com.example.chessgpt.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Update
    fun updateUser(user: User)

    @Insert
    fun newUser(user: User)
}
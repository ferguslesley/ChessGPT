package com.example.chessgpt.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chessgpt.user.AppDatabase

lateinit var db: AppDatabase
fun buildDb(context: Context) {
    db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "chess_gpt_database"
    ).build()
}

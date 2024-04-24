package com.example.chessgpt.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true) val userId: Int,
    @ColumnInfo(name = "wins") var wins: Int,
    @ColumnInfo(name = "losses") var losses: Int
)
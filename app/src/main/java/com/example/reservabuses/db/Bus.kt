package com.example.reservabuses.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buses")
data class Buses(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "bid") val bid: Int,
    @ColumnInfo(name = "schedule") val schedule: String,
    @ColumnInfo(name = "capacity") val password: Int
)
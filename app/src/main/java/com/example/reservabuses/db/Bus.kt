package com.example.reservabuses.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "buses")
data class Buses(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "bid") val bid: Int,
    @ColumnInfo(name = "schedule") val schedule: LocalDateTime,
    @ColumnInfo(name = "capacity") val capacity: Int
)
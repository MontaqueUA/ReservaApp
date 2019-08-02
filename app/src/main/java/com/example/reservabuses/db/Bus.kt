package com.example.reservabuses.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "buses")
data class Buses(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "bid") val bid: Int,
    @ColumnInfo (name = "schedule") @NotNull val schedule: String?,
    @ColumnInfo(name = "capacity") @NotNull val capacity: Int
)
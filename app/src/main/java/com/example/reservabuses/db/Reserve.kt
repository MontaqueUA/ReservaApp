package com.example.reservabuses.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "reserve", foreignKeys =
    [ForeignKey(entity = User::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("userId")),
    ForeignKey(entity = Buses::class,
        parentColumns = arrayOf("bid"),
        childColumns = arrayOf("busId")
    )])
data class Reserve(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "busId") val busId: String
)

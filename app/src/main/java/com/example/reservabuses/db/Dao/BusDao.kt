package com.example.reservabuses.db.Dao

import androidx.room.Dao
import androidx.room.Query
import com.example.reservabuses.db.Buses

@Dao
interface BusDao {
    @Query("SELECT * FROM buses WHERE bid LIKE :id")
    fun getBusById (id: Int): Buses?
}
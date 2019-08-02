package com.example.reservabuses.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reservabuses.db.Reserve
import com.example.reservabuses.db.User

@Dao
interface ReserveDao {
    @Query("SELECT * FROM reserve WHERE rid LIKE :id")
    fun getReserveById(id: Int): Reserve?
    @Query("SELECT * FROM reserve WHERE busId LIKE :bid")
    fun getReserveByBusId(bid: Int): Reserve?
    @Query("SELECT * FROM reserve WHERE userId LIKE :uid AND timeOfRes > date('now', 'start of day') AND timeOfRes < date('now', 'start of day', '+1 day')")
    fun getReserveByUserToday(uid: Int): Reserve?
    @Insert
    fun insertAll(vararg reserve: Reserve)
}
package com.example.reservabuses.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reservabuses.db.Buses
import java.util.*

@Dao
interface BusDao {
    @Query("SELECT * FROM buses WHERE bid LIKE :id")
    fun getBusById (id: Int): Buses?
    @Query("SELECT * FROM buses WHERE date(datetime(schedule/1000, 'unixepoch')) = date('now')")
    fun getBusForToday () :Buses?
    @Query("SELECT COUNT(r.rid) FROM buses b, reserve r WHERE r.busId=:busId")
    fun getReserveCountForBus (busId: Int) :Int
    @Insert
    fun insertAll(vararg buses: Buses)
}
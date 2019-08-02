package com.example.reservabuses.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reservabuses.db.Buses
import java.util.*

@Dao
interface BusDao {
    @Query("SELECT * FROM buses")
    fun getAllBuses (): Array<Buses>
    @Query("SELECT * FROM buses WHERE bid LIKE :id")
    fun getBusById (id: Int): Buses?
    @Query("""SELECT * FROM buses WHERE schedule > date('now', 'start of day') AND schedule < date('now', 'start of day', '+1 day') ORDER BY bid""") //2019-07-17T07:50
    fun getBusForToday () :Array<Buses>
    @Query("SELECT COUNT(r.rid) FROM buses b, reserve r WHERE r.busId=:busId")
    fun getReserveCountForBus (busId: Int) :Int
    @Query("SELECT COUNT(r.rid) FROM buses b, reserve r WHERE r.busId=b.bid ORDER BY b.bid")
    fun getAllReserveCountForBuses () :Array<Int>
    @Insert
    fun insertAll(vararg buses: Buses)
}
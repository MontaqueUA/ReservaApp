package com.example.reservabuses.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reservabuses.db.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uid LIKE :id")
    fun getUser (id: Int): User?
    @Query("SELECT * FROM users WHERE email LIKE :email")
    fun getUser (email: String): User?
    @Insert
    fun insertAll(vararg user: User)
}
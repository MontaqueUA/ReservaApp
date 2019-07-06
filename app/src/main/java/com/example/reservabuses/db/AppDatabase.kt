package com.example.reservabuses.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bus::class, User::class, Reserve::class, ComplaintVote::class], version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun complaintDao(): ComplaintDao
    abstract fun userDao(): UserDao
    abstract fun commentaryDao(): CommentaryDao
    abstract fun complaintVoteDao(): ComplaintVoteDao

    // Add Singleton to prevent having multiple instances
    // of the database opened at the same time.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
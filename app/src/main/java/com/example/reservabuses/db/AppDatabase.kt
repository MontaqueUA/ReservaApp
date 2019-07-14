package com.example.reservabuses.db

import android.content.Context
import androidx.room.*
import com.example.reservabuses.Converters
import com.example.reservabuses.db.Dao.BusDao
import com.example.reservabuses.db.Dao.UserDao
import com.example.reservabuses.db.Dao.ReserveDao

@Database(entities = [Buses::class, User::class, Reserve::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun reserveDao(): ReserveDao
    abstract fun userDao(): UserDao
    abstract fun busDao(): BusDao

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
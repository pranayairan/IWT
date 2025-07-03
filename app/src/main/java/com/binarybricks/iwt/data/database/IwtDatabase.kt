package com.binarybricks.iwt.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.binarybricks.iwt.data.model.Converters
import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.data.model.WorkoutPreset

@Database(
    entities = [WorkoutLog::class, WorkoutPreset::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class IwtDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: IwtDatabase? = null

        fun getDatabase(context: Context): IwtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IwtDatabase::class.java,
                    "iwt_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
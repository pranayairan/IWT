package com.binarybricks.iwt.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.data.model.WorkoutPreset
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // WorkoutLog queries
    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs ORDER BY date DESC LIMIT 1")
    fun getLatestWorkoutLog(): Flow<WorkoutLog?>

    @Query("SELECT * FROM workout_logs WHERE id = :id")
    suspend fun getWorkoutLogById(id: String): WorkoutLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLog): Long

    // WorkoutPreset queries
    @Query("SELECT * FROM workout_presets")
    fun getAllWorkoutPresets(): List<WorkoutPreset>

    @Query("SELECT * FROM workout_presets WHERE id = :id")
    suspend fun getWorkoutPresetById(id: String): WorkoutPreset?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPreset(preset: WorkoutPreset)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPresets(presets: List<WorkoutPreset>)
}
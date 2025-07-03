package com.binarybricks.iwt.di

import android.content.Context
import com.binarybricks.iwt.data.database.IwtDatabase
import com.binarybricks.iwt.data.database.WorkoutDao
import com.binarybricks.iwt.data.repository.RoomWorkoutRepository
import com.binarybricks.iwt.data.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        repository: RoomWorkoutRepository
    ): WorkoutRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): IwtDatabase {
        return IwtDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideWorkoutDao(database: IwtDatabase): WorkoutDao {
        return database.workoutDao()
    }
}
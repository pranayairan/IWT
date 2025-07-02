package com.binarybricks.iwt.di

import com.binarybricks.iwt.data.repository.FakeWorkoutRepository
import com.binarybricks.iwt.data.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.text.Typography.dagger

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        fakeWorkoutRepository: FakeWorkoutRepository
    ): WorkoutRepository
}
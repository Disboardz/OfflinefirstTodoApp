package com.example.offlinefirsttodoapp.di

import com.example.offlinefirsttodoapp.data.repository.TaskRepositoryImpl
import com.example.offlinefirsttodoapp.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TasksRepository
}
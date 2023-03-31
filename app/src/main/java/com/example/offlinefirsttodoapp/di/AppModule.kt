package com.example.offlinefirsttodoapp.di

import android.app.Application
import androidx.room.Room
import com.example.offlinefirsttodoapp.data.local.TodoAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoAppDatabase(app: Application): TodoAppDatabase {
        return Room.databaseBuilder(
            app,
            TodoAppDatabase::class.java,
            "todo_app_database.db"
        ).createFromAsset("database/TodoApp.db")
            .build()
    }
}
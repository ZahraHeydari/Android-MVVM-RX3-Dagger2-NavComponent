package com.zest.android.di.module

import android.app.Application
import androidx.room.Room
import com.zest.android.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideAppDatabase(application: Application): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()//allows database to be cleared after upgrading version
                    .build()
}
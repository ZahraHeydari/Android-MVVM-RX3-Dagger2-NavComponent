package com.zest.android.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zest.android.data.model.Recipe
import com.zest.android.data.source.local.dao.RecipeDao

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val recipeDao: RecipeDao

    companion object {
        val DB_NAME = "ZestDatabase.db"
    }
}

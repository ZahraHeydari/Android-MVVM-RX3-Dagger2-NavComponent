package com.zest.android.data.source.local.dao

import androidx.room.*
import com.zest.android.data.model.Recipe


@Dao
interface RecipeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe)

    @Query("SELECT * FROM Recipe")
    fun loadAll(): MutableList<Recipe>

    @Delete
    fun delete(recipe: Recipe)

    @Query("SELECT * FROM Recipe where recipeId = :recipeId")
    fun loadOneByRecipeId(recipeId: String): Recipe?

    @Update
    fun update(recipe: Recipe)

}
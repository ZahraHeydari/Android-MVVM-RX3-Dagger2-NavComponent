package com.zest.android.data.source.remote

import com.zest.android.data.model.CategoryResponse
import com.zest.android.data.model.RecipeResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * All Api services must specific in this interface
 *
 * @Author ZARA.
 */
interface APIService {

    @GET("random.php")
    fun getRecipes(@Query("s") randomChar: String): Observable<RecipeResponse>

    @get:GET("categories.php")
    val categories: Observable<CategoryResponse>

    @GET("lookup.php")
    fun getRecipeDetailById(@Query("i") recipeId: String): Observable<RecipeResponse>

    @GET("search.php")
    fun search(@Query("s") query: String): Observable<RecipeResponse>
}


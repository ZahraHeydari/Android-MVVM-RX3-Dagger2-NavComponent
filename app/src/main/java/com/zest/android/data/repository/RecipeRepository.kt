package com.zest.android.data.repository

import com.zest.android.data.model.Recipe
import com.zest.android.data.model.RecipeResponse
import com.zest.android.data.source.local.AppDatabase
import com.zest.android.data.source.remote.APIResponse
import com.zest.android.data.source.remote.APIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 * @Author ZARA.
 */
class RecipeRepository @Inject constructor(
    private var apiService: APIService,
    private var appDatabase: AppDatabase
) {
    fun insertFavorite(recipe: Recipe) {
        appDatabase.recipeDao.insert(recipe)
    }

    fun loadAllFavorites(): MutableList<Recipe> {
        return appDatabase.recipeDao.loadAll()
    }

    fun getFavoriteByRecipeId(recipe: Recipe): Recipe? {
        return appDatabase.recipeDao.loadOneByRecipeId(recipe.recipeId)
    }

    fun deleteFavorite(recipe: Recipe) {
        appDatabase.recipeDao.delete(recipe)
    }

    fun getRecipes(
        compositeDisposable: CompositeDisposable,
        input: String,
        fromSearch: Boolean,
        onResponse: APIResponse<RecipeResponse>
    ): Disposable {
        val result = RecipeResponse()
        return apiService.run {
            if (fromSearch) this.search(input) else this.getRecipes(input)
        }.run {
            this.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeat(10)
                .doOnNext {
                    result.recipes.add(it.recipes.first())
                }
                .subscribe({
                    onResponse.onSuccess(result)
                }, {
                    onResponse.onError(it)
                }).also {
                    compositeDisposable.add(it)
                }
        }
    }
}

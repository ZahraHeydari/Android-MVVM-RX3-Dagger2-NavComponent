package com.zest.android.ui.recipes

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zest.android.data.model.Recipe
import com.zest.android.data.model.RecipeResponse
import com.zest.android.data.repository.RecipeRepository
import com.zest.android.data.source.remote.APIResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class RecipeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) :
    ViewModel() {

    val recipesData = MutableLiveData<List<Recipe>?>()
    val isLoading = MutableLiveData(false)
    private val compositeDisposable = CompositeDisposable()

    private fun generateRandomChar(): Char {
        return ('a' + (Math.random() * ('z' - 'a' + 1)).toInt())
    }

    fun getMainRecipes() {
        fetchRecipesData(generateRandomChar().toString(), false)
    }

    fun search(query: String) {
        fetchRecipesData(query, true)
    }

    private fun fetchRecipesData(query: String, fromSearch: Boolean) {
        setLoading(true)
        recipeRepository.getRecipes(
            compositeDisposable,
            query,
            fromSearch,
            object : APIResponse<RecipeResponse> {
                override fun onSuccess(result: RecipeResponse?) {
                    Log.d("RecipeResponse", "onSuccess() called with: result = $result")
                    setLoading(false)
                    recipesData.value = result?.recipes
                }

                override fun onError(t: Throwable) {
                    setLoading(false)
                    t.printStackTrace()
                }
            })
    }

    fun loadFavoriteItems() {
        val recipes: MutableList<Recipe> = recipeRepository.loadAllFavorites()
        recipesData.value = recipes
    }

    fun loadFavorite(recipe: Recipe): Recipe? {
        return recipeRepository.getFavoriteByRecipeId(recipe)
    }

    fun isFavorited(recipe: Recipe): Boolean {
        return loadFavorite(recipe) != null
    }

    fun deleteFavoriteFromDB(recipe: Recipe) {
        recipeRepository.deleteFavorite(recipe)
    }

    fun addOrRemoveAsFavorite(recipe: Recipe) {
        if (loadFavorite(recipe) == null) recipeRepository.insertFavorite(recipe)
        else recipeRepository.deleteFavorite(recipe)
    }

    fun setLoading(isVisible: Boolean) {
        this.isLoading.value = isVisible
    }

    fun loadTags(recipe: Recipe): Array<String>? {
        val tags = recipe.tag
        if (TextUtils.isEmpty(tags)) {
            return null
        }
        return tags?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
    }

    fun dispose() {
        if (compositeDisposable.isDisposed) compositeDisposable.dispose()
    }
}

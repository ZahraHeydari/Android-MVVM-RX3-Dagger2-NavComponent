package com.zest.android.di.component

import com.zest.android.di.ActivityScope
import com.zest.android.ui.category.CategoryFragment
import com.zest.android.ui.favorite.FavoriteFragment
import com.zest.android.ui.main.MainActivity
import com.zest.android.ui.recipes.RecipesFragment
import com.zest.android.ui.search.SearchActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(searchActivity: SearchActivity)
    fun inject(recipesFragment: RecipesFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(categoryFragment: CategoryFragment)
}
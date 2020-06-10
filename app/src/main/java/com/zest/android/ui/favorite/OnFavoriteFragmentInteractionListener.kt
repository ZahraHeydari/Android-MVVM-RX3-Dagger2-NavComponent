package com.zest.android.ui.favorite

import com.zest.android.data.model.Recipe

/**
 * To make an interaction between [FavoriteFragment]
 *
 * Created by ZARA
 */
interface OnFavoriteFragmentInteractionListener {

    fun gotoDetailPage(recipe: Recipe)

    fun showDeleteFavoriteDialog(recipe: Recipe)

    fun isFavorited(recipe: Recipe): Boolean

}

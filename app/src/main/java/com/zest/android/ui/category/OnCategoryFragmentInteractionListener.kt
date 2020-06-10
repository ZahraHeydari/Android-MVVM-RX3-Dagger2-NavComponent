package com.zest.android.ui.category

import com.zest.android.data.model.Category

/**
 * To make an interaction between [CategoryFragment] and [CategoryViewModel]
 *
 * Created by ZARA
 */
interface OnCategoryFragmentInteractionListener {

    fun showSubCategories(category: Category)

}
package com.zest.android.ui.main

import com.zest.android.data.model.Category
import com.zest.android.data.model.Recipe

/**
 * To make interaction between [MainActivity] and its child
 *
 * Created by ZARA
 */
interface OnMainCallback {


    fun gotoDetailPage(recipe: Recipe)


    fun gotoSubCategories(category: Category)


    fun updateActionBarTitle(resId: Int)

}

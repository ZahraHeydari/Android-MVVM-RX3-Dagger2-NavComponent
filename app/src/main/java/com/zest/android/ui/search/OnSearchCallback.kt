package com.zest.android.ui.search

import com.zest.android.data.model.Recipe

interface OnSearchCallback {

    fun gotoDetailPage(recipe: Recipe)

}

package com.zest.android.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RecipeResponse(@Json(name = "meals") var recipes: MutableList<Recipe> = ArrayList()) : Parcelable
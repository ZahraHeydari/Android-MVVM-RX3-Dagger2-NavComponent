package com.zest.android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import androidx.annotation.NonNull
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@JsonClass(generateAdapter = true)
data class Recipe(@Json(name = "idMeal") @NonNull @PrimaryKey var recipeId: String = "",
                  @Json(name = "strMeal") var title: String? = null,
                  @Json(name = "strMealThumb") var image: String? = null,
                  @Json(name = "strInstructions") var instructions: String? = null,
                  @Json(name = "strTags") var tag: String? = null) : Parcelable
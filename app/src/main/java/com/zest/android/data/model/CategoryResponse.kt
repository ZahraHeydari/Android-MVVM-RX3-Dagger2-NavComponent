package com.zest.android.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zest.android.data.model.Category
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class CategoryResponse(@Json(name = "category") var categories: List<Category> = ArrayList()) : Parcelable
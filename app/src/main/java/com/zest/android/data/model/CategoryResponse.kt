package com.zest.android.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class CategoryResponse(@Json(name = "categories") var categories: List<Category> = ArrayList()) :
    Parcelable
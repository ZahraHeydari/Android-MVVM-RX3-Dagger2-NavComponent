package com.zest.android.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Category(var id: Long? = null,
                    @Json(name = "strCategory") var title: String,
                    @Json(name = "idCategory") var categoryId: String? = null,
                    @Json(name = "strCategoryThumb") var image: String? = null,
                    @Json(name = "strCategoryDescription") var description: String? = null) : Parcelable {

}
package com.myshopee.app.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon_name") val iconName: String? = "ic_category",
    @SerializedName("image_url") val imageUrl: String? = ""
)

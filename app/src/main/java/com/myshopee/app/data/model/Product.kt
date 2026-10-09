package com.myshopee.app.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("rating") val rating: Double = 4.8,
    @SerializedName("sales_count") val salesCount: Int = 0,
    @SerializedName("stock") val stock: Int = 10,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("is_popular") val isPopular: Boolean = false,
    @SerializedName("is_recommended") val isRecommended: Boolean = true
) : Serializable

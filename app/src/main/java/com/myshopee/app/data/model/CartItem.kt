package com.myshopee.app.data.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("is_selected") var isSelected: Boolean = true,
    @SerializedName("products") val product: Product? = null
)

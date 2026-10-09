package com.myshopee.app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String? = "",
    @SerializedName("address") val address: String? = "Kota Cirebon, Jawa Barat",
    @SerializedName("avatar_url") val avatarUrl: String? = ""
)

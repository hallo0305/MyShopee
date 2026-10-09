package com.myshopee.app.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("id") val id: String,
    @SerializedName("order_number") val orderNumber: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("recipient_name") val recipientName: String,
    @SerializedName("recipient_phone") val recipientPhone: String,
    @SerializedName("shipping_address") val shippingAddress: String,
    @SerializedName("shipping_courier") val shippingCourier: String = "Reguler (2-3 hari)",
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("shipping_fee") val shippingFee: Double = 10000.0,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("virtual_account") val virtualAccount: String = "1234567890",
    @SerializedName("payment_status") val paymentStatus: String = "Menunggu Pembayaran",
    @SerializedName("created_at") val createdAt: String? = null
) : Serializable

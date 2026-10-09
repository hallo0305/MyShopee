package com.myshopee.app.data.repository

import com.myshopee.app.data.model.Order
import java.util.UUID

object OrderRepository {
    private val ordersList = mutableListOf<Order>()

    fun createOrder(
        userId: String,
        recipientName: String,
        recipientPhone: String,
        address: String,
        courier: String,
        subtotal: Double,
        shippingFee: Double
    ): Order {
        val orderNumber = "MSP-" + (10000..99999).random()
        val order = Order(
            id = UUID.randomUUID().toString(),
            orderNumber = orderNumber,
            userId = userId,
            recipientName = recipientName,
            recipientPhone = recipientPhone,
            shippingAddress = address,
            shippingCourier = courier,
            subtotal = subtotal,
            shippingFee = shippingFee,
            totalAmount = subtotal + shippingFee,
            virtualAccount = "1234567890",
            paymentStatus = "Menunggu Pembayaran"
        )
        ordersList.add(0, order)
        return order
    }

    fun getOrders(): List<Order> = ordersList.toList()

    fun updatePaymentStatus(orderId: String, newStatus: String): Boolean {
        val index = ordersList.indexOfFirst { it.id == orderId }
        if (index != -1) {
            val old = ordersList[index]
            ordersList[index] = old.copy(paymentStatus = newStatus)
            return true
        }
        return false
    }

    fun getOrderById(orderId: String): Order? {
        return ordersList.find { it.id == orderId }
    }
}

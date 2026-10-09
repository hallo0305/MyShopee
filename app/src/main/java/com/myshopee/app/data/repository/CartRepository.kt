package com.myshopee.app.data.repository

import com.myshopee.app.data.model.CartItem
import com.myshopee.app.data.model.Product

object CartRepository {
    private val inMemoryCart = mutableListOf<CartItem>()

    fun getCartItems(): List<CartItem> = inMemoryCart.toList()

    fun addToCart(product: Product, quantity: Int = 1) {
        val existing = inMemoryCart.find { it.productId == product.id }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            inMemoryCart.add(
                CartItem(
                    id = "cart-${System.currentTimeMillis()}",
                    userId = "diva-demo-id",
                    productId = product.id,
                    quantity = quantity,
                    isSelected = true,
                    product = product
                )
            )
        }
    }

    fun updateQuantity(cartItemId: String, newQty: Int) {
        val item = inMemoryCart.find { it.id == cartItemId }
        if (item != null) {
            if (newQty <= 0) {
                inMemoryCart.remove(item)
            } else {
                item.quantity = newQty
            }
        }
    }

    fun removeCartItem(cartItemId: String) {
        inMemoryCart.removeAll { it.id == cartItemId }
    }

    fun toggleSelection(cartItemId: String) {
        val item = inMemoryCart.find { it.id == cartItemId }
        if (item != null) {
            item.isSelected = !item.isSelected
        }
    }

    fun calculateSubtotal(): Double {
        return inMemoryCart.filter { it.isSelected }
            .sumOf { (it.product?.price ?: 0.0) * it.quantity }
    }

    fun clearCart() {
        inMemoryCart.clear()
    }
}

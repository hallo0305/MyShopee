package com.myshopee.app

import com.myshopee.app.data.model.Product
import com.myshopee.app.data.repository.CartRepository
import com.myshopee.app.data.repository.OrderRepository
import com.myshopee.app.data.repository.ProductRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MyShopeeRepositoryTest {

    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        productRepository = ProductRepository()
        CartRepository.clearCart()
    }

    @Test
    fun testGetCategoriesNotEmpty() = runBlocking {
        val categories = productRepository.getCategories()
        assertTrue("Categories should not be empty", categories.isNotEmpty())
        assertEquals(6, categories.size)
    }

    @Test
    fun testGetProductsAndFilter() = runBlocking {
        val allProducts = productRepository.getProducts()
        assertTrue("Products should not be empty", allProducts.isNotEmpty())

        val fashionProducts = productRepository.getProducts(categoryName = "Fashion")
        assertTrue("Fashion products should be found", fashionProducts.isNotEmpty())
        assertTrue(fashionProducts.all { it.categoryId == "cat-1" })

        val searchProducts = productRepository.getProducts(query = "Cardigan")
        assertEquals(1, searchProducts.size)
        assertTrue(searchProducts[0].name.contains("Cardigan"))
    }

    @Test
    fun testCartOperations() {
        val sampleProduct = Product(
            id = "test-prod-1",
            categoryId = "cat-1",
            name = "Test Cardigan",
            description = "Soft cardigan",
            price = 100000.0,
            imageUrl = "http://example.com/test.jpg"
        )

        CartRepository.addToCart(sampleProduct, quantity = 2)
        val items = CartRepository.getCartItems()
        assertEquals(1, items.size)
        assertEquals(2, items[0].quantity)
        assertEquals(200000.0, CartRepository.calculateSubtotal(), 0.01)

        // Update quantity
        CartRepository.updateQuantity(items[0].id, 3)
        assertEquals(300000.0, CartRepository.calculateSubtotal(), 0.01)

        // Clear cart
        CartRepository.clearCart()
        assertEquals(0, CartRepository.getCartItems().size)
        assertEquals(0.0, CartRepository.calculateSubtotal(), 0.01)
    }

    @Test
    fun testOrderCreationAndStatusUpdate() {
        val order = OrderRepository.createOrder(
            userId = "user-123",
            recipientName = "Diva",
            recipientPhone = "081234567890",
            address = "Kota Cirebon",
            courier = "Reguler (2-3 hari)",
            subtotal = 150000.0,
            shippingFee = 10000.0
        )

        assertNotNull(order.id)
        assertTrue(order.orderNumber.startsWith("MSP-"))
        assertEquals("Menunggu Pembayaran", order.paymentStatus)
        assertEquals(160000.0, order.totalAmount, 0.01)

        val updated = OrderRepository.updatePaymentStatus(order.id, "Berhasil Dibayar")
        assertTrue(updated)

        val retrieved = OrderRepository.getOrderById(order.id)
        assertNotNull(retrieved)
        assertEquals("Berhasil Dibayar", retrieved?.paymentStatus)
    }
}

package com.myshopee.app.data.remote

import com.myshopee.app.data.model.CartItem
import com.myshopee.app.data.model.Category
import com.myshopee.app.data.model.Order
import com.myshopee.app.data.model.Product
import com.myshopee.app.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApiService {

    // Fetch All Categories
    @GET("rest/v1/categories?select=*")
    suspend fun getCategories(
        @Header("apikey") apiKey: String
    ): Response<List<Category>>

    // Fetch Products
    @GET("rest/v1/products?select=*")
    suspend fun getProducts(
        @Header("apikey") apiKey: String,
        @Query("category_id") categoryId: String? = null
    ): Response<List<Product>>

    // Fetch Single Product
    @GET("rest/v1/products?select=*")
    suspend fun getProductById(
        @Header("apikey") apiKey: String,
        @Query("id") id: String
    ): Response<List<Product>>

    // Fetch User Cart
    @GET("rest/v1/cart_items?select=*,products(*)")
    suspend fun getCartItems(
        @Header("apikey") apiKey: String,
        @Query("user_id") userId: String
    ): Response<List<CartItem>>

    // Add to Cart
    @POST("rest/v1/cart_items")
    suspend fun addToCart(
        @Header("apikey") apiKey: String,
        @Body cartItem: CartItem
    ): Response<Unit>

    // Update Cart Quantity
    @PATCH("rest/v1/cart_items")
    suspend fun updateCartQuantity(
        @Header("apikey") apiKey: String,
        @Query("id") eqId: String,
        @Body updates: Map<String, Int>
    ): Response<Unit>

    // Delete Cart Item
    @DELETE("rest/v1/cart_items")
    suspend fun deleteCartItem(
        @Header("apikey") apiKey: String,
        @Query("id") eqId: String
    ): Response<Unit>

    // Create Order
    @POST("rest/v1/orders")
    suspend fun createOrder(
        @Header("apikey") apiKey: String,
        @Body order: Order
    ): Response<Unit>

    // Get User Orders
    @GET("rest/v1/orders?select=*")
    suspend fun getUserOrders(
        @Header("apikey") apiKey: String,
        @Query("user_id") userId: String
    ): Response<List<Order>>

    // Get Order Status
    @GET("rest/v1/orders?select=*")
    suspend fun getOrderById(
        @Header("apikey") apiKey: String,
        @Query("id") orderId: String
    ): Response<List<Order>>
}

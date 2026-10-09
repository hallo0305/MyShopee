package com.myshopee.app.data.repository

import com.myshopee.app.data.model.Category
import com.myshopee.app.data.model.Product
import com.myshopee.app.data.remote.RetrofitClient

class ProductRepository {

    private val dummyCategories = listOf(
        Category("cat-1", "Fashion", "ic_fashion"),
        Category("cat-2", "Aksesori", "ic_accessory"),
        Category("cat-3", "Elektronik", "ic_electronic"),
        Category("cat-4", "Tas", "ic_bag"),
        Category("cat-5", "Sepatu", "ic_shoes"),
        Category("cat-6", "Kecantikan", "ic_beauty")
    )

    private val dummyProducts = listOf(
        Product(
            id = "p1",
            categoryId = "cat-1",
            name = "Pastel Cardigan Over-sized Diva",
            description = "Cardigan rajut super lembut dengan nuansa warna soft pink pastel. Perfect for aesthetic daily outfits.",
            price = 149000.0,
            rating = 4.9,
            salesCount = 128,
            stock = 25,
            imageUrl = "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=500",
            isPopular = true,
            isRecommended = true
        ),
        Product(
            id = "p2",
            categoryId = "cat-6",
            name = "Lip Gloss Soft Pink Glow Diva",
            description = "Formula lip gloss tahan lama dengan efek plumping glossy alami dan kelembapan seharian.",
            price = 65000.0,
            rating = 4.8,
            salesCount = 350,
            stock = 40,
            imageUrl = "https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=500",
            isPopular = true,
            isRecommended = true
        ),
        Product(
            id = "p3",
            categoryId = "cat-4",
            name = "Cute Mini Backpack Pastel Pink",
            description = "Tas punggung mini imut bahan vegan leather kualitas tinggi dengan gantungan pita manis.",
            price = 185000.0,
            rating = 4.9,
            salesCount = 94,
            stock = 15,
            imageUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500",
            isPopular = true,
            isRecommended = true
        ),
        Product(
            id = "p4",
            categoryId = "cat-5",
            name = "Pastel Pink Sneakers Aesthetic",
            description = "Sepatu kets bergaya Korea warna pink lembut yang empuk dan sangat ringan.",
            price = 245000.0,
            rating = 4.7,
            salesCount = 82,
            stock = 12,
            imageUrl = "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=500",
            isPopular = false,
            isRecommended = true
        ),
        Product(
            id = "p5",
            categoryId = "cat-2",
            name = "Kalung Heart Crystal Pink",
            description = "Kalung liontin hati terbuat dari kristal berkilau anti karat yang anggun dan elegan.",
            price = 89000.0,
            rating = 5.0,
            salesCount = 210,
            stock = 30,
            imageUrl = "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=500",
            isPopular = true,
            isRecommended = true
        ),
        Product(
            id = "p6",
            categoryId = "cat-3",
            name = "Headphone Wireless Rose Gold Cute",
            description = "Headphone bluetooth nirkabel berdesain elegan rose gold dengan peredam bising dan suara HD.",
            price = 320000.0,
            rating = 4.8,
            salesCount = 65,
            stock = 8,
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500",
            isPopular = false,
            isRecommended = true
        )
    )

    suspend fun getCategories(): List<Category> {
        return try {
            val response = RetrofitClient.apiService.getCategories(RetrofitClient.SUPABASE_ANON_KEY)
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!
            } else dummyCategories
        } catch (e: Exception) {
            dummyCategories
        }
    }

    suspend fun getProducts(categoryName: String? = null, query: String? = null): List<Product> {
        var result = dummyProducts
        if (!categoryName.isNullOrEmpty() && categoryName != "All" && categoryName != "Semua") {
            val category = dummyCategories.find { it.name.equals(categoryName, ignoreCase = true) }
            if (category != null) {
                result = result.filter { it.categoryId == category.id }
            }
        }
        if (!query.isNullOrBlank()) {
            result = result.filter { 
                it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) 
            }
        }
        return result
    }

    suspend fun getProductById(id: String): Product? {
        return dummyProducts.find { it.id == id }
    }
}

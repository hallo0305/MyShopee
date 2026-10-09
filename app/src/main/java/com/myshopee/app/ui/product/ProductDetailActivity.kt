package com.myshopee.app.ui.product

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.myshopee.app.data.model.Product
import com.myshopee.app.data.repository.CartRepository
import com.myshopee.app.data.repository.ProductRepository
import com.myshopee.app.databinding.ActivityProductDetailBinding
import com.myshopee.app.ui.checkout.CheckoutActivity
import com.myshopee.app.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val productRepository = ProductRepository()
    private var currentProduct: Product? = null
    private var selectedQuantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prodId = intent.getStringExtra("PRODUCT_ID") ?: "p1"

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDetailCart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("OPEN_TAB", "CART")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }

        binding.btnQtyMinus.setOnClickListener {
            if (selectedQuantity > 1) {
                selectedQuantity--
                updateQuantityDisplay()
            }
        }

        binding.btnQtyPlus.setOnClickListener {
            val stock = currentProduct?.stock ?: 10
            if (selectedQuantity < stock) {
                selectedQuantity++
                updateQuantityDisplay()
            } else {
                Toast.makeText(this, "Jumlah mencapai batas stok yang tersedia ($stock)", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val product = productRepository.getProductById(prodId)
            withContext(Dispatchers.Main) {
                if (product != null) {
                    currentProduct = product
                    binding.tvDetailName.text = product.name

                    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
                        maximumFractionDigits = 0
                    }
                    binding.tvDetailPrice.text = format.format(product.price)
                    binding.tvDetailStock.text = "Stok: ${product.stock}"
                    binding.tvDetailRating.text = "⭐ ${product.rating} (${product.salesCount} Terjual)"
                    binding.tvDetailDesc.text = product.description

                    Glide.with(this@ProductDetailActivity)
                        .load(product.imageUrl)
                        .centerCrop()
                        .into(binding.ivDetailImage)
                }
            }
        }

        binding.btnAddToCart.setOnClickListener {
            currentProduct?.let { p ->
                CartRepository.addToCart(p, selectedQuantity)
                Toast.makeText(this, "\"${p.name}\" ($selectedQuantity item) berhasil ditambahkan ke keranjang! 💗", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBuyNow.setOnClickListener {
            currentProduct?.let { p ->
                // Add to cart and ensure it's selected, then proceed to checkout
                CartRepository.addToCart(p, selectedQuantity)
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateQuantityDisplay() {
        binding.tvQtyCount.text = selectedQuantity.toString()
    }
}

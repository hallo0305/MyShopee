package com.myshopee.app.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.myshopee.app.data.repository.ProductRepository
import com.myshopee.app.databinding.ActivityProductListBinding
import com.myshopee.app.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private val productRepository = ProductRepository()
    private lateinit var adapter: ProductAdapter
    private var currentCategory: String = "Semua"
    private var currentQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentCategory = intent.getStringExtra("CATEGORY_NAME") ?: "Semua"
        currentQuery = intent.getStringExtra("SEARCH_QUERY") ?: ""

        val title = if (currentQuery.isNotEmpty()) {
            "Pencarian: \"$currentQuery\""
        } else {
            "Kategori: $currentCategory 💗"
        }
        binding.tvCategoryTitle.text = title

        if (currentQuery.isNotEmpty()) {
            binding.etSearchInList.setText(currentQuery)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnListCart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("OPEN_TAB", "CART")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }

        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this@ProductListActivity, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
            }
            startActivity(intent)
        }

        binding.rvProductsGrid.layoutManager = GridLayoutManager(this, 2)
        binding.rvProductsGrid.adapter = adapter

        binding.etSearchInList.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                currentQuery = binding.etSearchInList.text.toString().trim()
                filterAndLoadData()
                true
            } else {
                false
            }
        }

        filterAndLoadData()
    }

    private fun filterAndLoadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = productRepository.getProducts(
                categoryName = if (currentCategory == "Semua" || currentCategory == "All") null else currentCategory,
                query = if (currentQuery.isBlank()) null else currentQuery
            )
            withContext(Dispatchers.Main) {
                adapter.updateData(list)
                if (list.isEmpty()) {
                    binding.rvProductsGrid.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                } else {
                    binding.rvProductsGrid.visibility = View.VISIBLE
                    binding.layoutEmptyState.visibility = View.GONE
                }
            }
        }
    }
}

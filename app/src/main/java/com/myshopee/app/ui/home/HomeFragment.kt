package com.myshopee.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.myshopee.app.data.local.SessionManager
import com.myshopee.app.data.repository.ProductRepository
import com.myshopee.app.databinding.FragmentHomeBinding
import com.myshopee.app.ui.category.CategoryAdapter
import com.myshopee.app.ui.product.ProductAdapter
import com.myshopee.app.ui.product.ProductDetailActivity
import com.myshopee.app.ui.product.ProductListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val productRepository = ProductRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())
        val name = sessionManager.getUserName()
        binding.tvUserGreeting.text = "Hai, $name! 💗"

        binding.btnHomeCart.setOnClickListener {
            (activity as? com.myshopee.app.ui.main.MainActivity)?.navigateToTab(com.myshopee.app.R.id.nav_cart)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    val intent = Intent(context, ProductListActivity::class.java).apply {
                        putExtra("SEARCH_QUERY", query)
                        putExtra("CATEGORY_NAME", "Semua")
                    }
                    startActivity(intent)
                }
                true
            } else {
                false
            }
        }

        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val categories = productRepository.getCategories()
            val products = productRepository.getProducts()

            withContext(Dispatchers.Main) {
                _binding?.let { b ->
                    // Categories horizontal list
                    b.rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    b.rvCategories.adapter = CategoryAdapter(categories) { cat ->
                        val intent = Intent(context, ProductListActivity::class.java).apply {
                            putExtra("CATEGORY_NAME", cat.name)
                        }
                        startActivity(intent)
                    }

                    // Popular products grid 2 columns
                    b.rvPopularProducts.layoutManager = GridLayoutManager(context, 2)
                    b.rvPopularProducts.adapter = ProductAdapter(products.filter { it.isPopular }) { prod ->
                        val intent = Intent(context, ProductDetailActivity::class.java).apply {
                            putExtra("PRODUCT_ID", prod.id)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

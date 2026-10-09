package com.myshopee.app.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.myshopee.app.data.repository.ProductRepository
import com.myshopee.app.databinding.FragmentCategoryBinding
import com.myshopee.app.ui.product.ProductListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val productRepository = ProductRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val categories = productRepository.getCategories()
            withContext(Dispatchers.Main) {
                _binding?.let { b ->
                    b.rvAllCategories.layoutManager = LinearLayoutManager(context)
                    b.rvAllCategories.adapter = CategoryAdapter(categories, isCardLayout = true) { cat ->
                        val intent = Intent(context, ProductListActivity::class.java).apply {
                            putExtra("CATEGORY_NAME", cat.name)
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

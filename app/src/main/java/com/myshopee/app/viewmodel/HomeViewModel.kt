package com.myshopee.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myshopee.app.data.model.Category
import com.myshopee.app.data.model.Product
import com.myshopee.app.data.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository = ProductRepository()) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _popularProducts = MutableLiveData<List<Product>>()
    val popularProducts: LiveData<List<Product>> = _popularProducts

    private val _recommendedProducts = MutableLiveData<List<Product>>()
    val recommendedProducts: LiveData<List<Product>> = _recommendedProducts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadHomeData() {
        _isLoading.value = true
        viewModelScope.launch {
            val cats = productRepository.getCategories()
            _categories.value = cats

            val prods = productRepository.getProducts()
            _popularProducts.value = prods.filter { it.isPopular }
            _recommendedProducts.value = prods.filter { it.isRecommended }
            _isLoading.value = false
        }
    }
}

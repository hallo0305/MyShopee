package com.myshopee.app.ui.cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.myshopee.app.R
import com.myshopee.app.data.model.CartItem
import com.myshopee.app.data.repository.CartRepository
import com.myshopee.app.databinding.FragmentCartBinding
import com.myshopee.app.ui.checkout.CheckoutActivity
import com.myshopee.app.ui.main.MainActivity
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCartItems.layoutManager = LinearLayoutManager(context)
        adapter = CartAdapter(
            cartItems = CartRepository.getCartItems(),
            onQuantityChanged = { item, newQty ->
                if (newQty <= 0) {
                    confirmRemoveItem(item)
                } else {
                    CartRepository.updateQuantity(item.id, newQty)
                    refreshCart()
                }
            },
            onSelectionToggled = { item ->
                CartRepository.toggleSelection(item.id)
                refreshCart()
            },
            onItemDeleted = { item ->
                confirmRemoveItem(item)
            }
        )
        binding.rvCartItems.adapter = adapter

        binding.btnClearCart.setOnClickListener {
            val items = CartRepository.getCartItems()
            if (items.isEmpty()) {
                Toast.makeText(context, "Keranjang sudah kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Kosongkan Keranjang")
                .setMessage("Apakah Anda yakin ingin menghapus semua barang dari keranjang belanja?")
                .setPositiveButton("Kosongkan") { _, _ ->
                    CartRepository.clearCart()
                    refreshCart()
                    Toast.makeText(context, "Keranjang berhasil dikosongkan", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        binding.btnShopNow.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.nav_home)
        }

        binding.btnCheckout.setOnClickListener {
            val selectedItems = CartRepository.getCartItems().filter { it.isSelected }
            if (selectedItems.isEmpty()) {
                Toast.makeText(context, "Pilih minimal satu barang untuk melanjutkan checkout!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(context, CheckoutActivity::class.java))
            }
        }

        refreshCart()
    }

    private fun confirmRemoveItem(item: CartItem) {
        val itemName = item.product?.name ?: "Barang"
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Barang")
            .setMessage("Apakah Anda ingin menghapus \"$itemName\" dari keranjang?")
            .setPositiveButton("Hapus") { _, _ ->
                CartRepository.removeCartItem(item.id)
                refreshCart()
                Toast.makeText(context, "Barang dihapus dari keranjang", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    private fun refreshCart() {
        _binding?.let { b ->
            val items = CartRepository.getCartItems()
            adapter.updateData(items)

            if (items.isEmpty()) {
                b.layoutCartEmpty.visibility = View.VISIBLE
                b.rvCartItems.visibility = View.GONE
                b.bottomCheckoutBar.visibility = View.GONE
                b.btnClearCart.visibility = View.GONE
            } else {
                b.layoutCartEmpty.visibility = View.GONE
                b.rvCartItems.visibility = View.VISIBLE
                b.bottomCheckoutBar.visibility = View.VISIBLE
                b.btnClearCart.visibility = View.VISIBLE
            }

            val subtotal = CartRepository.calculateSubtotal()
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
                maximumFractionDigits = 0
            }
            b.tvCartSubtotal.text = format.format(subtotal)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

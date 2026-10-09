package com.myshopee.app.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myshopee.app.data.model.CartItem
import com.myshopee.app.databinding.ItemCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onSelectionToggled: (CartItem) -> Unit,
    private val onItemDeleted: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        val product = item.product

        holder.binding.tvCartProductName.text = product?.name ?: "Produk My Shopee"

        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
        holder.binding.tvCartProductPrice.text = format.format(product?.price ?: 0.0)

        holder.binding.tvQuantity.text = item.quantity.toString()
        holder.binding.cbSelect.isChecked = item.isSelected

        Glide.with(holder.itemView.context)
            .load(product?.imageUrl)
            .centerCrop()
            .into(holder.binding.ivCartImage)

        holder.binding.btnPlus.setOnClickListener {
            val maxStock = product?.stock ?: 99
            if (item.quantity < maxStock) {
                onQuantityChanged(item, item.quantity + 1)
            }
        }

        holder.binding.btnMinus.setOnClickListener {
            onQuantityChanged(item, item.quantity - 1)
        }

        holder.binding.btnDelete.setOnClickListener {
            onItemDeleted(item)
        }

        holder.binding.cbSelect.setOnClickListener {
            onSelectionToggled(item)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newItems: List<CartItem>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}

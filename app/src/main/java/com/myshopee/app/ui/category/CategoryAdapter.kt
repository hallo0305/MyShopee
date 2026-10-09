package com.myshopee.app.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myshopee.app.data.model.Category
import com.myshopee.app.databinding.ItemCategoryBinding
import com.myshopee.app.databinding.ItemCategoryCardBinding

class CategoryAdapter(
    private val categories: List<Category>,
    private val isCardLayout: Boolean = false,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ChipViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
    inner class CardViewHolder(val binding: ItemCategoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (isCardLayout) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding = ItemCategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CardViewHolder(binding)
        } else {
            val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChipViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cat = categories[position]
        if (holder is CardViewHolder) {
            holder.binding.tvCategoryName.text = cat.name
            holder.itemView.setOnClickListener { onItemClick(cat) }
        } else if (holder is ChipViewHolder) {
            holder.binding.tvCategoryName.text = cat.name
            holder.itemView.setOnClickListener { onItemClick(cat) }
        }
    }

    override fun getItemCount(): Int = categories.size
}

package com.myshopee.app.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.myshopee.app.R
import com.myshopee.app.data.model.Order
import com.myshopee.app.databinding.ItemOrderBinding
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(
    private var orders: List<Order>,
    private val onItemClick: (Order) -> Unit,
    private val onActionClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        val context = holder.itemView.context

        holder.binding.tvOrderNum.text = "No. Pesanan: ${order.orderNumber}"
        holder.binding.tvOrderStatus.text = order.paymentStatus
        holder.binding.tvOrderRecipient.text = "Penerima: ${order.recipientName} (${order.recipientPhone})"
        holder.binding.tvOrderCourier.text = "Pengiriman: ${order.shippingCourier}"

        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
        holder.binding.tvOrderTotal.text = format.format(order.totalAmount)

        // Status badge color & Action button setup
        when (order.paymentStatus) {
            "Menunggu Pembayaran" -> {
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending))
                holder.binding.btnOrderAction.visibility = View.VISIBLE
                holder.binding.btnOrderAction.text = "Bayar Sekarang"
            }
            "Berhasil Dibayar" -> {
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_paid))
                holder.binding.btnOrderAction.visibility = View.VISIBLE
                holder.binding.btnOrderAction.text = "Lihat Detail"
            }
            "Diproses" -> {
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_processing))
                holder.binding.btnOrderAction.visibility = View.VISIBLE
                holder.binding.btnOrderAction.text = "Lacak Paket"
            }
            else -> {
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_completed))
                holder.binding.btnOrderAction.visibility = View.GONE
            }
        }

        holder.binding.btnOrderAction.setOnClickListener {
            onActionClick(order)
        }

        holder.itemView.setOnClickListener {
            onItemClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

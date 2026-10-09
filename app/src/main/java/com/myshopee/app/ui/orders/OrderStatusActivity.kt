package com.myshopee.app.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.myshopee.app.data.repository.OrderRepository
import com.myshopee.app.databinding.ActivityOrderStatusBinding
import com.myshopee.app.ui.payment.PaymentActivity

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderStatusBinding
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rvOrdersList.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(
            orders = emptyList(),
            onItemClick = { order ->
                Toast.makeText(this, "Pesanan #${order.orderNumber} saat ini berstatus: ${order.paymentStatus}", Toast.LENGTH_SHORT).show()
            },
            onActionClick = { order ->
                if (order.paymentStatus == "Menunggu Pembayaran") {
                    val intent = Intent(this, PaymentActivity::class.java).apply {
                        putExtra("ORDER_ID", order.id)
                    }
                    startActivity(intent)
                } else if (order.paymentStatus == "Berhasil Dibayar") {
                    Toast.makeText(this, "Pesanan telah dikonfirmasi dan sedang disiapkan penjual", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Paket sedang dikirim oleh kurir ${order.shippingCourier} ke alamat penerima", Toast.LENGTH_LONG).show()
                }
            }
        )
        binding.rvOrdersList.adapter = adapter

        loadOrders()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        val orders = OrderRepository.getOrders()
        adapter.updateData(orders)

        if (orders.isEmpty()) {
            binding.layoutOrdersEmpty.visibility = View.VISIBLE
            binding.rvOrdersList.visibility = View.GONE
        } else {
            binding.layoutOrdersEmpty.visibility = View.GONE
            binding.rvOrdersList.visibility = View.VISIBLE
        }
    }
}

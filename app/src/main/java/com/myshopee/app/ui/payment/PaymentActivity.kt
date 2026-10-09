package com.myshopee.app.ui.payment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myshopee.app.data.repository.OrderRepository
import com.myshopee.app.databinding.ActivityPaymentBinding
import com.myshopee.app.ui.orders.OrderStatusActivity
import java.text.NumberFormat
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getStringExtra("ORDER_ID")
        val order = orderId?.let { OrderRepository.getOrderById(it) }

        if (order != null) {
            binding.tvOrderNumberDisplay.text = "No. Pesanan: ${order.orderNumber}"
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
            binding.tvTotalPayDisplay.text = "Total: ${format.format(order.totalAmount)}"
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCopyVa.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("VA Number", "1234567890")
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Nomor Virtual Account 1234567890 berhasil disalin ke papan klip!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSimulatePay.setOnClickListener {
            orderId?.let { id ->
                OrderRepository.updatePaymentStatus(id, "Berhasil Dibayar")
            }
            Toast.makeText(this, "Pembayaran Simulasi Berhasil Dikonfirmasi! Pesanan segera diproses 💗", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, OrderStatusActivity::class.java))
            finish()
        }

        binding.btnCheckStatus.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
            finish()
        }
    }
}

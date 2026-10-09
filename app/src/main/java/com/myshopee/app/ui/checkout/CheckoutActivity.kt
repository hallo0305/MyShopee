package com.myshopee.app.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myshopee.app.data.local.SessionManager
import com.myshopee.app.data.repository.CartRepository
import com.myshopee.app.data.repository.OrderRepository
import com.myshopee.app.databinding.ActivityCheckoutBinding
import com.myshopee.app.ui.payment.PaymentActivity
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private var subtotal: Double = 0.0
    private var shippingFee: Double = 10000.0
    private val courierOptions = arrayOf("Reguler (2-3 hari) - Rp 10.000", "Express (1 hari) - Rp 20.000")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this)
        binding.etRecipientName.setText(sessionManager.getUserName())

        binding.btnBack.setOnClickListener {
            finish()
        }

        subtotal = CartRepository.calculateSubtotal()
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
        binding.tvCheckoutSubtotal.text = format.format(subtotal)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, courierOptions)
        binding.spCourier.adapter = spinnerAdapter

        binding.spCourier.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                shippingFee = if (position == 0) 10000.0 else 20000.0
                updateTotal()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnPlaceOrder.setOnClickListener {
            val name = binding.etRecipientName.text.toString().trim()
            val phone = binding.etRecipientPhone.text.toString().trim()
            val addr = binding.etAddress.text.toString().trim()

            if (name.isBlank() || phone.isBlank() || addr.isBlank()) {
                Toast.makeText(this, "Mohon lengkapi data pengiriman!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val courier = courierOptions[binding.spCourier.selectedItemPosition]
            val newOrder = OrderRepository.createOrder(
                userId = sessionManager.getUserId(),
                recipientName = name,
                recipientPhone = phone,
                address = addr,
                courier = courier,
                subtotal = subtotal,
                shippingFee = shippingFee
            )

            CartRepository.clearCart()
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("ORDER_ID", newOrder.id)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun updateTotal() {
        val total = subtotal + shippingFee
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { maximumFractionDigits = 0 }
        binding.tvCheckoutShippingFee.text = format.format(shippingFee)
        binding.tvCheckoutTotal.text = format.format(total)
    }
}

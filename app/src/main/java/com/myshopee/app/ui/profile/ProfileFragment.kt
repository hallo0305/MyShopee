package com.myshopee.app.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.myshopee.app.R
import com.myshopee.app.data.local.SessionManager
import com.myshopee.app.databinding.FragmentProfileBinding
import com.myshopee.app.ui.auth.LoginActivity
import com.myshopee.app.ui.orders.OrderStatusActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())
        refreshProfileUI(sessionManager)

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog(sessionManager)
        }

        binding.btnMyOrders.setOnClickListener {
            startActivity(Intent(context, OrderStatusActivity::class.java))
        }

        binding.btnAddress.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Alamat Pengiriman Utama")
                .setMessage("Alamat saat ini:\nJl. Kartini No. 45, Kesambi, Kota Cirebon, Jawa Barat 45134\n\nNomor Kontak: 081234567890")
                .setPositiveButton("Tutup", null)
                .show()
        }

        binding.btnHelp.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Pusat Bantuan My Shopee")
                .setMessage("Butuh bantuan seputar pesanan atau aplikasi?\n\nEmail: bantuan@myshopee.app\nWhatsApp: +62 812-3456-7890\nJam Operasional: 08.00 - 21.00 WIB")
                .setPositiveButton("Mengerti", null)
                .show()
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar dari akun My Shopee?")
                .setPositiveButton("Keluar") { _, _ ->
                    sessionManager.clearSession()
                    Toast.makeText(context, "Anda berhasil keluar dari akun", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    activity?.finishAffinity()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun refreshProfileUI(sessionManager: SessionManager) {
        binding.tvProfileName.text = sessionManager.getUserName()
        binding.tvProfileEmail.text = sessionManager.getUserEmail()
    }

    private fun showEditProfileDialog(sessionManager: SessionManager) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val etName = dialogView.findViewById<EditText>(R.id.etEditName)
        etName.setText(sessionManager.getUserName())

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etName.text.toString().trim()
                if (newName.isNotBlank()) {
                    sessionManager.saveSession(
                        userId = sessionManager.getUserId(),
                        name = newName,
                        email = sessionManager.getUserEmail(),
                        token = sessionManager.getAuthToken()
                    )
                    refreshProfileUI(sessionManager)
                    Toast.makeText(requireContext(), "Profil berhasil diperbarui! 💗", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

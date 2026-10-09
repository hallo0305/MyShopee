# My Shopee by Diva 💗 — E-Commerce Android & Web Demo

[![GitHub Pages Demo](https://img.shields.io/badge/Demo_Website-Lihat_Demo_My_Shopee-ff69b4?style=for-the-badge&logo=github)](https://USERNAME.github.io/MyShopee/)

> **Lihat Demo Web My Shopee**: [https://USERNAME.github.io/MyShopee/](https://USERNAME.github.io/MyShopee/)  
> *(Catatan: Ganti `USERNAME` dengan username GitHub Anda setelah mengaktifkan GitHub Pages dari folder `docs/` di branch `main`)*

---

## 🌸 Tentangan & Identitas Aplikasi

**My Shopee by Diva** adalah aplikasi e-commerce modern dengan estetika **cute pink pastel**, diciptakan khusus untuk memenuhi tugas UAS Mata Kuliah Pemrograman Mobile Dasar.

- **Nama Branding**: My Shopee by Diva
- **Slogan**: *"Belanja Cantik, Harga Menarik"*
- **Sapaan Beranda**: *"Hai, Diva! 💗"*
- **Application ID Android**: `com.myshopee.app`
- **Arsitektur Android**: MVVM (Model-View-ViewModel) + Retrofit + Coroutines + Material Components
- **Backend & Database**: Supabase PostgreSQL & REST API
- **Demo Website**: HTML5, CSS3 (Pastel Design System), Vanilla JavaScript (dalam folder `docs/`)

---

## 🎨 Palet Warna Visual Design System

| Warna | Hex Code | Peranan |
| :--- | :--- | :--- |
| **Pink Pastel Utama** | `#F8BBD0` | Card background, header accent |
| **Pink Muda** | `#FCE4EC` | Button background, chip background |
| **Background App** | `#FFF7FA` | Canvas background seluruh layar |
| **Pink Tombol** | `#EC6A9C` | Primary Action Button |
| **Pink Gelap** | `#AD416D` | Text highlight, active icons |
| **Putih** | `#FFFFFF` | Card surface, modal background |
| **Teks Utama** | `#3F3037` | Body text & title text |

---

## 📱 12 Layar Utama Aplikasi Android

1. **Splash Screen**: Logo My Shopee, slogan, background pastel, cek sesi login.
2. **Login Screen**: Form Email & Password, validasi input, autentikasi Supabase.
3. **Register Screen**: Form Nama Lengkap, Email, Password, Konfirmasi Password.
4. **Beranda (Home)**: Sapaan *"Hai, Diva! 💗"*, Search Bar, Banner promo, Kategori & Produk Rekomendasi.
5. **Kategori**: Filter 6 Kategori (Fashion, Aksesori, Elektronik, Tas, Sepatu, Kecantikan).
6. **Daftar Produk**: RecyclerView 2 Kolom (GridLayoutManager), pencarian, filter, dan pengurutan harga.
7. **Detail Produk**: Slider gambar, nama, harga, deskripsi, rating, varian, dan pemilih kuantitas.
8. **Keranjang Belanja**: Daftar item, kontrol kuantitas (+/-), checkbox item, subtotal otomatis.
9. **Checkout**: Nama penerima, telepon, alamat pengiriman (Kota Cirebon), opsi kurir Reguler & Express.
10. **Pembayaran**: Simulasi Virtual Account BCA `1234567890`, timer, tombol salin VA, & cek status.
11. **Status Pesanan**: Tracking status (Menunggu Pembayaran, Berhasil Dibayar, Diproses, Selesai).
12. **Profil Pengguna**: Avatar Diva, detail akun, riwayat pesanan, dan tombol Keluar.

---

## 🗄️ Database Schema & Supabase RLS

File SQL lengkap telah disediakan di `supabase_schema.sql` mencakup tabel:
- `profiles`
- `categories`
- `products`
- `product_variants`
- `cart_items`
- `orders`
- `order_items`

---

## 🚀 Langkah Menjalankan Project (Untuk Pemula)

### 1. Membuka di Android Studio
1. Buka **Android Studio**.
2. Pilih **Open** dan arahkan ke folder `MyShopee`.
3. Tunggu hingga **Gradle Sync** selesai secara otomatis.
4. Hubungkan HP Android via kabel data (USB Debugging) atau gunakan **Android Emulator**.
5. Klik tombol **Run (Shift + F10)**.

### 2. Membuat APK Debug
1. Pada menu Android Studio, pilih **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
2. Setelah proses selesai, klik **locate** pada notifikasi di sudut kanan bawah.
3. APK siap dipasang di HP Android!

### 3. Mengaktifkan GitHub Pages (Demo Website)
1. Commit & Push seluruh folder `MyShopee` ke repository GitHub bernama `MyShopee`.
2. Buka **Settings** pada repository GitHub.
3. Pilih menu **Pages** di panel kiri.
4. Pilih **Deploy from a branch**.
5. Set Branch ke `main` dan Folder ke `/docs`.
6. Klik **Save** dan tunggu 1-2 menit hingga link demo website aktif!

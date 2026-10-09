/* ========================================================
   MY SHOPEE BY DIVA 💗 — DYNAMIC INTERACTION SCRIPT (WEB DEMO)
   ======================================================== */

// Demo Products Data
const productsData = [
    {
        id: "p1",
        name: "Pastel Cardigan Over-sized Diva",
        category: "Fashion",
        price: 149000,
        rating: 4.9,
        sales: 128,
        image: "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=500",
        description: "Cardigan rajut super lembut dengan nuansa warna soft pink pastel. Cocok untuk daily look yang aesthetic dan stylish."
    },
    {
        id: "p2",
        name: "Lip Gloss Soft Pink Glow Diva",
        category: "Kecantikan",
        price: 65000,
        rating: 4.8,
        sales: 350,
        image: "https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=500",
        description: "Formula lip gloss tahan lama dengan efek plumping glossy alami. Diperkaya dengan Vitamin E untuk nutrisi bibir."
    },
    {
        id: "p3",
        name: "Cute Mini Backpack Pastel Pink",
        category: "Tas",
        price: 185000,
        rating: 4.9,
        sales: 94,
        image: "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500",
        description: "Tas punggung mini imut bahan vegan leather kualitas tinggi dengan gantungan pita pink menggemaskan."
    },
    {
        id: "p4",
        name: "Pastel Pink Sneakers Aesthetic",
        category: "Sepatu",
        price: 245000,
        rating: 4.7,
        sales: 82,
        image: "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=500",
        description: "Sneakers kasual Korea style dengan bantalan empuk super ringan, sangat cocok untuk jalan-jalan seharian."
    },
    {
        id: "p5",
        name: "Kalung Heart Crystal Pink",
        category: "Aksesori",
        price: 89000,
        rating: 5.0,
        sales: 210,
        image: "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=500",
        description: "Liontin kristal berbentuk hati dengan rantai perak anti karat yang memancarkan kilau cantik elegan."
    },
    {
        id: "p6",
        name: "Headphone Wireless Rose Gold Cute",
        category: "Elektronik",
        price: 320000,
        rating: 4.8,
        sales: 65,
        image: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500",
        description: "Headphone bluetooth nirkabel berdesain elegan rose gold dengan peredam bising pasif dan suara HD bass."
    }
];

let cart = [];
let currentCategory = "All";

// Initialize App
document.addEventListener("DOMContentLoaded", () => {
    renderProducts(productsData);
});

// Format Currency
function formatRupiah(number) {
    return new Intl.NumberFormat("id-ID", {
        style: "currency",
        currency: "IDR",
        maximumFractionDigits: 0
    }).format(number);
}

// Render Products Grid
function renderProducts(list) {
    const grid = document.getElementById("productsGrid");
    grid.innerHTML = "";

    if (list.length === 0) {
        grid.innerHTML = `<p style="grid-column: 1/-1; text-align: center; color: var(--text-secondary); padding: 30px;">Produk tidak ditemukan 💗</p>`;
        return;
    }

    list.forEach(p => {
        const card = document.createElement("div");
        card.className = "product-card";
        card.onclick = () => openProductDetail(p.id);

        card.innerHTML = `
            <img src="${p.image}" alt="${p.name}" class="product-img">
            <div class="product-info">
                <h4 class="product-name">${p.name}</h4>
                <div class="product-price">${formatRupiah(p.price)}</div>
                <div class="product-meta">
                    <span class="rating"><i class="fa-solid fa-star"></i> ${p.rating}</span>
                    <span>Terjual ${p.sales}</span>
                </div>
            </div>
        `;
        grid.appendChild(card);
    });
}

// Filter by Category
function selectCategory(category, element) {
    currentCategory = category;
    document.querySelectorAll(".category-chip").forEach(el => el.classList.remove("active"));
    element.classList.add("active");

    const titleEl = document.getElementById("productGridTitle");
    titleEl.textContent = category === "All" ? "Produk Populer 💗" : `Kategori: ${category}`;

    filterProducts();
}

// Filter Search & Category
function filterProducts() {
    const searchVal = document.getElementById("searchInput").value.toLowerCase();
    
    let filtered = productsData.filter(p => {
        const matchCategory = currentCategory === "All" || p.category === currentCategory;
        const matchSearch = p.name.toLowerCase().includes(searchVal) || p.description.toLowerCase().includes(searchVal);
        return matchCategory && matchSearch;
    });

    renderProducts(filtered);
}

// Sort Products
function sortProducts() {
    const sortVal = document.getElementById("sortSelect").value;
    let list = [...productsData];

    if (currentCategory !== "All") {
        list = list.filter(p => p.category === currentCategory);
    }

    if (sortVal === "low-high") {
        list.sort((a, b) => a.price - b.price);
    } else if (sortVal === "high-low") {
        list.sort((a, b) => b.price - a.price);
    }

    renderProducts(list);
}

// Open Detail Modal
function openProductDetail(id) {
    const p = productsData.find(item => item.id === id);
    if (!p) return;

    const detailBody = document.getElementById("detailBody");
    detailBody.innerHTML = `
        <img src="${p.image}" alt="${p.name}" class="detail-img">
        <h3 style="color: var(--pink-dark); margin-bottom: 5px;">${p.name}</h3>
        <div style="font-size: 1.3rem; font-weight: 700; color: var(--pink-button); margin-bottom: 10px;">${formatRupiah(p.price)}</div>
        <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 15px;">${p.description}</p>
        <button class="btn-primary" onclick="addToCart('${p.id}'); closeProductModal();">
            <i class="fa-solid fa-cart-plus"></i> Tambah ke Keranjang
        </button>
    `;

    document.getElementById("productModal").classList.add("active");
}

function closeProductModal() {
    document.getElementById("productModal").classList.remove("active");
}

// Cart Operations
function addToCart(id) {
    const p = productsData.find(item => item.id === id);
    if (!p) return;

    const existing = cart.find(item => item.id === id);
    if (existing) {
        existing.qty += 1;
    } else {
        cart.push({ ...p, qty: 1 });
    }

    updateCartUI();
    alert(`"${p.name}" telah ditambahkan ke keranjang Diva 💗`);
}

function updateCartUI() {
    const cartBadge = document.getElementById("cartBadge");
    const totalCount = cart.reduce((acc, item) => acc + item.qty, 0);
    cartBadge.textContent = totalCount;

    const cartList = document.getElementById("cartItemsList");
    cartList.innerHTML = "";

    let subtotal = 0;

    if (cart.length === 0) {
        cartList.innerHTML = `<p style="text-align:center; padding:20px; color: var(--text-secondary);">Keranjang belanja masih kosong.</p>`;
    } else {
        cart.forEach(item => {
            const itemTotal = item.price * item.qty;
            subtotal += itemTotal;

            const div = document.createElement("div");
            div.className = "cart-item";
            div.innerHTML = `
                <img src="${item.image}" alt="${item.name}">
                <div class="cart-item-info">
                    <h5 style="font-size: 0.85rem;">${item.name}</h5>
                    <span style="color: var(--pink-button); font-weight:600; font-size:0.85rem;">${formatRupiah(item.price)}</span>
                </div>
                <div class="cart-qty-controls">
                    <button onclick="changeQty('${item.id}', -1)">-</button>
                    <span>${item.qty}</span>
                    <button onclick="changeQty('${item.id}', 1)">+</button>
                </div>
            `;
            cartList.appendChild(div);
        });
    }

    document.getElementById("cartSubtotal").textContent = formatRupiah(subtotal);
}

function changeQty(id, delta) {
    const item = cart.find(i => i.id === id);
    if (!item) return;

    item.qty += delta;
    if (item.qty <= 0) {
        cart = cart.filter(i => i.id !== id);
    }
    updateCartUI();
}

function openCartModal() {
    updateCartUI();
    document.getElementById("cartModal").classList.add("active");
}

function closeCartModal() {
    document.getElementById("cartModal").classList.remove("active");
}

// Checkout & Payment Modals
function openCheckoutModal() {
    if (cart.length === 0) {
        alert("Keranjang kamu masih kosong!");
        return;
    }
    closeCartModal();

    let subtotal = cart.reduce((acc, item) => acc + (item.price * item.qty), 0);
    document.getElementById("checkoutSubtotal").textContent = formatRupiah(subtotal);
    updateCheckoutTotal();

    document.getElementById("checkoutModal").classList.add("active");
}

function closeCheckoutModal() {
    document.getElementById("checkoutModal").classList.remove("active");
}

function updateCheckoutTotal() {
    let subtotal = cart.reduce((acc, item) => acc + (item.price * item.qty), 0);
    let shipping = parseInt(document.getElementById("shippingOption").value, 10);

    document.getElementById("checkoutShipping").textContent = formatRupiah(shipping);
    document.getElementById("checkoutTotal").textContent = formatRupiah(subtotal + shipping);
}

function processOrder() {
    const subtotal = cart.reduce((acc, item) => acc + (item.price * item.qty), 0);
    const shipping = parseInt(document.getElementById("shippingOption").value, 10);
    const total = subtotal + shipping;

    closeCheckoutModal();

    const orderNum = "MSP-" + Math.floor(10000 + Math.random() * 90000);
    document.getElementById("orderNumberDisplay").textContent = orderNum;
    document.getElementById("paymentTotalDisplay").textContent = formatRupiah(total);

    document.getElementById("paymentModal").classList.add("active");
}

function closePaymentModal() {
    document.getElementById("paymentModal").classList.remove("active");
}

function copyVA() {
    navigator.clipboard.writeText("1234567890");
    alert("Nomor Virtual Account 1234567890 telah disalin!");
}

function checkPaymentStatus() {
    const statusBox = document.getElementById("paymentStatusBox");
    statusBox.classList.remove("hidden");
    statusBox.innerHTML = `
        <div style="background: #E8F5E9; color: #2E7D32; padding: 12px; border-radius: 8px; margin-top: 15px; text-align: center;">
            <i class="fa-solid fa-circle-check"></i> <strong>Pembayaran Berhasil!</strong><br>
            <small>Status pesanan telah diperbarui menjadi <strong>Diproses</strong> 💗</small>
        </div>
    `;
    cart = [];
    updateCartUI();
}

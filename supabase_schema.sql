-- ========================================================
-- MY SHOPEE BY DIVA 💗 — SUPABASE POSTGRESQL SCHEMA & RLS
-- ========================================================

-- Enable UUID Extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. PROFILES TABLE
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone_number TEXT DEFAULT '',
    address TEXT DEFAULT 'Kota Cirebon, Jawa Barat',
    avatar_url TEXT DEFAULT '',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 2. CATEGORIES TABLE
CREATE TABLE IF NOT EXISTS public.categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL UNIQUE,
    icon_name TEXT DEFAULT 'ic_category',
    image_url TEXT DEFAULT '',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 3. PRODUCTS TABLE
CREATE TABLE IF NOT EXISTS public.products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_id UUID REFERENCES public.categories(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    description TEXT DEFAULT '',
    price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    rating NUMERIC(2, 1) DEFAULT 4.8 CHECK (rating >= 0 AND rating <= 5.0),
    sales_count INT DEFAULT 0 CHECK (sales_count >= 0),
    stock INT DEFAULT 10 CHECK (stock >= 0),
    image_url TEXT NOT NULL,
    is_popular BOOLEAN DEFAULT false,
    is_recommended BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 4. PRODUCT VARIANTS TABLE
CREATE TABLE IF NOT EXISTS public.product_variants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID REFERENCES public.products(id) ON DELETE CASCADE,
    color TEXT DEFAULT 'Pink Pastel',
    size TEXT DEFAULT 'Standard',
    stock INT DEFAULT 10 CHECK (stock >= 0),
    price_extra NUMERIC(12, 2) DEFAULT 0
);

-- 5. CART ITEMS TABLE
CREATE TABLE IF NOT EXISTS public.cart_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    product_id UUID REFERENCES public.products(id) ON DELETE CASCADE,
    variant_id UUID REFERENCES public.product_variants(id) ON DELETE SET NULL,
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
    is_selected BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 6. ORDERS TABLE
CREATE TABLE IF NOT EXISTS public.orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_number TEXT UNIQUE NOT NULL,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    recipient_name TEXT NOT NULL,
    recipient_phone TEXT NOT NULL,
    shipping_address TEXT NOT NULL,
    shipping_courier TEXT NOT NULL DEFAULT 'Reguler (2-3 hari)',
    subtotal NUMERIC(12, 2) NOT NULL,
    shipping_fee NUMERIC(12, 2) NOT NULL DEFAULT 10000,
    total_amount NUMERIC(12, 2) NOT NULL,
    virtual_account TEXT NOT NULL DEFAULT '1234567890',
    payment_status TEXT NOT NULL DEFAULT 'Menunggu Pembayaran' 
        CHECK (payment_status IN ('Menunggu Pembayaran', 'Berhasil Dibayar', 'Diproses', 'Selesai', 'Dibatalkan')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 7. ORDER ITEMS TABLE
CREATE TABLE IF NOT EXISTS public.order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID REFERENCES public.orders(id) ON DELETE CASCADE,
    product_id UUID REFERENCES public.products(id) ON DELETE SET NULL,
    product_name TEXT NOT NULL,
    product_image TEXT NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    subtotal NUMERIC(12, 2) NOT NULL
);

-- ========================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- ========================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.categories ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.products ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.product_variants ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.cart_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.order_items ENABLE ROW LEVEL SECURITY;

-- Categories & Products: Public Read Access
CREATE POLICY "Public Categories Select" ON public.categories FOR SELECT USING (true);
CREATE POLICY "Public Products Select" ON public.products FOR SELECT USING (true);
CREATE POLICY "Public Variants Select" ON public.product_variants FOR SELECT USING (true);

-- Profiles: User can manage own profile
CREATE POLICY "User Manage Profile" ON public.profiles FOR ALL USING (auth.uid() = id);
CREATE POLICY "Public Profiles Select" ON public.profiles FOR SELECT USING (true);

-- Cart Items: Strict User Isolation
CREATE POLICY "User Cart Select" ON public.cart_items FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "User Cart Insert" ON public.cart_items FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "User Cart Update" ON public.cart_items FOR UPDATE USING (auth.uid() = user_id);
CREATE POLICY "User Cart Delete" ON public.cart_items FOR DELETE USING (auth.uid() = user_id);

-- Orders: Strict User Isolation
CREATE POLICY "User Order Select" ON public.orders FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "User Order Insert" ON public.orders FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "User Order Update" ON public.orders FOR UPDATE USING (auth.uid() = user_id);

-- Order Items: Isolated via Order ownership
CREATE POLICY "User Order Items Select" ON public.order_items FOR SELECT USING (
    EXISTS (SELECT 1 FROM public.orders WHERE orders.id = order_items.order_id AND orders.user_id = auth.uid())
);
CREATE POLICY "User Order Items Insert" ON public.order_items FOR INSERT WITH CHECK (
    EXISTS (SELECT 1 FROM public.orders WHERE orders.id = order_items.order_id AND orders.user_id = auth.uid())
);

-- ========================================================
-- SEED DATA FOR DEMO & TESTING
-- ========================================================

INSERT INTO public.categories (name, icon_name) VALUES
('Fashion', 'ic_fashion'),
('Aksesori', 'ic_accessory'),
('Elektronik', 'ic_electronic'),
('Tas', 'ic_bag'),
('Sepatu', 'ic_shoes'),
('Kecantikan', 'ic_beauty')
ON CONFLICT (name) DO NOTHING;

-- Seed Products
DO $$
DECLARE
    cat_fashion UUID;
    cat_accessory UUID;
    cat_electronic UUID;
    cat_bag UUID;
    cat_shoes UUID;
    cat_beauty UUID;
BEGIN
    SELECT id INTO cat_fashion FROM public.categories WHERE name = 'Fashion';
    SELECT id INTO cat_accessory FROM public.categories WHERE name = 'Aksesori';
    SELECT id INTO cat_electronic FROM public.categories WHERE name = 'Elektronik';
    SELECT id INTO cat_bag FROM public.categories WHERE name = 'Tas';
    SELECT id INTO cat_shoes FROM public.categories WHERE name = 'Sepatu';
    SELECT id INTO cat_beauty FROM public.categories WHERE name = 'Kecantikan';

    INSERT INTO public.products (category_id, name, description, price, rating, sales_count, stock, image_url, is_popular, is_recommended) VALUES
    (cat_fashion, 'Pastel Cardigan Over-sized Diva', 'Cardigan rajut premium warna soft pink pastel yang nyaman dan aesthetic.', 149000, 4.9, 128, 25, 'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=500', true, true),
    (cat_beauty, 'Lip Gloss Soft Pink Glow Diva', 'Lip gloss tahan lama memberikan efek glossy cantik dan melembabkan bibir.', 65000, 4.8, 350, 40, 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=500', true, true),
    (cat_bag, 'Cute Mini Backpack Pastel Pink', 'Tas punggung mini bahan kulit sintetis premium dengan gantungan pita manis.', 185000, 4.9, 94, 15, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500', true, true),
    (cat_shoes, 'Pastel Pink Sneakers Aesthetic', 'Sepatu kets bergaya Korea warna pink lembut yang empuk dan ringan.', 245000, 4.7, 82, 12, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=500', false, true),
    (cat_accessory, 'Kalung Heart Crystal Pink', 'Kalung liontin hati terbuat dari kristal berkilau anti karat.', 89000, 5.0, 210, 30, 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=500', true, true),
    (cat_electronic, 'Headphone Wireless Rose Gold Cute', 'Earphone bluetooth nirkabel dengan kualitas suara bass jernih dan bantalan empuk.', 320000, 4.8, 65, 8, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', false, true);
END $$;

-- USERS
CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL
);

-- PRODUCTS
CREATE TABLE products (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(15,2) NOT NULL,
    category VARCHAR(100),
    stock_quantity INT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

-- ORDERS
CREATE TABLE orders (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ORDER_ITEMS
CREATE TABLE order_items (
    id BINARY(16) PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Índices auxiliares
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    delete_at TIMESTAMP DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- CREATE TABLE sessions (
--       id VARCHAR(36) PRIMARY KEY,
--       user_id VARCHAR(36) NOT NULL,
--       session_token VARCHAR(255) UNIQUE NOT NULL,
--       active BOOLEAN DEFAULT TRUE,
--       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--       expires_at TIMESTAMP,
--       CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES users(id)
-- );

CREATE TABLE vendors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    cnpj VARCHAR(20),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    vendor_id UUID NOT NULL,
    total_amount DECIMAL(10,2),
    status VARCHAR(30) NOT NULL CHECK (status IN ('CREATED', 'SENT_TO_VENDOR', 'ACCEPTED', 'PREPARING', 'DELIVERED', 'CANCELED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_orders_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id)
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vendor_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL ,
    description VARCHAR(255) NOT NULL ,
    price DECIMAL(10,2) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_products_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id)
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_name VARCHAR(150),
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2),
    total DECIMAL(10,2),

    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    amount DECIMAL(10,2),
    status VARCHAR(30) NOT NULL CHECK (status IN ('CREATED', 'AUTHORIZED', 'PAID', 'REFUNDED')),
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders(id)
);


-- CREATE TABLE flows (
--     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--     order_id UUID,
--     current_step VARCHAR(50),
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_flows_order FOREIGN KEY (order_id) REFERENCES orders(id)
-- );
--
-- CREATE TABLE flow_steps (
--     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--     flow_id UUID NOT NULL,
--     step VARCHAR(50),
--     token VARCHAR(255),
--     timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
-- CONSTRAINT fk_flow_steps_flow FOREIGN KEY (flow_id) REFERENCES flows(id)
-- );
--

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trace_id VARCHAR(100),
    action VARCHAR(100),
    user_id UUID NOT NULL,
    order_id UUID NOT NULL,
    status VARCHAR(50),
    payload TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_vendor ON products(vendor_id);
CREATE INDEX idx_user_email ON users (email);
-- CREATE INDEX idx_session_token ON sessions(session_token);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_payments_order ON payments(order_id);
-- CREATE INDEX idx_flow_order ON flows(order_id);
-- CREATE INDEX idx_flow_steps_flow ON flow_steps(flow_id);
CREATE INDEX idx_audit_trace ON audit_logs(trace_id);

ALTER TABLE ag_user_product
    ADD COLUMN reserved_quantity NUMERIC(19,4) NOT NULL DEFAULT 0;

CREATE TABLE ag_cart (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
                         status VARCHAR(20) NOT NULL
);

CREATE TABLE ag_cart_item (
                              id UUID PRIMARY KEY,
                              cart_id UUID NOT NULL REFERENCES ag_cart(id) ON DELETE CASCADE,
                              user_product_id UUID NOT NULL REFERENCES ag_user_product(id) ON DELETE RESTRICT,
                              quantity NUMERIC(19,4) NOT NULL,
                              CONSTRAINT uk_ag_cart_item_cart_product UNIQUE (cart_id, user_product_id)
);

CREATE TABLE ag_order (
                          id UUID PRIMARY KEY,
                          user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
                          status VARCHAR(30) NOT NULL,
                          payment_method VARCHAR(30) NOT NULL,
                          currency_code VARCHAR(3) NOT NULL,
                          subtotal NUMERIC(19,4) NOT NULL,
                          delivery_fee NUMERIC(19,4) NOT NULL,
                          total NUMERIC(19,4) NOT NULL,
                          assigned_driver_user_id UUID REFERENCES id_user(id) ON DELETE SET NULL
);

CREATE TABLE ag_order_item (
                               id UUID PRIMARY KEY,
                               order_id UUID NOT NULL REFERENCES ag_order(id) ON DELETE CASCADE,
                               user_product_id UUID NOT NULL REFERENCES ag_user_product(id) ON DELETE RESTRICT,
                               quantity NUMERIC(19,4) NOT NULL,
                               unit_price NUMERIC(19,4) NOT NULL,
                               line_total NUMERIC(19,4) NOT NULL,
                               currency_code VARCHAR(3) NOT NULL
);

CREATE TABLE ag_delivery_address (
                                     id UUID PRIMARY KEY,
                                     order_id UUID NOT NULL UNIQUE REFERENCES ag_order(id) ON DELETE CASCADE,
                                     country_id UUID NOT NULL REFERENCES geo_country(id) ON DELETE RESTRICT,
                                     province_id UUID NOT NULL REFERENCES geo_province(id) ON DELETE RESTRICT,
                                     city_id UUID NOT NULL REFERENCES geo_city(id) ON DELETE RESTRICT,
                                     municipality_id UUID NOT NULL REFERENCES geo_municipality(id) ON DELETE RESTRICT,
                                     address_line VARCHAR(255) NOT NULL,
                                     contact_name VARCHAR(120) NOT NULL,
                                     contact_phone VARCHAR(30) NOT NULL,
                                     notes VARCHAR(500)
);

CREATE INDEX idx_ag_cart_user_status ON ag_cart(user_id, status);
CREATE INDEX idx_ag_order_user ON ag_order(user_id);
CREATE INDEX idx_ag_order_status ON ag_order(status);
CREATE INDEX idx_ag_order_driver ON ag_order(assigned_driver_user_id);

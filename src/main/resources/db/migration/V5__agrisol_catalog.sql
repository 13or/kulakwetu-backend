CREATE TABLE ag_product_category (
                                     id UUID PRIMARY KEY,
                                     image_url VARCHAR(255),
                                     name VARCHAR(120) NOT NULL,
                                     description VARCHAR(500) NOT NULL,
                                     is_public BOOLEAN NOT NULL,
                                     CONSTRAINT uk_ag_product_category_name UNIQUE (name)
);

CREATE TABLE ag_product (
                            id UUID PRIMARY KEY,
                            image_url VARCHAR(255),
                            name VARCHAR(120) NOT NULL,
                            product_category_id UUID NOT NULL REFERENCES ag_product_category(id) ON DELETE RESTRICT,
                            is_public BOOLEAN NOT NULL,
                            is_popular BOOLEAN NOT NULL,
                            CONSTRAINT uk_ag_product_name_category UNIQUE (name, product_category_id)
);

CREATE TABLE ag_supplier_business (
                                      id UUID PRIMARY KEY,
                                      supplier_user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
                                      business_name VARCHAR(160) NOT NULL,
                                      idnat VARCHAR(60),
                                      rccm VARCHAR(60),
                                      tax_number VARCHAR(60),
                                      location_address VARCHAR(255),
                                      production_address VARCHAR(255),
                                      logo_url VARCHAR(255),
                                      phone_number VARCHAR(30) NOT NULL,
                                      whatsapp VARCHAR(30),
                                      email VARCHAR(120),
                                      facebook VARCHAR(255),
                                      linkedin VARCHAR(255),
                                      instagram VARCHAR(255),
                                      tiktok VARCHAR(255),
                                      youtube VARCHAR(255),
                                      CONSTRAINT uk_ag_supplier_business_user UNIQUE (supplier_user_id)
);

CREATE TABLE ag_user_product (
                                 id UUID PRIMARY KEY,
                                 supplier_user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
                                 agrisol_product_id UUID NOT NULL REFERENCES ag_product(id) ON DELETE RESTRICT,
                                 description VARCHAR(500) NOT NULL,
                                 in_stock BOOLEAN NOT NULL,
                                 quantity_in_stock NUMERIC(19,4),
                                 stock_measure_unit VARCHAR(40),
                                 available BOOLEAN NOT NULL,
                                 available_at TIMESTAMP,
                                 name VARCHAR(120) NOT NULL,
                                 production_space NUMERIC(19,4),
                                 production_space_unit VARCHAR(40),
                                 production_location VARCHAR(255),
                                 is_popular BOOLEAN NOT NULL,
                                 status VARCHAR(20) NOT NULL
);

CREATE TABLE ag_user_product_media (
                                       id UUID PRIMARY KEY,
                                       user_product_id UUID NOT NULL REFERENCES ag_user_product(id) ON DELETE CASCADE,
                                       media_url VARCHAR(255) NOT NULL,
                                       featured BOOLEAN NOT NULL,
                                       type VARCHAR(10) NOT NULL
);

CREATE TABLE ag_user_product_price (
                                       id UUID PRIMARY KEY,
                                       user_product_id UUID NOT NULL REFERENCES ag_user_product(id) ON DELETE CASCADE,
                                       unit VARCHAR(40) NOT NULL,
                                       price NUMERIC(19,4) NOT NULL,
                                       currency_code VARCHAR(3) NOT NULL
);

CREATE INDEX idx_ag_user_product_supplier ON ag_user_product(supplier_user_id);
CREATE INDEX idx_ag_user_product_status ON ag_user_product(status);

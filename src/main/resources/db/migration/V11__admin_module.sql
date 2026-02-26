CREATE TABLE IF NOT EXISTS adm_company_info (
                                                id UUID PRIMARY KEY,
                                                legal_name VARCHAR(140) NOT NULL,
    trade_name VARCHAR(140),
    support_email VARCHAR(120),
    support_phone VARCHAR(30),
    website_url VARCHAR(255),
    agrisol_description TEXT,
    agricash_description TEXT,
    updated_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS adm_slide (
                                         id UUID PRIMARY KEY,
                                         target VARCHAR(20) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    heading VARCHAR(160) NOT NULL,
    body_text VARCHAR(255),
    description TEXT,
    slug VARCHAR(160) NOT NULL,
    is_public BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_adm_slide_slug UNIQUE (slug)
    );

CREATE INDEX IF NOT EXISTS idx_adm_slide_target ON adm_slide(target);
CREATE INDEX IF NOT EXISTS idx_adm_slide_public ON adm_slide(is_public);

CREATE TABLE IF NOT EXISTS adm_subscription_type (
                                                     id UUID PRIMARY KEY,
                                                     name VARCHAR(120) NOT NULL,
    benefits TEXT,
    price NUMERIC(19,4) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    duration_days INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_adm_subscription_name UNIQUE (name)
    );

CREATE INDEX IF NOT EXISTS idx_adm_subscription_active ON adm_subscription_type(is_active);

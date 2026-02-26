CREATE TABLE ref_account_type (
                                  id UUID PRIMARY KEY,
                                  code VARCHAR(40) NOT NULL,
                                  name VARCHAR(120) NOT NULL,
                                  is_active BOOLEAN NOT NULL,
                                  CONSTRAINT uk_ref_account_type_code UNIQUE (code)
);

CREATE TABLE ref_account_category (
                                      id UUID PRIMARY KEY,
                                      code VARCHAR(40) NOT NULL,
                                      name VARCHAR(120) NOT NULL,
                                      is_active BOOLEAN NOT NULL,
                                      CONSTRAINT uk_ref_account_category_code UNIQUE (code)
);

CREATE TABLE ref_currency (
                              id UUID PRIMARY KEY,
                              code VARCHAR(3) NOT NULL,
                              name VARCHAR(120) NOT NULL,
                              symbol VARCHAR(10) NOT NULL,
                              decimals INT NOT NULL,
                              is_active BOOLEAN NOT NULL,
                              CONSTRAINT uk_ref_currency_code UNIQUE (code)
);

CREATE TABLE ref_currency_rate (
                                   id UUID PRIMARY KEY,
                                   base_currency_id UUID NOT NULL REFERENCES ref_currency(id) ON DELETE RESTRICT,
                                   quote_currency_id UUID NOT NULL REFERENCES ref_currency(id) ON DELETE RESTRICT,
                                   rate NUMERIC(19,8) NOT NULL,
                                   valid_from TIMESTAMP NOT NULL,
                                   CONSTRAINT uk_ref_currency_rate_pair_time UNIQUE (base_currency_id, quote_currency_id, valid_from)
);

CREATE TABLE ref_measure_unit_product (
                                          id UUID PRIMARY KEY,
                                          name VARCHAR(120) NOT NULL,
                                          symbol VARCHAR(20) NOT NULL,
                                          is_public BOOLEAN NOT NULL,
                                          CONSTRAINT uk_ref_unit_product_name UNIQUE (name)
);

CREATE TABLE ref_measure_unit_land (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(120) NOT NULL,
                                       symbol VARCHAR(20) NOT NULL,
                                       is_public BOOLEAN NOT NULL,
                                       CONSTRAINT uk_ref_unit_land_name UNIQUE (name)
);

INSERT INTO ref_account_type (id, code, name, is_active) VALUES
                                                             ('44444444-4444-4444-4444-444444444001','CONSUMER','Consumer',TRUE),
                                                             ('44444444-4444-4444-4444-444444444002','SUPPLIER','Supplier',TRUE),
                                                             ('44444444-4444-4444-4444-444444444003','PRODUCER','Producer',TRUE);

INSERT INTO ref_account_category (id, code, name, is_active) VALUES
                                                                 ('55555555-5555-5555-5555-555555555001','INDIVIDUAL','Individual',TRUE),
                                                                 ('55555555-5555-5555-5555-555555555002','FAMILY','Family',TRUE),
                                                                 ('55555555-5555-5555-5555-555555555003','SUPERMARKET','Supermarket',TRUE),
                                                                 ('55555555-5555-5555-5555-555555555004','RESTAURANT','Restaurant',TRUE),
                                                                 ('55555555-5555-5555-5555-555555555005','LOCALMARKET','Local market',TRUE),
                                                                 ('55555555-5555-5555-5555-555555555006','COMPANY','Company',TRUE);

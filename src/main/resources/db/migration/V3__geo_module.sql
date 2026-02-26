CREATE TABLE geo_country (
                             id UUID PRIMARY KEY,
                             name VARCHAR(120) NOT NULL,
                             iso_code VARCHAR(3) NOT NULL,
                             phone_code VARCHAR(10) NOT NULL,
                             is_public BOOLEAN NOT NULL,
                             CONSTRAINT uk_geo_country_name UNIQUE (name),
                             CONSTRAINT uk_geo_country_iso UNIQUE (iso_code)
);

CREATE TABLE geo_province (
                              id UUID PRIMARY KEY,
                              country_id UUID NOT NULL REFERENCES geo_country(id) ON DELETE RESTRICT,
                              name VARCHAR(120) NOT NULL,
                              is_public BOOLEAN NOT NULL,
                              CONSTRAINT uk_geo_province_country_name UNIQUE (country_id, name)
);

CREATE TABLE geo_city (
                          id UUID PRIMARY KEY,
                          province_id UUID NOT NULL REFERENCES geo_province(id) ON DELETE RESTRICT,
                          name VARCHAR(120) NOT NULL,
                          is_public BOOLEAN NOT NULL,
                          CONSTRAINT uk_geo_city_province_name UNIQUE (province_id, name)
);

CREATE TABLE geo_municipality (
                                  id UUID PRIMARY KEY,
                                  city_id UUID NOT NULL REFERENCES geo_city(id) ON DELETE RESTRICT,
                                  name VARCHAR(120) NOT NULL,
                                  is_public BOOLEAN NOT NULL,
                                  CONSTRAINT uk_geo_municipality_city_name UNIQUE (city_id, name)
);

CREATE INDEX idx_geo_province_country ON geo_province(country_id);
CREATE INDEX idx_geo_city_province ON geo_city(province_id);
CREATE INDEX idx_geo_municipality_city ON geo_municipality(city_id);

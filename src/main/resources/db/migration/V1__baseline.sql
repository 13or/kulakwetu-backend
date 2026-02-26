-- Flyway baseline migration for KULAKWETU.
-- flyway_schema_history is created automatically by Flyway.

CREATE TABLE IF NOT EXISTS app_metadata (
                                            id BIGSERIAL PRIMARY KEY,
                                            app_name VARCHAR(120) NOT NULL,
    environment VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
    );

INSERT INTO app_metadata (app_name, environment)
SELECT 'KULAKWETU', 'baseline'
    WHERE NOT EXISTS (SELECT 1 FROM app_metadata WHERE app_name = 'KULAKWETU' AND environment = 'baseline');

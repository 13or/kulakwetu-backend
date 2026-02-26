CREATE TABLE id_user (
                         id UUID PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         phone_number VARCHAR(30) NOT NULL UNIQUE,
                         email VARCHAR(120) UNIQUE,
                         password_hash VARCHAR(255) NOT NULL,
                         first_name VARCHAR(80) NOT NULL,
                         last_name VARCHAR(80) NOT NULL,
                         account_type VARCHAR(30) NOT NULL,
                         enabled BOOLEAN NOT NULL,
                         verified BOOLEAN NOT NULL,
                         mfa_enabled BOOLEAN NOT NULL,
                         mfa_secret VARCHAR(128),
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL
);

CREATE TABLE id_role (
                         id UUID PRIMARY KEY,
                         name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE id_permission (
                               id UUID PRIMARY KEY,
                               name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE id_user_role (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL REFERENCES id_user(id),
                              role_id UUID NOT NULL REFERENCES id_role(id),
                              created_at TIMESTAMP NOT NULL,
                              CONSTRAINT uk_id_user_role UNIQUE (user_id, role_id)
);

CREATE TABLE id_role_permission (
                                    id UUID PRIMARY KEY,
                                    role_id UUID NOT NULL REFERENCES id_role(id),
                                    permission_id UUID NOT NULL REFERENCES id_permission(id),
                                    CONSTRAINT uk_id_role_permission UNIQUE (role_id, permission_id)
);

CREATE TABLE id_refresh_token (
                                  id UUID PRIMARY KEY,
                                  user_id UUID NOT NULL REFERENCES id_user(id),
                                  token VARCHAR(140) NOT NULL UNIQUE,
                                  expires_at TIMESTAMP NOT NULL,
                                  revoked BOOLEAN NOT NULL,
                                  created_at TIMESTAMP NOT NULL
);

CREATE TABLE id_verification_token (
                                       id UUID PRIMARY KEY,
                                       user_id UUID NOT NULL REFERENCES id_user(id),
                                       channel VARCHAR(20) NOT NULL,
                                       token VARCHAR(140) NOT NULL UNIQUE,
                                       consumed BOOLEAN NOT NULL,
                                       expires_at TIMESTAMP NOT NULL,
                                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE id_password_reset_token (
                                         id UUID PRIMARY KEY,
                                         user_id UUID NOT NULL REFERENCES id_user(id),
                                         token VARCHAR(140) NOT NULL UNIQUE,
                                         consumed BOOLEAN NOT NULL,
                                         expires_at TIMESTAMP NOT NULL,
                                         created_at TIMESTAMP NOT NULL
);

CREATE TABLE ag_wallet (
                           id UUID PRIMARY KEY,
                           user_id UUID NOT NULL REFERENCES id_user(id),
                           currency VARCHAR(3) NOT NULL,
                           balance NUMERIC(19,4) NOT NULL,
                           created_at TIMESTAMP NOT NULL
);

INSERT INTO id_role (id, name) VALUES
                                   ('11111111-1111-1111-1111-111111111001', 'SYSADMIN'),
                                   ('11111111-1111-1111-1111-111111111002', 'ADMIN'),
                                   ('11111111-1111-1111-1111-111111111003', 'ORDER_MANAGER'),
                                   ('11111111-1111-1111-1111-111111111004', 'DELIVERY_DRIVER'),
                                   ('11111111-1111-1111-1111-111111111005', 'STOCK_MANAGER'),
                                   ('11111111-1111-1111-1111-111111111006', 'SUPPLIER'),
                                   ('11111111-1111-1111-1111-111111111007', 'CONSUMER');

INSERT INTO id_permission (id, name) VALUES
                                         ('22222222-2222-2222-2222-222222222001', 'AGRISOL_PRODUCT_APPROVE'),
                                         ('22222222-2222-2222-2222-222222222002', 'AGRICASH_WITHDRAW_APPROVE'),
                                         ('22222222-2222-2222-2222-222222222003', 'ORDER_ASSIGN'),
                                         ('22222222-2222-2222-2222-222222222004', 'ORDER_DELIVER'),
                                         ('22222222-2222-2222-2222-222222222005', 'STOCK_MANAGE');

INSERT INTO id_role_permission (id, role_id, permission_id) VALUES
                                                                ('33333333-3333-3333-3333-333333333001', '11111111-1111-1111-1111-111111111001', '22222222-2222-2222-2222-222222222001'),
                                                                ('33333333-3333-3333-3333-333333333002', '11111111-1111-1111-1111-111111111001', '22222222-2222-2222-2222-222222222002'),
                                                                ('33333333-3333-3333-3333-333333333003', '11111111-1111-1111-1111-111111111001', '22222222-2222-2222-2222-222222222003'),
                                                                ('33333333-3333-3333-3333-333333333004', '11111111-1111-1111-1111-111111111001', '22222222-2222-2222-2222-222222222004'),
                                                                ('33333333-3333-3333-3333-333333333005', '11111111-1111-1111-1111-111111111001', '22222222-2222-2222-2222-222222222005'),
                                                                ('33333333-3333-3333-3333-333333333006', '11111111-1111-1111-1111-111111111002', '22222222-2222-2222-2222-222222222001'),
                                                                ('33333333-3333-3333-3333-333333333007', '11111111-1111-1111-1111-111111111002', '22222222-2222-2222-2222-222222222003'),
                                                                ('33333333-3333-3333-3333-333333333008', '11111111-1111-1111-1111-111111111003', '22222222-2222-2222-2222-222222222003'),
                                                                ('33333333-3333-3333-3333-333333333009', '11111111-1111-1111-1111-111111111004', '22222222-2222-2222-2222-222222222004'),
                                                                ('33333333-3333-3333-3333-333333333010', '11111111-1111-1111-1111-111111111005', '22222222-2222-2222-2222-222222222005');

CREATE TABLE IF NOT EXISTS ag_withdrawal_request (
                                                     id UUID PRIMARY KEY,
                                                     supplier_user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
    amount NUMERIC(19,4) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    idempotency_key VARCHAR(120) NOT NULL,
    payout_method VARCHAR(40) NOT NULL,
    payout_account VARCHAR(120) NOT NULL,
    rejection_reason VARCHAR(255),
    external_reference VARCHAR(120),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_ag_withdrawal_idempotency UNIQUE (idempotency_key)
    );

CREATE INDEX IF NOT EXISTS idx_ag_withdrawal_supplier_created ON ag_withdrawal_request(supplier_user_id, created_at);
CREATE INDEX IF NOT EXISTS idx_ag_withdrawal_status ON ag_withdrawal_request(status);

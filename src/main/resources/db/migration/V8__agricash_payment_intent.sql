CREATE TABLE IF NOT EXISTS ag_payment_intent (
                                                 id UUID PRIMARY KEY,
                                                 order_id UUID NOT NULL REFERENCES ag_order(id) ON DELETE RESTRICT,
    payer_user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
    amount NUMERIC(19,4) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    captured_amount NUMERIC(19,4) NOT NULL,
    refunded_amount NUMERIC(19,4) NOT NULL,
    idempotency_key VARCHAR(120) NOT NULL,
    provider_ref VARCHAR(120),
    created_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL,
    CONSTRAINT uk_ag_payment_intent_order UNIQUE (order_id),
    CONSTRAINT uk_ag_payment_intent_idempotency UNIQUE (idempotency_key)
    );

CREATE INDEX IF NOT EXISTS idx_ag_payment_intent_payer ON ag_payment_intent(payer_user_id);
CREATE INDEX IF NOT EXISTS idx_ag_payment_intent_status ON ag_payment_intent(status);

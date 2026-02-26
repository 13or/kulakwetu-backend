CREATE TABLE IF NOT EXISTS ag_payment_operation (
                                                    id UUID PRIMARY KEY,
                                                    payment_intent_id UUID NOT NULL REFERENCES ag_payment_intent(id) ON DELETE CASCADE,
    operation_type VARCHAR(30) NOT NULL,
    idempotency_key VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_ag_payment_op_type_key UNIQUE (operation_type, idempotency_key)
    );

CREATE INDEX IF NOT EXISTS idx_ag_payment_op_intent ON ag_payment_operation(payment_intent_id);

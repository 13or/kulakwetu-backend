CREATE TABLE IF NOT EXISTS ag_wallet (
                                         id UUID PRIMARY KEY,
                                         user_id UUID NOT NULL REFERENCES id_user(id) ON DELETE RESTRICT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL
    );

ALTER TABLE ag_wallet ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE ag_wallet ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
UPDATE ag_wallet SET status = 'ACTIVE' WHERE status IS NULL;
UPDATE ag_wallet SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uk_ag_wallet_user ON ag_wallet(user_id);

CREATE TABLE IF NOT EXISTS ag_wallet_balance (
                                                 id UUID PRIMARY KEY,
                                                 wallet_id UUID NOT NULL REFERENCES ag_wallet(id) ON DELETE CASCADE,
    currency_code VARCHAR(3) NOT NULL,
    available_amount NUMERIC(19,4) NOT NULL,
    reserved_amount NUMERIC(19,4) NOT NULL,
    version BIGINT NOT NULL,
    CONSTRAINT uk_ag_wallet_balance_wallet_currency UNIQUE (wallet_id, currency_code)
    );

CREATE TABLE IF NOT EXISTS ag_ledger_journal (
                                                 id UUID PRIMARY KEY,
                                                 reference_type VARCHAR(60) NOT NULL,
    reference_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    reversal_of_journal_id UUID REFERENCES ag_ledger_journal(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS ag_ledger_account (
                                                 id UUID PRIMARY KEY,
                                                 type VARCHAR(30) NOT NULL,
    owner_id UUID,
    currency_code VARCHAR(3) NOT NULL,
    CONSTRAINT uk_ag_ledger_account_type_owner_currency UNIQUE (type, owner_id, currency_code)
    );

CREATE TABLE IF NOT EXISTS ag_ledger_entry (
                                               id UUID PRIMARY KEY,
                                               journal_id UUID NOT NULL REFERENCES ag_ledger_journal(id) ON DELETE CASCADE,
    account_id UUID NOT NULL REFERENCES ag_ledger_account(id) ON DELETE RESTRICT,
    debit NUMERIC(19,4) NOT NULL,
    credit NUMERIC(19,4) NOT NULL,
    currency_code VARCHAR(3) NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_ag_ledger_entry_journal ON ag_ledger_entry(journal_id);
CREATE INDEX IF NOT EXISTS idx_ag_ledger_entry_account ON ag_ledger_entry(account_id);
CREATE INDEX IF NOT EXISTS idx_ag_ledger_journal_created ON ag_ledger_journal(created_at);
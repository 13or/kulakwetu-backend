# KULAKWETU Implementation Plan (Incremental)

## Step-by-step roadmap
1. **Identity module (implemented in this iteration)**
   - Unified account for Agrisol + Agricash
   - Register/login with username/email/phone
   - Verify account, forgot/reset password
   - Remember-me JWT TTL
   - MFA challenge flow scaffolding
   - Auto-provision Agricash account + default wallet on signup

2. **Geo module (foundation implemented, admin endpoints next)**
   - Country/province/city master tables
   - User profile links to geo entities
   - Add CRUD + activation endpoints for admin

3. **Catalog module (next iteration)**
   - Product categories, products, units of measure
   - Publication workflow requiring admin approval

4. **Marketplace module (next iteration)**
   - Cart, order, order lines, delivery address snapshot per order
   - Payment mode selection (Agricash or cash on delivery)

5. **Agricash module expansion (next iteration)**
   - Deposits, withdrawals, reversal workflows
   - Currency conversion with auditable rates
   - Advanced idempotency and reconciliation jobs

6. **Admin module expansion (next iteration)**
   - System user management
   - Account type/category configuration
   - Slides, subscriptions, currency/rate operations

7. **Ops/Production hardening (iterative)**
   - Kubernetes manifests, HPA, secrets/config maps
   - Observability (structured logs/metrics/tracing)
   - Backup/restore and disaster recovery runbooks

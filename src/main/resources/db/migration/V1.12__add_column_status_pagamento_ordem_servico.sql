ALTER TABLE IF EXISTS ordens_servico
    ADD COLUMN IF NOT EXISTS pagamento_status VARCHAR(32);
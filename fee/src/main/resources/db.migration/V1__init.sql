CREATE TABLE interest_rates (
    id SERIAL PRIMARY KEY, -- Auto-incremental
    interest_rate NUMERIC(5, 2) NOT NULL, -- NUMERIC é preferido para valores exatos como taxas
    interest_rate_type VARCHAR(15) NOT NULL CHECK (interest_rate_type IN ('monthly', 'annual')),
    age_range_start INT NOT NULL,
    age_range_end INT NOT NULL,
    effective_date_start DATE NOT NULL,
    effective_date_end DATE DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Para atualizar automaticamente a coluna updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at
BEFORE UPDATE ON interest_rates
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
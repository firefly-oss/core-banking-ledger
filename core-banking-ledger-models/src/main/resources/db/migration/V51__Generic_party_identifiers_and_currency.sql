-- Party identifiers are not Spanish tax numbers, and ISO currency is VARCHAR(3).

ALTER TABLE transaction RENAME COLUMN nif_initiating_party TO initiating_party_identifier;
ALTER TABLE transaction_line_card RENAME COLUMN card_merchant_cif TO card_merchant_tax_id;

COMMENT ON COLUMN transaction.initiating_party_identifier IS 'Identifier of the party that initiated the transaction. Not a country-specific tax number.';
COMMENT ON COLUMN transaction_line_card.card_merchant_tax_id IS 'Merchant tax identifier. The scheme is not implied.';

DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT table_name, column_name
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND data_type = 'character'
          AND character_maximum_length = 3
          AND (column_name = 'currency' OR column_name LIKE '%currency')
    LOOP
        EXECUTE format(
            'ALTER TABLE %I ALTER COLUMN %I TYPE VARCHAR(3)',
            r.table_name, r.column_name);
    END LOOP;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'money_type') THEN
        ALTER TYPE money_type ALTER ATTRIBUTE currency TYPE VARCHAR(3);
    END IF;
END $$;

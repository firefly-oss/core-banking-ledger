-- V5__Add_geotag_fields_to_transaction.sql

-- Add geotag columns to transaction table
ALTER TABLE transaction
ADD COLUMN latitude DOUBLE PRECISION,
ADD COLUMN longitude DOUBLE PRECISION,
ADD COLUMN location_name VARCHAR(255),
ADD COLUMN country VARCHAR(100),
ADD COLUMN city VARCHAR(100),
ADD COLUMN postal_code VARCHAR(20);

-- Add comments explaining the purpose of the columns
COMMENT ON COLUMN transaction.latitude IS 'Latitude coordinate of the transaction location';
COMMENT ON COLUMN transaction.longitude IS 'Longitude coordinate of the transaction location';
COMMENT ON COLUMN transaction.location_name IS 'Human-readable name of the transaction location';
COMMENT ON COLUMN transaction.country IS 'Country where the transaction occurred';
COMMENT ON COLUMN transaction.city IS 'City where the transaction occurred';
COMMENT ON COLUMN transaction.postal_code IS 'Postal code of the transaction location';

-- Create an index for geospatial queries
CREATE INDEX idx_transaction_geolocation ON transaction(latitude, longitude);

-- Create indexes for common location-based queries
CREATE INDEX idx_transaction_country ON transaction(country);
CREATE INDEX idx_transaction_city ON transaction(city);
CREATE INDEX idx_transaction_postal_code ON transaction(postal_code);

-- Update existing transactions if needed (this is a placeholder and might need to be adjusted based on business requirements)
-- UPDATE transaction SET latitude = ..., longitude = ..., location_name = ..., country = ..., city = ..., postal_code = ... WHERE ...;
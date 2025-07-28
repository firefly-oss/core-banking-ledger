-- V23__Add_asset_type_enum.sql
-- Add new asset types enum for crypto and tokenized assets

-- Create the asset_type_enum
CREATE TYPE asset_type_enum AS ENUM (
    'FIAT',
    'CRYPTOCURRENCY',
    'TOKEN_SECURITY',
    'TOKEN_UTILITY',
    'TOKEN_NFT',
    'TOKEN_STABLECOIN'
);

-- Create cast for asset_type_enum
CREATE CAST (varchar AS asset_type_enum)
    WITH INOUT
    AS IMPLICIT;
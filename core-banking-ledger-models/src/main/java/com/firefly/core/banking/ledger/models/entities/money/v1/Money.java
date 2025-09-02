package com.firefly.core.banking.ledger.models.entities.money.v1;

import com.firefly.core.banking.ledger.interfaces.enums.asset.v1.AssetTypeEnum;
import com.firefly.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

import java.util.UUID;
/**
 * Entity representing a money value with amount, currency, and asset type.
 * Supports both fiat currencies and crypto assets.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("money")
public class Money extends BaseEntity {
    @Id
    @Column("money_id")
    private UUID moneyId;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;
    
    /**
     * Type of asset (FIAT, CRYPTOCURRENCY, TOKEN_SECURITY, etc.)
     * Defaults to FIAT for backward compatibility
     */
    @Column("asset_type")
    private AssetTypeEnum assetType = AssetTypeEnum.FIAT;
    
    /**
     * Reference to crypto_asset table for cryptocurrency and token assets
     * Only populated when assetType is not FIAT
     */
    @Column("crypto_asset_id")
    private UUID cryptoAssetId;
}

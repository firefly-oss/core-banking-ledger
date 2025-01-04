package com.catalis.core.banking.ledger.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity {

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd/MM/yyyy'T'HH:mm:ss.SSSSSS",
            timezone = "Europe/Madrid"
    )
    @CreatedDate
    @Column("date_created")
    private LocalDateTime dateCreated;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd/MM/yyyy'T'HH:mm:ss.SSSSSS",
            timezone = "Europe/Madrid"
    )
    @LastModifiedDate
    @Column("date_updated")
    private LocalDateTime dateUpdated;
}
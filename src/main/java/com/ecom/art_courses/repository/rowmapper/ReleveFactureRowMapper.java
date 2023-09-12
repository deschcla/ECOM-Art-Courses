package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.ReleveFacture;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReleveFacture}, with proper type conversions.
 */
@Service
public class ReleveFactureRowMapper implements BiFunction<Row, String, ReleveFacture> {

    private final ColumnConverter converter;

    public ReleveFactureRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReleveFacture} stored in the database.
     */
    @Override
    public ReleveFacture apply(Row row, String prefix) {
        ReleveFacture entity = new ReleveFacture();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMontant(converter.fromRow(row, prefix + "_montant", Float.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setAcheteurId(converter.fromRow(row, prefix + "_acheteur_id", Long.class));
        return entity;
    }
}

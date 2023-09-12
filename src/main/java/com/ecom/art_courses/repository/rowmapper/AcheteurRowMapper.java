package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.Acheteur;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Acheteur}, with proper type conversions.
 */
@Service
public class AcheteurRowMapper implements BiFunction<Row, String, Acheteur> {

    private final ColumnConverter converter;

    public AcheteurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Acheteur} stored in the database.
     */
    @Override
    public Acheteur apply(Row row, String prefix) {
        Acheteur entity = new Acheteur();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        entity.setDateNaiss(converter.fromRow(row, prefix + "_date_naiss", LocalDate.class));
        entity.setNumTel(converter.fromRow(row, prefix + "_num_tel", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setInternalUserId(converter.fromRow(row, prefix + "_internal_user_id", Long.class));
        return entity;
    }
}

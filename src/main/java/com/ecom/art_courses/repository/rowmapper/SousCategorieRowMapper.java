package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.SousCategorie;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SousCategorie}, with proper type conversions.
 */
@Service
public class SousCategorieRowMapper implements BiFunction<Row, String, SousCategorie> {

    private final ColumnConverter converter;

    public SousCategorieRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SousCategorie} stored in the database.
     */
    @Override
    public SousCategorie apply(Row row, String prefix) {
        SousCategorie entity = new SousCategorie();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeSousCategorie(converter.fromRow(row, prefix + "_type_sous_categorie", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setCategorieId(converter.fromRow(row, prefix + "_categorie_id", Long.class));
        return entity;
    }
}

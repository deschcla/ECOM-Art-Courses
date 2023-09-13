package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.CarteBancaire;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CarteBancaire}, with proper type conversions.
 */
@Service
public class CarteBancaireRowMapper implements BiFunction<Row, String, CarteBancaire> {

    private final ColumnConverter converter;

    public CarteBancaireRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CarteBancaire} stored in the database.
     */
    @Override
    public CarteBancaire apply(Row row, String prefix) {
        CarteBancaire entity = new CarteBancaire();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRefCarte(converter.fromRow(row, prefix + "_ref_carte", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setCommandeId(converter.fromRow(row, prefix + "_commande_id", Long.class));
        return entity;
    }
}

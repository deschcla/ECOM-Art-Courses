package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.Commande;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commande}, with proper type conversions.
 */
@Service
public class CommandeRowMapper implements BiFunction<Row, String, Commande> {

    private final ColumnConverter converter;

    public CommandeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commande} stored in the database.
     */
    @Override
    public Commande apply(Row row, String prefix) {
        Commande entity = new Commande();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMontant(converter.fromRow(row, prefix + "_montant", Float.class));
        entity.setValided(converter.fromRow(row, prefix + "_valided", Integer.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setReleveFactureId(converter.fromRow(row, prefix + "_releve_facture_id", Long.class));
        entity.setAcheteurId(converter.fromRow(row, prefix + "_acheteur_id", Long.class));
        return entity;
    }
}

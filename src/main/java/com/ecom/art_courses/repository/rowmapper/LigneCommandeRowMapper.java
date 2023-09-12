package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.LigneCommande;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LigneCommande}, with proper type conversions.
 */
@Service
public class LigneCommandeRowMapper implements BiFunction<Row, String, LigneCommande> {

    private final ColumnConverter converter;

    public LigneCommandeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link LigneCommande} stored in the database.
     */
    @Override
    public LigneCommande apply(Row row, String prefix) {
        LigneCommande entity = new LigneCommande();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuantite(converter.fromRow(row, prefix + "_quantite", Integer.class));
        entity.setMontant(converter.fromRow(row, prefix + "_montant", Float.class));
        entity.setValidated(converter.fromRow(row, prefix + "_validated", Integer.class));
        entity.setNomParticipant(converter.fromRow(row, prefix + "_nom_participant", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setProduitId(converter.fromRow(row, prefix + "_produit_id", Long.class));
        entity.setCommandeId(converter.fromRow(row, prefix + "_commande_id", Long.class));
        return entity;
    }
}

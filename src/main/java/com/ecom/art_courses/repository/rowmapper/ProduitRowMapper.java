package com.ecom.art_courses.repository.rowmapper;

import com.ecom.art_courses.domain.Produit;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Produit}, with proper type conversions.
 */
@Service
public class ProduitRowMapper implements BiFunction<Row, String, Produit> {

    private final ColumnConverter converter;

    public ProduitRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Produit} stored in the database.
     */
    @Override
    public Produit apply(Row row, String prefix) {
        Produit entity = new Produit();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomProduit(converter.fromRow(row, prefix + "_nom_produit", String.class));
        entity.setDesc(converter.fromRow(row, prefix + "_jhi_desc", String.class));
        entity.setTarifUnit(converter.fromRow(row, prefix + "_tarif_unit", Float.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", ZonedDateTime.class));
        entity.setDuree(converter.fromRow(row, prefix + "_duree", String.class));
        entity.setLienImg(converter.fromRow(row, prefix + "_lien_img", String.class));
        entity.setQuantiteTotale(converter.fromRow(row, prefix + "_quantite_totale", Integer.class));
        entity.setQuantiteDispo(converter.fromRow(row, prefix + "_quantite_dispo", Integer.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setUpdateAt(converter.fromRow(row, prefix + "_update_at", Instant.class));
        entity.setSouscategorieId(converter.fromRow(row, prefix + "_souscategorie_id", Long.class));
        return entity;
    }
}

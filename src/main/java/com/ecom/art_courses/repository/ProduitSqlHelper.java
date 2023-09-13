package com.ecom.art_courses.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProduitSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_produit", table, columnPrefix + "_nom_produit"));
        columns.add(Column.aliased("jhi_desc", table, columnPrefix + "_jhi_desc"));
        columns.add(Column.aliased("tarif_unit", table, columnPrefix + "_tarif_unit"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("duree", table, columnPrefix + "_duree"));
        columns.add(Column.aliased("lien_img", table, columnPrefix + "_lien_img"));
        columns.add(Column.aliased("quantite_totale", table, columnPrefix + "_quantite_totale"));
        columns.add(Column.aliased("quantite_dispo", table, columnPrefix + "_quantite_dispo"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));

        columns.add(Column.aliased("souscategorie_id", table, columnPrefix + "_souscategorie_id"));
        return columns;
    }
}

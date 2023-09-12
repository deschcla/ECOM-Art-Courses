package com.ecom.art_courses.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LigneCommandeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("quantite", table, columnPrefix + "_quantite"));
        columns.add(Column.aliased("montant", table, columnPrefix + "_montant"));
        columns.add(Column.aliased("valided", table, columnPrefix + "_valided"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));

        columns.add(Column.aliased("produit_id", table, columnPrefix + "_produit_id"));
        columns.add(Column.aliased("commande_id", table, columnPrefix + "_commande_id"));
        return columns;
    }
}

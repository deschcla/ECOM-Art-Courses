package com.ecom.art_courses.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AcheteurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("adresse", table, columnPrefix + "_adresse"));
        columns.add(Column.aliased("date_naiss", table, columnPrefix + "_date_naiss"));
        columns.add(Column.aliased("num_tel", table, columnPrefix + "_num_tel"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("update_at", table, columnPrefix + "_update_at"));

        columns.add(Column.aliased("internal_user_id", table, columnPrefix + "_internal_user_id"));
        return columns;
    }
}

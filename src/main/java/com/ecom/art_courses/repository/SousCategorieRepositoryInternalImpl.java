package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.rowmapper.CategorieRowMapper;
import com.ecom.art_courses.repository.rowmapper.SousCategorieRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the SousCategorie entity.
 */
@SuppressWarnings("unused")
class SousCategorieRepositoryInternalImpl extends SimpleR2dbcRepository<SousCategorie, Long> implements SousCategorieRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CategorieRowMapper categorieMapper;
    private final SousCategorieRowMapper souscategorieMapper;

    private static final Table entityTable = Table.aliased("sous_categorie", EntityManager.ENTITY_ALIAS);
    private static final Table categorieTable = Table.aliased("categorie", "categorie");

    public SousCategorieRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CategorieRowMapper categorieMapper,
        SousCategorieRowMapper souscategorieMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SousCategorie.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.categorieMapper = categorieMapper;
        this.souscategorieMapper = souscategorieMapper;
    }

    @Override
    public Flux<SousCategorie> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SousCategorie> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SousCategorieSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CategorieSqlHelper.getColumns(categorieTable, "categorie"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(categorieTable)
            .on(Column.create("categorie_id", entityTable))
            .equals(Column.create("id", categorieTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SousCategorie.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SousCategorie> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SousCategorie> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private SousCategorie process(Row row, RowMetadata metadata) {
        SousCategorie entity = souscategorieMapper.apply(row, "e");
        entity.setCategorie(categorieMapper.apply(row, "categorie"));
        return entity;
    }

    @Override
    public <S extends SousCategorie> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

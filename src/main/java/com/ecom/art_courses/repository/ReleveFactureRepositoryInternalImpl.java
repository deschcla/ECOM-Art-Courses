package com.ecom.art_courses.repository;


import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.rowmapper.AcheteurRowMapper;
import com.ecom.art_courses.repository.rowmapper.ReleveFactureRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ReleveFacture entity.
 */
@SuppressWarnings("unused")
class ReleveFactureRepositoryInternalImpl extends SimpleR2dbcRepository<ReleveFacture, Long> implements ReleveFactureRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AcheteurRowMapper acheteurMapper;
    private final ReleveFactureRowMapper relevefactureMapper;

    private static final Table entityTable = Table.aliased("releve_facture", EntityManager.ENTITY_ALIAS);
    private static final Table acheteurTable = Table.aliased("acheteur", "acheteur");

    public ReleveFactureRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AcheteurRowMapper acheteurMapper,
        ReleveFactureRowMapper relevefactureMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ReleveFacture.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.acheteurMapper = acheteurMapper;
        this.relevefactureMapper = relevefactureMapper;
    }

    @Override
    public Flux<ReleveFacture> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ReleveFacture> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ReleveFactureSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AcheteurSqlHelper.getColumns(acheteurTable, "acheteur"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(acheteurTable)
            .on(Column.create("acheteur_id", entityTable))
            .equals(Column.create("id", acheteurTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ReleveFacture.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ReleveFacture> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ReleveFacture> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ReleveFacture process(Row row, RowMetadata metadata) {
        ReleveFacture entity = relevefactureMapper.apply(row, "e");
        entity.setAcheteur(acheteurMapper.apply(row, "acheteur"));
        return entity;
    }

    @Override
    public <S extends ReleveFacture> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

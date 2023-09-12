package com.ecom.art_courses.repository;


import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.repository.rowmapper.AcheteurRowMapper;
import com.ecom.art_courses.repository.rowmapper.UserRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Acheteur entity.
 */
@SuppressWarnings("unused")
class AcheteurRepositoryInternalImpl extends SimpleR2dbcRepository<Acheteur, Long> implements AcheteurRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final AcheteurRowMapper acheteurMapper;

    private static final Table entityTable = Table.aliased("acheteur", EntityManager.ENTITY_ALIAS);
    private static final Table internalUserTable = Table.aliased("jhi_user", "internalUser");

    public AcheteurRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        AcheteurRowMapper acheteurMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Acheteur.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.acheteurMapper = acheteurMapper;
    }

    @Override
    public Flux<Acheteur> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Acheteur> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AcheteurSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(internalUserTable, "internalUser"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(internalUserTable)
            .on(Column.create("internal_user_id", entityTable))
            .equals(Column.create("id", internalUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Acheteur.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Acheteur> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Acheteur> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Acheteur process(Row row, RowMetadata metadata) {
        Acheteur entity = acheteurMapper.apply(row, "e");
        entity.setInternalUser(userMapper.apply(row, "internalUser"));
        return entity;
    }

    @Override
    public <S extends Acheteur> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

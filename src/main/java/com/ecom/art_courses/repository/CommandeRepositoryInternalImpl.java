package com.ecom.art_courses.repository;


import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.repository.rowmapper.AcheteurRowMapper;
import com.ecom.art_courses.repository.rowmapper.CommandeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Commande entity.
 */
@SuppressWarnings("unused")
class CommandeRepositoryInternalImpl extends SimpleR2dbcRepository<Commande, Long> implements CommandeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ReleveFactureRowMapper relevefactureMapper;
    private final AcheteurRowMapper acheteurMapper;
    private final CommandeRowMapper commandeMapper;

    private static final Table entityTable = Table.aliased("commande", EntityManager.ENTITY_ALIAS);
    private static final Table releveFactureTable = Table.aliased("releve_facture", "releveFacture");
    private static final Table acheteurTable = Table.aliased("acheteur", "acheteur");

    private static final EntityManager.LinkTable produitLink = new EntityManager.LinkTable(
        "rel_commande__produit",
        "commande_id",
        "produit_id"
    );

    public CommandeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ReleveFactureRowMapper relevefactureMapper,
        AcheteurRowMapper acheteurMapper,
        CommandeRowMapper commandeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Commande.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.relevefactureMapper = relevefactureMapper;
        this.acheteurMapper = acheteurMapper;
        this.commandeMapper = commandeMapper;
    }

    @Override
    public Flux<Commande> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Commande> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CommandeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ReleveFactureSqlHelper.getColumns(releveFactureTable, "releveFacture"));
        columns.addAll(AcheteurSqlHelper.getColumns(acheteurTable, "acheteur"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(releveFactureTable)
            .on(Column.create("releve_facture_id", entityTable))
            .equals(Column.create("id", releveFactureTable))
            .leftOuterJoin(acheteurTable)
            .on(Column.create("acheteur_id", entityTable))
            .equals(Column.create("id", acheteurTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Commande.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Commande> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Commande> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Commande> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Commande> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Commande> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Commande process(Row row, RowMetadata metadata) {
        Commande entity = commandeMapper.apply(row, "e");
        entity.setReleveFacture(relevefactureMapper.apply(row, "releveFacture"));
        entity.setAcheteur(acheteurMapper.apply(row, "acheteur"));
        return entity;
    }

    @Override
    public <S extends Commande> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Commande> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(produitLink, entity.getId(), entity.getProduits().stream().map(Produit::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(produitLink, entityId);
    }
}

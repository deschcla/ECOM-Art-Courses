package com.ecom.art_courses.repository;

import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.repository.rowmapper.CommandeRowMapper;
import com.ecom.art_courses.repository.rowmapper.LigneCommandeRowMapper;
import com.ecom.art_courses.repository.rowmapper.ProduitRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the LigneCommande entity.
 */
@SuppressWarnings("unused")
class LigneCommandeRepositoryInternalImpl extends SimpleR2dbcRepository<LigneCommande, Long> implements LigneCommandeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProduitRowMapper produitMapper;
    private final CommandeRowMapper commandeMapper;
    private final LigneCommandeRowMapper lignecommandeMapper;

    private static final Table entityTable = Table.aliased("ligne_commande", EntityManager.ENTITY_ALIAS);
    private static final Table produitTable = Table.aliased("produit", "produit");
    private static final Table commandeTable = Table.aliased("commande", "commande");

    public LigneCommandeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProduitRowMapper produitMapper,
        CommandeRowMapper commandeMapper,
        LigneCommandeRowMapper lignecommandeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(LigneCommande.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.produitMapper = produitMapper;
        this.commandeMapper = commandeMapper;
        this.lignecommandeMapper = lignecommandeMapper;
    }

    @Override
    public Flux<LigneCommande> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<LigneCommande> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LigneCommandeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProduitSqlHelper.getColumns(produitTable, "produit"));
        columns.addAll(CommandeSqlHelper.getColumns(commandeTable, "commande"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(produitTable)
            .on(Column.create("produit_id", entityTable))
            .equals(Column.create("id", produitTable))
            .leftOuterJoin(commandeTable)
            .on(Column.create("commande_id", entityTable))
            .equals(Column.create("id", commandeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, LigneCommande.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<LigneCommande> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<LigneCommande> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private LigneCommande process(Row row, RowMetadata metadata) {
        LigneCommande entity = lignecommandeMapper.apply(row, "e");
        entity.setProduit(produitMapper.apply(row, "produit"));
        entity.setCommande(commandeMapper.apply(row, "commande"));
        return entity;
    }

    @Override
    public <S extends LigneCommande> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

package com.ecom.art_courses.web.rest;

import static com.ecom.art_courses.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.EntityManager;
import com.ecom.art_courses.repository.ProduitRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProduitResourceIT {

    private static final String DEFAULT_NOM_PRODUIT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRODUIT = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Float DEFAULT_TARIF_UNIT = 1F;
    private static final Float UPDATED_TARIF_UNIT = 2F;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DUREE = "AAAAAAAAAA";
    private static final String UPDATED_DUREE = "BBBBBBBBBB";

    private static final String DEFAULT_LIEN_IMG = "AAAAAAAAAA";
    private static final String UPDATED_LIEN_IMG = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE_TOTALE = 1;
    private static final Integer UPDATED_QUANTITE_TOTALE = 2;

    private static final Integer DEFAULT_QUANTITE_DISPO = 1;
    private static final Integer UPDATED_QUANTITE_DISPO = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Produit produit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createEntity(EntityManager em) {
        Produit produit = new Produit()
            .nomProduit(DEFAULT_NOM_PRODUIT)
            .desc(DEFAULT_DESC)
            .tarifUnit(DEFAULT_TARIF_UNIT)
            .date(DEFAULT_DATE)
            .duree(DEFAULT_DUREE)
            .lienImg(DEFAULT_LIEN_IMG)
            .quantiteTotale(DEFAULT_QUANTITE_TOTALE)
            .quantiteDispo(DEFAULT_QUANTITE_DISPO)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        SousCategorie sousCategorie;
        sousCategorie = em.insert(SousCategorieResourceIT.createEntity(em)).block();
        produit.setSouscategorie(sousCategorie);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createEntity(em)).block();
        produit.getCommandes().add(commande);
        return produit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createUpdatedEntity(EntityManager em) {
        Produit produit = new Produit()
            .nomProduit(UPDATED_NOM_PRODUIT)
            .desc(UPDATED_DESC)
            .tarifUnit(UPDATED_TARIF_UNIT)
            .date(UPDATED_DATE)
            .duree(UPDATED_DUREE)
            .lienImg(UPDATED_LIEN_IMG)
            .quantiteTotale(UPDATED_QUANTITE_TOTALE)
            .quantiteDispo(UPDATED_QUANTITE_DISPO)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        SousCategorie sousCategorie;
        sousCategorie = em.insert(SousCategorieResourceIT.createUpdatedEntity(em)).block();
        produit.setSouscategorie(sousCategorie);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createUpdatedEntity(em)).block();
        produit.getCommandes().add(commande);
        return produit;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Produit.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        SousCategorieResourceIT.deleteEntities(em);
        CommandeResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        produit = createEntity(em);
    }

    @Test
    void createProduit() throws Exception {
        int databaseSizeBeforeCreate = produitRepository.findAll().collectList().block().size();
        // Create the Produit
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeCreate + 1);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(DEFAULT_NOM_PRODUIT);
        assertThat(testProduit.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testProduit.getTarifUnit()).isEqualTo(DEFAULT_TARIF_UNIT);
        assertThat(testProduit.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProduit.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testProduit.getLienImg()).isEqualTo(DEFAULT_LIEN_IMG);
        assertThat(testProduit.getQuantiteTotale()).isEqualTo(DEFAULT_QUANTITE_TOTALE);
        assertThat(testProduit.getQuantiteDispo()).isEqualTo(DEFAULT_QUANTITE_DISPO);
        assertThat(testProduit.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProduit.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createProduitWithExistingId() throws Exception {
        // Create the Produit with an existing ID
        produit.setId(1L);

        int databaseSizeBeforeCreate = produitRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProduitsAsStream() {
        // Initialize the database
        produitRepository.save(produit).block();

        List<Produit> produitList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Produit.class)
            .getResponseBody()
            .filter(produit::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(produitList).isNotNull();
        assertThat(produitList).hasSize(1);
        Produit testProduit = produitList.get(0);
        assertThat(testProduit.getNomProduit()).isEqualTo(DEFAULT_NOM_PRODUIT);
        assertThat(testProduit.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testProduit.getTarifUnit()).isEqualTo(DEFAULT_TARIF_UNIT);
        assertThat(testProduit.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProduit.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testProduit.getLienImg()).isEqualTo(DEFAULT_LIEN_IMG);
        assertThat(testProduit.getQuantiteTotale()).isEqualTo(DEFAULT_QUANTITE_TOTALE);
        assertThat(testProduit.getQuantiteDispo()).isEqualTo(DEFAULT_QUANTITE_DISPO);
        assertThat(testProduit.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProduit.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllProduits() {
        // Initialize the database
        produitRepository.save(produit).block();

        // Get all the produitList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(produit.getId().intValue()))
            .jsonPath("$.[*].nomProduit")
            .value(hasItem(DEFAULT_NOM_PRODUIT))
            .jsonPath("$.[*].desc")
            .value(hasItem(DEFAULT_DESC))
            .jsonPath("$.[*].tarifUnit")
            .value(hasItem(DEFAULT_TARIF_UNIT.doubleValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(sameInstant(DEFAULT_DATE)))
            .jsonPath("$.[*].duree")
            .value(hasItem(DEFAULT_DUREE))
            .jsonPath("$.[*].lienImg")
            .value(hasItem(DEFAULT_LIEN_IMG))
            .jsonPath("$.[*].quantiteTotale")
            .value(hasItem(DEFAULT_QUANTITE_TOTALE))
            .jsonPath("$.[*].quantiteDispo")
            .value(hasItem(DEFAULT_QUANTITE_DISPO))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getProduit() {
        // Initialize the database
        produitRepository.save(produit).block();

        // Get the produit
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, produit.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(produit.getId().intValue()))
            .jsonPath("$.nomProduit")
            .value(is(DEFAULT_NOM_PRODUIT))
            .jsonPath("$.desc")
            .value(is(DEFAULT_DESC))
            .jsonPath("$.tarifUnit")
            .value(is(DEFAULT_TARIF_UNIT.doubleValue()))
            .jsonPath("$.date")
            .value(is(sameInstant(DEFAULT_DATE)))
            .jsonPath("$.duree")
            .value(is(DEFAULT_DUREE))
            .jsonPath("$.lienImg")
            .value(is(DEFAULT_LIEN_IMG))
            .jsonPath("$.quantiteTotale")
            .value(is(DEFAULT_QUANTITE_TOTALE))
            .jsonPath("$.quantiteDispo")
            .value(is(DEFAULT_QUANTITE_DISPO))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingProduit() {
        // Get the produit
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProduit() throws Exception {
        // Initialize the database
        produitRepository.save(produit).block();

        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();

        // Update the produit
        Produit updatedProduit = produitRepository.findById(produit.getId()).block();
        updatedProduit
            .nomProduit(UPDATED_NOM_PRODUIT)
            .desc(UPDATED_DESC)
            .tarifUnit(UPDATED_TARIF_UNIT)
            .date(UPDATED_DATE)
            .duree(UPDATED_DUREE)
            .lienImg(UPDATED_LIEN_IMG)
            .quantiteTotale(UPDATED_QUANTITE_TOTALE)
            .quantiteDispo(UPDATED_QUANTITE_DISPO)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProduit.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProduit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(UPDATED_NOM_PRODUIT);
        assertThat(testProduit.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testProduit.getTarifUnit()).isEqualTo(UPDATED_TARIF_UNIT);
        assertThat(testProduit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProduit.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testProduit.getLienImg()).isEqualTo(UPDATED_LIEN_IMG);
        assertThat(testProduit.getQuantiteTotale()).isEqualTo(UPDATED_QUANTITE_TOTALE);
        assertThat(testProduit.getQuantiteDispo()).isEqualTo(UPDATED_QUANTITE_DISPO);
        assertThat(testProduit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduit.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();
        produit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, produit.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        produitRepository.save(produit).block();

        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit
            .nomProduit(UPDATED_NOM_PRODUIT)
            .date(UPDATED_DATE)
            .lienImg(UPDATED_LIEN_IMG)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(UPDATED_NOM_PRODUIT);
        assertThat(testProduit.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testProduit.getTarifUnit()).isEqualTo(DEFAULT_TARIF_UNIT);
        assertThat(testProduit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProduit.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testProduit.getLienImg()).isEqualTo(UPDATED_LIEN_IMG);
        assertThat(testProduit.getQuantiteTotale()).isEqualTo(DEFAULT_QUANTITE_TOTALE);
        assertThat(testProduit.getQuantiteDispo()).isEqualTo(DEFAULT_QUANTITE_DISPO);
        assertThat(testProduit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduit.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void fullUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        produitRepository.save(produit).block();

        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit
            .nomProduit(UPDATED_NOM_PRODUIT)
            .desc(UPDATED_DESC)
            .tarifUnit(UPDATED_TARIF_UNIT)
            .date(UPDATED_DATE)
            .duree(UPDATED_DUREE)
            .lienImg(UPDATED_LIEN_IMG)
            .quantiteTotale(UPDATED_QUANTITE_TOTALE)
            .quantiteDispo(UPDATED_QUANTITE_DISPO)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(UPDATED_NOM_PRODUIT);
        assertThat(testProduit.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testProduit.getTarifUnit()).isEqualTo(UPDATED_TARIF_UNIT);
        assertThat(testProduit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProduit.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testProduit.getLienImg()).isEqualTo(UPDATED_LIEN_IMG);
        assertThat(testProduit.getQuantiteTotale()).isEqualTo(UPDATED_QUANTITE_TOTALE);
        assertThat(testProduit.getQuantiteDispo()).isEqualTo(UPDATED_QUANTITE_DISPO);
        assertThat(testProduit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduit.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();
        produit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, produit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().collectList().block().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(produit))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduit() {
        // Initialize the database
        produitRepository.save(produit).block();

        int databaseSizeBeforeDelete = produitRepository.findAll().collectList().block().size();

        // Delete the produit
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, produit.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Produit> produitList = produitRepository.findAll().collectList().block();
        assertThat(produitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

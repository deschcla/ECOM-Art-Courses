package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Categorie;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.EntityManager;
import com.ecom.art_courses.repository.SousCategorieRepository;
import java.time.Duration;
import java.time.Instant;
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
 * Integration tests for the {@link SousCategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SousCategorieResourceIT {

    private static final String DEFAULT_TYPE_SOUS_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_SOUS_CATEGORIE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sous-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousCategorieRepository sousCategorieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SousCategorie sousCategorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousCategorie createEntity(EntityManager em) {
        SousCategorie sousCategorie = new SousCategorie()
            .typeSousCategorie(DEFAULT_TYPE_SOUS_CATEGORIE)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        Produit produit;
        produit = em.insert(ProduitResourceIT.createEntity(em)).block();
        sousCategorie.getProduits().add(produit);
        // Add required entity
        Categorie categorie;
        categorie = em.insert(CategorieResourceIT.createEntity(em)).block();
        sousCategorie.setCategorie(categorie);
        return sousCategorie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousCategorie createUpdatedEntity(EntityManager em) {
        SousCategorie sousCategorie = new SousCategorie()
            .typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        Produit produit;
        produit = em.insert(ProduitResourceIT.createUpdatedEntity(em)).block();
        sousCategorie.getProduits().add(produit);
        // Add required entity
        Categorie categorie;
        categorie = em.insert(CategorieResourceIT.createUpdatedEntity(em)).block();
        sousCategorie.setCategorie(categorie);
        return sousCategorie;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SousCategorie.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ProduitResourceIT.deleteEntities(em);
        CategorieResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        sousCategorie = createEntity(em);
    }

    @Test
    void createSousCategorie() throws Exception {
        int databaseSizeBeforeCreate = sousCategorieRepository.findAll().collectList().block().size();
        // Create the SousCategorie
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeCreate + 1);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(DEFAULT_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createSousCategorieWithExistingId() throws Exception {
        // Create the SousCategorie with an existing ID
        sousCategorie.setId(1L);

        int databaseSizeBeforeCreate = sousCategorieRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSousCategoriesAsStream() {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        List<SousCategorie> sousCategorieList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SousCategorie.class)
            .getResponseBody()
            .filter(sousCategorie::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sousCategorieList).isNotNull();
        assertThat(sousCategorieList).hasSize(1);
        SousCategorie testSousCategorie = sousCategorieList.get(0);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(DEFAULT_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllSousCategories() {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        // Get all the sousCategorieList
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
            .value(hasItem(sousCategorie.getId().intValue()))
            .jsonPath("$.[*].typeSousCategorie")
            .value(hasItem(DEFAULT_TYPE_SOUS_CATEGORIE))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getSousCategorie() {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        // Get the sousCategorie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sousCategorie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sousCategorie.getId().intValue()))
            .jsonPath("$.typeSousCategorie")
            .value(is(DEFAULT_TYPE_SOUS_CATEGORIE))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingSousCategorie() {
        // Get the sousCategorie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSousCategorie() throws Exception {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();

        // Update the sousCategorie
        SousCategorie updatedSousCategorie = sousCategorieRepository.findById(sousCategorie.getId()).block();
        updatedSousCategorie.typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSousCategorie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSousCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(UPDATED_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();
        sousCategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sousCategorie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSousCategorieWithPatch() throws Exception {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();

        // Update the sousCategorie using partial update
        SousCategorie partialUpdatedSousCategorie = new SousCategorie();
        partialUpdatedSousCategorie.setId(sousCategorie.getId());

        partialUpdatedSousCategorie.typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSousCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSousCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(UPDATED_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void fullUpdateSousCategorieWithPatch() throws Exception {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();

        // Update the sousCategorie using partial update
        SousCategorie partialUpdatedSousCategorie = new SousCategorie();
        partialUpdatedSousCategorie.setId(sousCategorie.getId());

        partialUpdatedSousCategorie
            .typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSousCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSousCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(UPDATED_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();
        sousCategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sousCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().collectList().block().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sousCategorie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSousCategorie() {
        // Initialize the database
        sousCategorieRepository.save(sousCategorie).block();

        int databaseSizeBeforeDelete = sousCategorieRepository.findAll().collectList().block().size();

        // Delete the sousCategorie
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sousCategorie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll().collectList().block();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

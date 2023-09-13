package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Categorie;
import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.CategorieRepository;
import com.ecom.art_courses.repository.EntityManager;
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
 * Integration tests for the {@link CategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CategorieResourceIT {

    private static final String DEFAULT_TYPE_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CATEGORIE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Categorie categorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .typeCategorie(DEFAULT_TYPE_CATEGORIE)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        SousCategorie sousCategorie;
        sousCategorie = em.insert(SousCategorieResourceIT.createEntity(em)).block();
        categorie.getSousCategories().add(sousCategorie);
        return categorie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createUpdatedEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .typeCategorie(UPDATED_TYPE_CATEGORIE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        SousCategorie sousCategorie;
        sousCategorie = em.insert(SousCategorieResourceIT.createUpdatedEntity(em)).block();
        categorie.getSousCategories().add(sousCategorie);
        return categorie;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Categorie.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        SousCategorieResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        categorie = createEntity(em);
    }

    @Test
    void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().collectList().block().size();
        // Create the Categorie
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getTypeCategorie()).isEqualTo(DEFAULT_TYPE_CATEGORIE);
        assertThat(testCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createCategorieWithExistingId() throws Exception {
        // Create the Categorie with an existing ID
        categorie.setId(1L);

        int databaseSizeBeforeCreate = categorieRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCategoriesAsStream() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        List<Categorie> categorieList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Categorie.class)
            .getResponseBody()
            .filter(categorie::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(categorieList).isNotNull();
        assertThat(categorieList).hasSize(1);
        Categorie testCategorie = categorieList.get(0);
        assertThat(testCategorie.getTypeCategorie()).isEqualTo(DEFAULT_TYPE_CATEGORIE);
        assertThat(testCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllCategories() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        // Get all the categorieList
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
            .value(hasItem(categorie.getId().intValue()))
            .jsonPath("$.[*].typeCategorie")
            .value(hasItem(DEFAULT_TYPE_CATEGORIE))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getCategorie() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        // Get the categorie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(categorie.getId().intValue()))
            .jsonPath("$.typeCategorie")
            .value(is(DEFAULT_TYPE_CATEGORIE))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingCategorie() {
        // Get the categorie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCategorie() throws Exception {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findById(categorie.getId()).block();
        updatedCategorie.typeCategorie(UPDATED_TYPE_CATEGORIE).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCategorie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getTypeCategorie()).isEqualTo(UPDATED_TYPE_CATEGORIE);
        assertThat(testCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategorieWithPatch() throws Exception {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();

        // Update the categorie using partial update
        Categorie partialUpdatedCategorie = new Categorie();
        partialUpdatedCategorie.setId(categorie.getId());

        partialUpdatedCategorie.typeCategorie(UPDATED_TYPE_CATEGORIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getTypeCategorie()).isEqualTo(UPDATED_TYPE_CATEGORIE);
        assertThat(testCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void fullUpdateCategorieWithPatch() throws Exception {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();

        // Update the categorie using partial update
        Categorie partialUpdatedCategorie = new Categorie();
        partialUpdatedCategorie.setId(categorie.getId());

        partialUpdatedCategorie.typeCategorie(UPDATED_TYPE_CATEGORIE).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getTypeCategorie()).isEqualTo(UPDATED_TYPE_CATEGORIE);
        assertThat(testCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategorie() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeDelete = categorieRepository.findAll().collectList().block().size();

        // Delete the categorie
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

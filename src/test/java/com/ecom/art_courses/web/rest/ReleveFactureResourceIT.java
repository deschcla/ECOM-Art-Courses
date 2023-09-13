package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.EntityManager;
import com.ecom.art_courses.repository.ReleveFactureRepository;
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
 * Integration tests for the {@link ReleveFactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReleveFactureResourceIT {

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/releve-factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReleveFactureRepository releveFactureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReleveFacture releveFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleveFacture createEntity(EntityManager em) {
        ReleveFacture releveFacture = new ReleveFacture()
            .montant(DEFAULT_MONTANT)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createEntity(em)).block();
        releveFacture.getCommandes().add(commande);
        // Add required entity
        Acheteur acheteur;
        acheteur = em.insert(AcheteurResourceIT.createEntity(em)).block();
        releveFacture.setAcheteur(acheteur);
        return releveFacture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleveFacture createUpdatedEntity(EntityManager em) {
        ReleveFacture releveFacture = new ReleveFacture()
            .montant(UPDATED_MONTANT)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createUpdatedEntity(em)).block();
        releveFacture.getCommandes().add(commande);
        // Add required entity
        Acheteur acheteur;
        acheteur = em.insert(AcheteurResourceIT.createUpdatedEntity(em)).block();
        releveFacture.setAcheteur(acheteur);
        return releveFacture;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReleveFacture.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CommandeResourceIT.deleteEntities(em);
        AcheteurResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        releveFacture = createEntity(em);
    }

    @Test
    void createReleveFacture() throws Exception {
        int databaseSizeBeforeCreate = releveFactureRepository.findAll().collectList().block().size();
        // Create the ReleveFacture
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeCreate + 1);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createReleveFactureWithExistingId() throws Exception {
        // Create the ReleveFacture with an existing ID
        releveFacture.setId(1L);

        int databaseSizeBeforeCreate = releveFactureRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReleveFacturesAsStream() {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        List<ReleveFacture> releveFactureList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReleveFacture.class)
            .getResponseBody()
            .filter(releveFacture::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(releveFactureList).isNotNull();
        assertThat(releveFactureList).hasSize(1);
        ReleveFacture testReleveFacture = releveFactureList.get(0);
        assertThat(testReleveFacture.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllReleveFactures() {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        // Get all the releveFactureList
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
            .value(hasItem(releveFacture.getId().intValue()))
            .jsonPath("$.[*].montant")
            .value(hasItem(DEFAULT_MONTANT.doubleValue()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getReleveFacture() {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        // Get the releveFacture
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, releveFacture.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(releveFacture.getId().intValue()))
            .jsonPath("$.montant")
            .value(is(DEFAULT_MONTANT.doubleValue()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingReleveFacture() {
        // Get the releveFacture
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReleveFacture() throws Exception {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();

        // Update the releveFacture
        ReleveFacture updatedReleveFacture = releveFactureRepository.findById(releveFacture.getId()).block();
        updatedReleveFacture.montant(UPDATED_MONTANT).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedReleveFacture.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedReleveFacture))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();
        releveFacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, releveFacture.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReleveFactureWithPatch() throws Exception {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();

        // Update the releveFacture using partial update
        ReleveFacture partialUpdatedReleveFacture = new ReleveFacture();
        partialUpdatedReleveFacture.setId(releveFacture.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReleveFacture.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReleveFacture))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void fullUpdateReleveFactureWithPatch() throws Exception {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();

        // Update the releveFacture using partial update
        ReleveFacture partialUpdatedReleveFacture = new ReleveFacture();
        partialUpdatedReleveFacture.setId(releveFacture.getId());

        partialUpdatedReleveFacture.montant(UPDATED_MONTANT).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReleveFacture.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReleveFacture))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();
        releveFacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, releveFacture.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().collectList().block().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(releveFacture))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReleveFacture() {
        // Initialize the database
        releveFactureRepository.save(releveFacture).block();

        int databaseSizeBeforeDelete = releveFactureRepository.findAll().collectList().block().size();

        // Delete the releveFacture
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, releveFacture.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll().collectList().block();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

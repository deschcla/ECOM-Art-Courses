package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.domain.User;
import com.ecom.art_courses.repository.AcheteurRepository;
import com.ecom.art_courses.repository.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AcheteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AcheteurResourceIT {

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISS = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/acheteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcheteurRepository acheteurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Acheteur acheteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acheteur createEntity(EntityManager em) {
        Acheteur acheteur = new Acheteur()
            .adresse(DEFAULT_ADRESSE)
            .dateNaiss(DEFAULT_DATE_NAISS)
            .numTel(DEFAULT_NUM_TEL)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        acheteur.setInternalUser(user);
        // Add required entity
        ReleveFacture releveFacture;
        releveFacture = em.insert(ReleveFactureResourceIT.createEntity(em)).block();
        acheteur.getReleveFactures().add(releveFacture);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createEntity(em)).block();
        acheteur.getCommandes().add(commande);
        return acheteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acheteur createUpdatedEntity(EntityManager em) {
        Acheteur acheteur = new Acheteur()
            .adresse(UPDATED_ADRESSE)
            .dateNaiss(UPDATED_DATE_NAISS)
            .numTel(UPDATED_NUM_TEL)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        acheteur.setInternalUser(user);
        // Add required entity
        ReleveFacture releveFacture;
        releveFacture = em.insert(ReleveFactureResourceIT.createUpdatedEntity(em)).block();
        acheteur.getReleveFactures().add(releveFacture);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createUpdatedEntity(em)).block();
        acheteur.getCommandes().add(commande);
        return acheteur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Acheteur.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
        ReleveFactureResourceIT.deleteEntities(em);
        CommandeResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        acheteur = createEntity(em);
    }

    @Test
    void createAcheteur() throws Exception {
        int databaseSizeBeforeCreate = acheteurRepository.findAll().collectList().block().size();
        // Create the Acheteur
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate + 1);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAcheteur.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testAcheteur.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testAcheteur.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAcheteur.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createAcheteurWithExistingId() throws Exception {
        // Create the Acheteur with an existing ID
        acheteur.setId(1L);

        int databaseSizeBeforeCreate = acheteurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAcheteursAsStream() {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        List<Acheteur> acheteurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Acheteur.class)
            .getResponseBody()
            .filter(acheteur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(acheteurList).isNotNull();
        assertThat(acheteurList).hasSize(1);
        Acheteur testAcheteur = acheteurList.get(0);
        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAcheteur.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testAcheteur.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testAcheteur.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAcheteur.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllAcheteurs() {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        // Get all the acheteurList
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
            .value(hasItem(acheteur.getId().intValue()))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE))
            .jsonPath("$.[*].dateNaiss")
            .value(hasItem(DEFAULT_DATE_NAISS.toString()))
            .jsonPath("$.[*].numTel")
            .value(hasItem(DEFAULT_NUM_TEL))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getAcheteur() {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        // Get the acheteur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, acheteur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(acheteur.getId().intValue()))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE))
            .jsonPath("$.dateNaiss")
            .value(is(DEFAULT_DATE_NAISS.toString()))
            .jsonPath("$.numTel")
            .value(is(DEFAULT_NUM_TEL))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingAcheteur() {
        // Get the acheteur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAcheteur() throws Exception {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();

        // Update the acheteur
        Acheteur updatedAcheteur = acheteurRepository.findById(acheteur.getId()).block();
        updatedAcheteur
            .adresse(UPDATED_ADRESSE)
            .dateNaiss(UPDATED_DATE_NAISS)
            .numTel(UPDATED_NUM_TEL)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAcheteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAcheteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAcheteur.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testAcheteur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testAcheteur.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAcheteur.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();
        acheteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, acheteur.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();
        acheteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();
        acheteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAcheteurWithPatch() throws Exception {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();

        // Update the acheteur using partial update
        Acheteur partialUpdatedAcheteur = new Acheteur();
        partialUpdatedAcheteur.setId(acheteur.getId());

        partialUpdatedAcheteur.dateNaiss(UPDATED_DATE_NAISS).numTel(UPDATED_NUM_TEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAcheteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAcheteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAcheteur.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testAcheteur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testAcheteur.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAcheteur.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void fullUpdateAcheteurWithPatch() throws Exception {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();

        // Update the acheteur using partial update
        Acheteur partialUpdatedAcheteur = new Acheteur();
        partialUpdatedAcheteur.setId(acheteur.getId());

        partialUpdatedAcheteur
            .adresse(UPDATED_ADRESSE)
            .dateNaiss(UPDATED_DATE_NAISS)
            .numTel(UPDATED_NUM_TEL)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAcheteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAcheteur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
        assertThat(testAcheteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAcheteur.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testAcheteur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testAcheteur.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAcheteur.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();
        acheteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, acheteur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();
        acheteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAcheteur() throws Exception {
        int databaseSizeBeforeUpdate = acheteurRepository.findAll().collectList().block().size();
        acheteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(acheteur))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Acheteur in the database
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAcheteur() {
        // Initialize the database
        acheteurRepository.save(acheteur).block();

        int databaseSizeBeforeDelete = acheteurRepository.findAll().collectList().block().size();

        // Delete the acheteur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, acheteur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Acheteur> acheteurList = acheteurRepository.findAll().collectList().block();
        assertThat(acheteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

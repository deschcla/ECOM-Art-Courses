package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.repository.EntityManager;
import com.ecom.art_courses.repository.LigneCommandeRepository;
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
 * Integration tests for the {@link LigneCommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LigneCommandeResourceIT {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final Integer DEFAULT_VALIDATED = 1;
    private static final Integer UPDATED_VALIDATED = 2;

    private static final String DEFAULT_NOM_PARTICIPANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PARTICIPANT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ligne-commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private LigneCommande ligneCommande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommande createEntity(EntityManager em) {
        LigneCommande ligneCommande = new LigneCommande()
            .quantite(DEFAULT_QUANTITE)
            .montant(DEFAULT_MONTANT)
            .validated(DEFAULT_VALIDATED)
            .nomParticipant(DEFAULT_NOM_PARTICIPANT)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        Produit produit;
        produit = em.insert(ProduitResourceIT.createEntity(em)).block();
        ligneCommande.setProduit(produit);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createEntity(em)).block();
        ligneCommande.setCommande(commande);
        return ligneCommande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommande createUpdatedEntity(EntityManager em) {
        LigneCommande ligneCommande = new LigneCommande()
            .quantite(UPDATED_QUANTITE)
            .montant(UPDATED_MONTANT)
            .validated(UPDATED_VALIDATED)
            .nomParticipant(UPDATED_NOM_PARTICIPANT)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        Produit produit;
        produit = em.insert(ProduitResourceIT.createUpdatedEntity(em)).block();
        ligneCommande.setProduit(produit);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createUpdatedEntity(em)).block();
        ligneCommande.setCommande(commande);
        return ligneCommande;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(LigneCommande.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ProduitResourceIT.deleteEntities(em);
        CommandeResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        ligneCommande = createEntity(em);
    }

    @Test
    void createLigneCommande() throws Exception {
        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().collectList().block().size();
        // Create the LigneCommande
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testLigneCommande.getValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testLigneCommande.getNomParticipant()).isEqualTo(DEFAULT_NOM_PARTICIPANT);
        assertThat(testLigneCommande.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLigneCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createLigneCommandeWithExistingId() throws Exception {
        // Create the LigneCommande with an existing ID
        ligneCommande.setId(1L);

        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLigneCommandesAsStream() {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        List<LigneCommande> ligneCommandeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LigneCommande.class)
            .getResponseBody()
            .filter(ligneCommande::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(ligneCommandeList).isNotNull();
        assertThat(ligneCommandeList).hasSize(1);
        LigneCommande testLigneCommande = ligneCommandeList.get(0);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testLigneCommande.getValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testLigneCommande.getNomParticipant()).isEqualTo(DEFAULT_NOM_PARTICIPANT);
        assertThat(testLigneCommande.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLigneCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllLigneCommandes() {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        // Get all the ligneCommandeList
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
            .value(hasItem(ligneCommande.getId().intValue()))
            .jsonPath("$.[*].quantite")
            .value(hasItem(DEFAULT_QUANTITE))
            .jsonPath("$.[*].montant")
            .value(hasItem(DEFAULT_MONTANT.doubleValue()))
            .jsonPath("$.[*].validated")
            .value(hasItem(DEFAULT_VALIDATED))
            .jsonPath("$.[*].nomParticipant")
            .value(hasItem(DEFAULT_NOM_PARTICIPANT))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getLigneCommande() {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        // Get the ligneCommande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ligneCommande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ligneCommande.getId().intValue()))
            .jsonPath("$.quantite")
            .value(is(DEFAULT_QUANTITE))
            .jsonPath("$.montant")
            .value(is(DEFAULT_MONTANT.doubleValue()))
            .jsonPath("$.validated")
            .value(is(DEFAULT_VALIDATED))
            .jsonPath("$.nomParticipant")
            .value(is(DEFAULT_NOM_PARTICIPANT))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingLigneCommande() {
        // Get the ligneCommande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();

        // Update the ligneCommande
        LigneCommande updatedLigneCommande = ligneCommandeRepository.findById(ligneCommande.getId()).block();
        updatedLigneCommande
            .quantite(UPDATED_QUANTITE)
            .montant(UPDATED_MONTANT)
            .validated(UPDATED_VALIDATED)
            .nomParticipant(UPDATED_NOM_PARTICIPANT)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedLigneCommande.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedLigneCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testLigneCommande.getValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testLigneCommande.getNomParticipant()).isEqualTo(UPDATED_NOM_PARTICIPANT);
        assertThat(testLigneCommande.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLigneCommande.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();
        ligneCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ligneCommande.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLigneCommandeWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();

        // Update the ligneCommande using partial update
        LigneCommande partialUpdatedLigneCommande = new LigneCommande();
        partialUpdatedLigneCommande.setId(ligneCommande.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLigneCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testLigneCommande.getValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testLigneCommande.getNomParticipant()).isEqualTo(DEFAULT_NOM_PARTICIPANT);
        assertThat(testLigneCommande.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLigneCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void fullUpdateLigneCommandeWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();

        // Update the ligneCommande using partial update
        LigneCommande partialUpdatedLigneCommande = new LigneCommande();
        partialUpdatedLigneCommande.setId(ligneCommande.getId());

        partialUpdatedLigneCommande
            .quantite(UPDATED_QUANTITE)
            .montant(UPDATED_MONTANT)
            .validated(UPDATED_VALIDATED)
            .nomParticipant(UPDATED_NOM_PARTICIPANT)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLigneCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testLigneCommande.getValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testLigneCommande.getNomParticipant()).isEqualTo(UPDATED_NOM_PARTICIPANT);
        assertThat(testLigneCommande.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLigneCommande.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();
        ligneCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ligneCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().collectList().block().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ligneCommande))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLigneCommande() {
        // Initialize the database
        ligneCommandeRepository.save(ligneCommande).block();

        int databaseSizeBeforeDelete = ligneCommandeRepository.findAll().collectList().block().size();

        // Delete the ligneCommande
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ligneCommande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll().collectList().block();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

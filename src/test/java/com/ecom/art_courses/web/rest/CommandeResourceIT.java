package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.domain.CarteBancaire;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.CommandeRepository;
import com.ecom.art_courses.repository.EntityManager;
import com.ecom.art_courses.service.CommandeService;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommandeResourceIT {

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final Integer DEFAULT_VALIDED = 1;
    private static final Integer UPDATED_VALIDED = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeRepository commandeRepositoryMock;

    @Mock
    private CommandeService commandeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .montant(DEFAULT_MONTANT)
            .valided(DEFAULT_VALIDED)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        CarteBancaire carteBancaire;
        carteBancaire = em.insert(CarteBancaireResourceIT.createEntity(em)).block();
        commande.getCarteBancaires().add(carteBancaire);
        // Add required entity
        Produit produit;
        produit = em.insert(ProduitResourceIT.createEntity(em)).block();
        commande.getProduits().add(produit);
        // Add required entity
        ReleveFacture releveFacture;
        releveFacture = em.insert(ReleveFactureResourceIT.createEntity(em)).block();
        commande.setReleveFacture(releveFacture);
        // Add required entity
        Acheteur acheteur;
        acheteur = em.insert(AcheteurResourceIT.createEntity(em)).block();
        commande.setAcheteur(acheteur);
        return commande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande()
            .montant(UPDATED_MONTANT)
            .valided(UPDATED_VALIDED)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        CarteBancaire carteBancaire;
        carteBancaire = em.insert(CarteBancaireResourceIT.createUpdatedEntity(em)).block();
        commande.getCarteBancaires().add(carteBancaire);
        // Add required entity
        Produit produit;
        produit = em.insert(ProduitResourceIT.createUpdatedEntity(em)).block();
        commande.getProduits().add(produit);
        // Add required entity
        ReleveFacture releveFacture;
        releveFacture = em.insert(ReleveFactureResourceIT.createUpdatedEntity(em)).block();
        commande.setReleveFacture(releveFacture);
        // Add required entity
        Acheteur acheteur;
        acheteur = em.insert(AcheteurResourceIT.createUpdatedEntity(em)).block();
        commande.setAcheteur(acheteur);
        return commande;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_commande__produit").block();
            em.deleteAll(Commande.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CarteBancaireResourceIT.deleteEntities(em);
        ProduitResourceIT.deleteEntities(em);
        ReleveFactureResourceIT.deleteEntities(em);
        AcheteurResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        commande = createEntity(em);
    }

    @Test
    void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().collectList().block().size();
        // Create the Commande
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testCommande.getValided()).isEqualTo(DEFAULT_VALIDED);
        assertThat(testCommande.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createCommandeWithExistingId() throws Exception {
        // Create the Commande with an existing ID
        commande.setId(1L);

        int databaseSizeBeforeCreate = commandeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCommandesAsStream() {
        // Initialize the database
        commandeRepository.save(commande).block();

        List<Commande> commandeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Commande.class)
            .getResponseBody()
            .filter(commande::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(commandeList).isNotNull();
        assertThat(commandeList).hasSize(1);
        Commande testCommande = commandeList.get(0);
        assertThat(testCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testCommande.getValided()).isEqualTo(DEFAULT_VALIDED);
        assertThat(testCommande.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllCommandes() {
        // Initialize the database
        commandeRepository.save(commande).block();

        // Get all the commandeList
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
            .value(hasItem(commande.getId().intValue()))
            .jsonPath("$.[*].montant")
            .value(hasItem(DEFAULT_MONTANT.doubleValue()))
            .jsonPath("$.[*].valided")
            .value(hasItem(DEFAULT_VALIDED))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandesWithEagerRelationshipsIsEnabled() {
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(commandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandesWithEagerRelationshipsIsNotEnabled() {
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(commandeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCommande() {
        // Initialize the database
        commandeRepository.save(commande).block();

        // Get the commande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(commande.getId().intValue()))
            .jsonPath("$.montant")
            .value(is(DEFAULT_MONTANT.doubleValue()))
            .jsonPath("$.valided")
            .value(is(DEFAULT_VALIDED))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingCommande() {
        // Get the commande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCommande() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).block();
        updatedCommande.montant(UPDATED_MONTANT).valided(UPDATED_VALIDED).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCommande.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCommande.getValided()).isEqualTo(UPDATED_VALIDED);
        assertThat(testCommande.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCommande.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.montant(UPDATED_MONTANT).createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCommande.getValided()).isEqualTo(DEFAULT_VALIDED);
        assertThat(testCommande.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void fullUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.montant(UPDATED_MONTANT).valided(UPDATED_VALIDED).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCommande.getValided()).isEqualTo(UPDATED_VALIDED);
        assertThat(testCommande.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCommande.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commande))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommande() {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeDelete = commandeRepository.findAll().collectList().block().size();

        // Delete the commande
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.CarteBancaire;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.repository.CarteBancaireRepository;
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
 * Integration tests for the {@link CarteBancaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CarteBancaireResourceIT {

    private static final String DEFAULT_REF_CARTE = "AAAAAAAAAA";
    private static final String UPDATED_REF_CARTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/carte-bancaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarteBancaireRepository carteBancaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CarteBancaire carteBancaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarteBancaire createEntity(EntityManager em) {
        CarteBancaire carteBancaire = new CarteBancaire()
            .refCarte(DEFAULT_REF_CARTE)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createEntity(em)).block();
        carteBancaire.setCommande(commande);
        return carteBancaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarteBancaire createUpdatedEntity(EntityManager em) {
        CarteBancaire carteBancaire = new CarteBancaire()
            .refCarte(UPDATED_REF_CARTE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        // Add required entity
        Commande commande;
        commande = em.insert(CommandeResourceIT.createUpdatedEntity(em)).block();
        carteBancaire.setCommande(commande);
        return carteBancaire;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CarteBancaire.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CommandeResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        carteBancaire = createEntity(em);
    }

    @Test
    void createCarteBancaire() throws Exception {
        int databaseSizeBeforeCreate = carteBancaireRepository.findAll().collectList().block().size();
        // Create the CarteBancaire
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeCreate + 1);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getRefCarte()).isEqualTo(DEFAULT_REF_CARTE);
        assertThat(testCarteBancaire.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCarteBancaire.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void createCarteBancaireWithExistingId() throws Exception {
        // Create the CarteBancaire with an existing ID
        carteBancaire.setId(1L);

        int databaseSizeBeforeCreate = carteBancaireRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCarteBancairesAsStream() {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        List<CarteBancaire> carteBancaireList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CarteBancaire.class)
            .getResponseBody()
            .filter(carteBancaire::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(carteBancaireList).isNotNull();
        assertThat(carteBancaireList).hasSize(1);
        CarteBancaire testCarteBancaire = carteBancaireList.get(0);
        assertThat(testCarteBancaire.getRefCarte()).isEqualTo(DEFAULT_REF_CARTE);
        assertThat(testCarteBancaire.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCarteBancaire.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void getAllCarteBancaires() {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        // Get all the carteBancaireList
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
            .value(hasItem(carteBancaire.getId().intValue()))
            .jsonPath("$.[*].refCarte")
            .value(hasItem(DEFAULT_REF_CARTE))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updateAt")
            .value(hasItem(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getCarteBancaire() {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        // Get the carteBancaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, carteBancaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(carteBancaire.getId().intValue()))
            .jsonPath("$.refCarte")
            .value(is(DEFAULT_REF_CARTE))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updateAt")
            .value(is(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    void getNonExistingCarteBancaire() {
        // Get the carteBancaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCarteBancaire() throws Exception {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();

        // Update the carteBancaire
        CarteBancaire updatedCarteBancaire = carteBancaireRepository.findById(carteBancaire.getId()).block();
        updatedCarteBancaire.refCarte(UPDATED_REF_CARTE).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCarteBancaire.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCarteBancaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getRefCarte()).isEqualTo(UPDATED_REF_CARTE);
        assertThat(testCarteBancaire.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCarteBancaire.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void putNonExistingCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();
        carteBancaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, carteBancaire.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCarteBancaireWithPatch() throws Exception {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();

        // Update the carteBancaire using partial update
        CarteBancaire partialUpdatedCarteBancaire = new CarteBancaire();
        partialUpdatedCarteBancaire.setId(carteBancaire.getId());

        partialUpdatedCarteBancaire.createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCarteBancaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCarteBancaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getRefCarte()).isEqualTo(DEFAULT_REF_CARTE);
        assertThat(testCarteBancaire.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCarteBancaire.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    void fullUpdateCarteBancaireWithPatch() throws Exception {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();

        // Update the carteBancaire using partial update
        CarteBancaire partialUpdatedCarteBancaire = new CarteBancaire();
        partialUpdatedCarteBancaire.setId(carteBancaire.getId());

        partialUpdatedCarteBancaire.refCarte(UPDATED_REF_CARTE).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCarteBancaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCarteBancaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getRefCarte()).isEqualTo(UPDATED_REF_CARTE);
        assertThat(testCarteBancaire.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCarteBancaire.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    void patchNonExistingCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();
        carteBancaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, carteBancaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().collectList().block().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(carteBancaire))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCarteBancaire() {
        // Initialize the database
        carteBancaireRepository.save(carteBancaire).block();

        int databaseSizeBeforeDelete = carteBancaireRepository.findAll().collectList().block().size();

        // Delete the carteBancaire
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, carteBancaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll().collectList().block();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

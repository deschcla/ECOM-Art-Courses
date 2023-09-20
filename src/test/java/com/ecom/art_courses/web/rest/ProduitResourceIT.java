package com.ecom.art_courses.web.rest;

import static com.ecom.art_courses.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.ProduitRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restProduitMockMvc;

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
        if (TestUtil.findAll(em, SousCategorie.class).isEmpty()) {
            sousCategorie = SousCategorieResourceIT.createEntity(em);
            em.persist(sousCategorie);
            em.flush();
        } else {
            sousCategorie = TestUtil.findAll(em, SousCategorie.class).get(0);
        }
        produit.setSouscategorie(sousCategorie);
        // Add required entity
        Commande commande;
        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
            commande = CommandeResourceIT.createEntity(em);
            em.persist(commande);
            em.flush();
        } else {
            commande = TestUtil.findAll(em, Commande.class).get(0);
        }
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
        if (TestUtil.findAll(em, SousCategorie.class).isEmpty()) {
            sousCategorie = SousCategorieResourceIT.createUpdatedEntity(em);
            em.persist(sousCategorie);
            em.flush();
        } else {
            sousCategorie = TestUtil.findAll(em, SousCategorie.class).get(0);
        }
        produit.setSouscategorie(sousCategorie);
        // Add required entity
        Commande commande;
        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
            commande = CommandeResourceIT.createUpdatedEntity(em);
            em.persist(commande);
            em.flush();
        } else {
            commande = TestUtil.findAll(em, Commande.class).get(0);
        }
        produit.getCommandes().add(commande);
        return produit;
    }

    @BeforeEach
    public void initTest() {
        produit = createEntity(em);
    }

    @Test
    @Transactional
    void createProduit() throws Exception {
        int databaseSizeBeforeCreate = produitRepository.findAll().size();
        // Create the Produit
        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isCreated());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
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
    @Transactional
    void createProduitWithExistingId() throws Exception {
        // Create the Produit with an existing ID
        produit.setId(1L);

        int databaseSizeBeforeCreate = produitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProduits() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProduit").value(hasItem(DEFAULT_NOM_PRODUIT)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].tarifUnit").value(hasItem(DEFAULT_TARIF_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].lienImg").value(hasItem(DEFAULT_LIEN_IMG)))
            .andExpect(jsonPath("$.[*].quantiteTotale").value(hasItem(DEFAULT_QUANTITE_TOTALE)))
            .andExpect(jsonPath("$.[*].quantiteDispo").value(hasItem(DEFAULT_QUANTITE_DISPO)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getProduit() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get the produit
        restProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produit.getId().intValue()))
            .andExpect(jsonPath("$.nomProduit").value(DEFAULT_NOM_PRODUIT))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.tarifUnit").value(DEFAULT_TARIF_UNIT.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.lienImg").value(DEFAULT_LIEN_IMG))
            .andExpect(jsonPath("$.quantiteTotale").value(DEFAULT_QUANTITE_TOTALE))
            .andExpect(jsonPath("$.quantiteDispo").value(DEFAULT_QUANTITE_DISPO))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProduit() throws Exception {
        // Get the produit
        restProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduit() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

        // Update the produit
        Produit updatedProduit = produitRepository.findById(produit.getId()).get();
        // Disconnect from session so that the updates on updatedProduit are not directly saved in db
        em.detach(updatedProduit);
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

        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
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
    @Transactional
    void putNonExistingProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();
        produit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(produit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit.tarifUnit(UPDATED_TARIF_UNIT).date(UPDATED_DATE).duree(UPDATED_DUREE).updateAt(UPDATED_UPDATE_AT);

        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(DEFAULT_NOM_PRODUIT);
        assertThat(testProduit.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testProduit.getTarifUnit()).isEqualTo(UPDATED_TARIF_UNIT);
        assertThat(testProduit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProduit.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testProduit.getLienImg()).isEqualTo(DEFAULT_LIEN_IMG);
        assertThat(testProduit.getQuantiteTotale()).isEqualTo(DEFAULT_QUANTITE_TOTALE);
        assertThat(testProduit.getQuantiteDispo()).isEqualTo(DEFAULT_QUANTITE_DISPO);
        assertThat(testProduit.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProduit.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

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

        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
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
    @Transactional
    void patchNonExistingProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();
        produit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(produit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(produit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();
        produit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduit() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        int databaseSizeBeforeDelete = produitRepository.findAll().size();

        // Delete the produit
        restProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, produit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

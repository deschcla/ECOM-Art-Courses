package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.repository.LigneCommandeRepository;
import java.time.Instant;
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
 * Integration tests for the {@link LigneCommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restLigneCommandeMockMvc;

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
        if (TestUtil.findAll(em, Produit.class).isEmpty()) {
            produit = ProduitResourceIT.createEntity(em);
            em.persist(produit);
            em.flush();
        } else {
            produit = TestUtil.findAll(em, Produit.class).get(0);
        }
        ligneCommande.setProduit(produit);
        // Add required entity
        Commande commande;
        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
            commande = CommandeResourceIT.createEntity(em);
            em.persist(commande);
            em.flush();
        } else {
            commande = TestUtil.findAll(em, Commande.class).get(0);
        }
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
        if (TestUtil.findAll(em, Produit.class).isEmpty()) {
            produit = ProduitResourceIT.createUpdatedEntity(em);
            em.persist(produit);
            em.flush();
        } else {
            produit = TestUtil.findAll(em, Produit.class).get(0);
        }
        ligneCommande.setProduit(produit);
        // Add required entity
        Commande commande;
        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
            commande = CommandeResourceIT.createUpdatedEntity(em);
            em.persist(commande);
            em.flush();
        } else {
            commande = TestUtil.findAll(em, Commande.class).get(0);
        }
        ligneCommande.setCommande(commande);
        return ligneCommande;
    }

    @BeforeEach
    public void initTest() {
        ligneCommande = createEntity(em);
    }

    @Test
    @Transactional
    void createLigneCommande() throws Exception {
        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().size();
        // Create the LigneCommande
        restLigneCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isCreated());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
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
    @Transactional
    void createLigneCommandeWithExistingId() throws Exception {
        // Create the LigneCommande with an existing ID
        ligneCommande.setId(1L);

        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLigneCommandes() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        // Get all the ligneCommandeList
        restLigneCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED)))
            .andExpect(jsonPath("$.[*].nomParticipant").value(hasItem(DEFAULT_NOM_PARTICIPANT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        // Get the ligneCommande
        restLigneCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, ligneCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneCommande.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.validated").value(DEFAULT_VALIDATED))
            .andExpect(jsonPath("$.nomParticipant").value(DEFAULT_NOM_PARTICIPANT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLigneCommande() throws Exception {
        // Get the ligneCommande
        restLigneCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Update the ligneCommande
        LigneCommande updatedLigneCommande = ligneCommandeRepository.findById(ligneCommande.getId()).get();
        // Disconnect from session so that the updates on updatedLigneCommande are not directly saved in db
        em.detach(updatedLigneCommande);
        updatedLigneCommande
            .quantite(UPDATED_QUANTITE)
            .montant(UPDATED_MONTANT)
            .validated(UPDATED_VALIDATED)
            .nomParticipant(UPDATED_NOM_PARTICIPANT)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restLigneCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLigneCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLigneCommande))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
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
    @Transactional
    void putNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigneCommandeWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Update the ligneCommande using partial update
        LigneCommande partialUpdatedLigneCommande = new LigneCommande();
        partialUpdatedLigneCommande.setId(ligneCommande.getId());

        partialUpdatedLigneCommande.quantite(UPDATED_QUANTITE).validated(UPDATED_VALIDATED).createdAt(UPDATED_CREATED_AT);

        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommande))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testLigneCommande.getValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testLigneCommande.getNomParticipant()).isEqualTo(DEFAULT_NOM_PARTICIPANT);
        assertThat(testLigneCommande.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLigneCommande.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateLigneCommandeWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

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

        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommande))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
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
    @Transactional
    void patchNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeDelete = ligneCommandeRepository.findAll().size();

        // Delete the ligneCommande
        restLigneCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligneCommande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

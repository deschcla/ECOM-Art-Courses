package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.ReleveFactureRepository;
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
 * Integration tests for the {@link ReleveFactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restReleveFactureMockMvc;

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
        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
            commande = CommandeResourceIT.createEntity(em);
            em.persist(commande);
            em.flush();
        } else {
            commande = TestUtil.findAll(em, Commande.class).get(0);
        }
        releveFacture.getCommandes().add(commande);
        // Add required entity
        Acheteur acheteur;
        if (TestUtil.findAll(em, Acheteur.class).isEmpty()) {
            acheteur = AcheteurResourceIT.createEntity(em);
            em.persist(acheteur);
            em.flush();
        } else {
            acheteur = TestUtil.findAll(em, Acheteur.class).get(0);
        }
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
        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
            commande = CommandeResourceIT.createUpdatedEntity(em);
            em.persist(commande);
            em.flush();
        } else {
            commande = TestUtil.findAll(em, Commande.class).get(0);
        }
        releveFacture.getCommandes().add(commande);
        // Add required entity
        Acheteur acheteur;
        if (TestUtil.findAll(em, Acheteur.class).isEmpty()) {
            acheteur = AcheteurResourceIT.createUpdatedEntity(em);
            em.persist(acheteur);
            em.flush();
        } else {
            acheteur = TestUtil.findAll(em, Acheteur.class).get(0);
        }
        releveFacture.setAcheteur(acheteur);
        return releveFacture;
    }

    @BeforeEach
    public void initTest() {
        releveFacture = createEntity(em);
    }

    @Test
    @Transactional
    void createReleveFacture() throws Exception {
        int databaseSizeBeforeCreate = releveFactureRepository.findAll().size();
        // Create the ReleveFacture
        restReleveFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveFacture)))
            .andExpect(status().isCreated());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeCreate + 1);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void createReleveFactureWithExistingId() throws Exception {
        // Create the ReleveFacture with an existing ID
        releveFacture.setId(1L);

        int databaseSizeBeforeCreate = releveFactureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleveFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveFacture)))
            .andExpect(status().isBadRequest());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReleveFactures() throws Exception {
        // Initialize the database
        releveFactureRepository.saveAndFlush(releveFacture);

        // Get all the releveFactureList
        restReleveFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releveFacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getReleveFacture() throws Exception {
        // Initialize the database
        releveFactureRepository.saveAndFlush(releveFacture);

        // Get the releveFacture
        restReleveFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, releveFacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releveFacture.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReleveFacture() throws Exception {
        // Get the releveFacture
        restReleveFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReleveFacture() throws Exception {
        // Initialize the database
        releveFactureRepository.saveAndFlush(releveFacture);

        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();

        // Update the releveFacture
        ReleveFacture updatedReleveFacture = releveFactureRepository.findById(releveFacture.getId()).get();
        // Disconnect from session so that the updates on updatedReleveFacture are not directly saved in db
        em.detach(updatedReleveFacture);
        updatedReleveFacture.montant(UPDATED_MONTANT).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        restReleveFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReleveFacture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReleveFacture))
            )
            .andExpect(status().isOk());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void putNonExistingReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();
        releveFacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleveFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releveFacture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releveFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releveFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releveFacture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReleveFactureWithPatch() throws Exception {
        // Initialize the database
        releveFactureRepository.saveAndFlush(releveFacture);

        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();

        // Update the releveFacture using partial update
        ReleveFacture partialUpdatedReleveFacture = new ReleveFacture();
        partialUpdatedReleveFacture.setId(releveFacture.getId());

        partialUpdatedReleveFacture.createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        restReleveFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleveFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleveFacture))
            )
            .andExpect(status().isOk());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateReleveFactureWithPatch() throws Exception {
        // Initialize the database
        releveFactureRepository.saveAndFlush(releveFacture);

        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();

        // Update the releveFacture using partial update
        ReleveFacture partialUpdatedReleveFacture = new ReleveFacture();
        partialUpdatedReleveFacture.setId(releveFacture.getId());

        partialUpdatedReleveFacture.montant(UPDATED_MONTANT).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        restReleveFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleveFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleveFacture))
            )
            .andExpect(status().isOk());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
        ReleveFacture testReleveFacture = releveFactureList.get(releveFactureList.size() - 1);
        assertThat(testReleveFacture.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testReleveFacture.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReleveFacture.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void patchNonExistingReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();
        releveFacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleveFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, releveFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releveFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releveFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReleveFacture() throws Exception {
        int databaseSizeBeforeUpdate = releveFactureRepository.findAll().size();
        releveFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveFactureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(releveFacture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleveFacture in the database
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReleveFacture() throws Exception {
        // Initialize the database
        releveFactureRepository.saveAndFlush(releveFacture);

        int databaseSizeBeforeDelete = releveFactureRepository.findAll().size();

        // Delete the releveFacture
        restReleveFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, releveFacture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleveFacture> releveFactureList = releveFactureRepository.findAll();
        assertThat(releveFactureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

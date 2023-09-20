package com.ecom.art_courses.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.art_courses.IntegrationTest;
import com.ecom.art_courses.domain.Categorie;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.SousCategorieRepository;
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
 * Integration tests for the {@link SousCategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restSousCategorieMockMvc;

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
        if (TestUtil.findAll(em, Produit.class).isEmpty()) {
            produit = ProduitResourceIT.createEntity(em);
            em.persist(produit);
            em.flush();
        } else {
            produit = TestUtil.findAll(em, Produit.class).get(0);
        }
        sousCategorie.getProduits().add(produit);
        // Add required entity
        Categorie categorie;
        if (TestUtil.findAll(em, Categorie.class).isEmpty()) {
            categorie = CategorieResourceIT.createEntity(em);
            em.persist(categorie);
            em.flush();
        } else {
            categorie = TestUtil.findAll(em, Categorie.class).get(0);
        }
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
        if (TestUtil.findAll(em, Produit.class).isEmpty()) {
            produit = ProduitResourceIT.createUpdatedEntity(em);
            em.persist(produit);
            em.flush();
        } else {
            produit = TestUtil.findAll(em, Produit.class).get(0);
        }
        sousCategorie.getProduits().add(produit);
        // Add required entity
        Categorie categorie;
        if (TestUtil.findAll(em, Categorie.class).isEmpty()) {
            categorie = CategorieResourceIT.createUpdatedEntity(em);
            em.persist(categorie);
            em.flush();
        } else {
            categorie = TestUtil.findAll(em, Categorie.class).get(0);
        }
        sousCategorie.setCategorie(categorie);
        return sousCategorie;
    }

    @BeforeEach
    public void initTest() {
        sousCategorie = createEntity(em);
    }

    @Test
    @Transactional
    void createSousCategorie() throws Exception {
        int databaseSizeBeforeCreate = sousCategorieRepository.findAll().size();
        // Create the SousCategorie
        restSousCategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousCategorie)))
            .andExpect(status().isCreated());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeCreate + 1);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(DEFAULT_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void createSousCategorieWithExistingId() throws Exception {
        // Create the SousCategorie with an existing ID
        sousCategorie.setId(1L);

        int databaseSizeBeforeCreate = sousCategorieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousCategorieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousCategorie)))
            .andExpect(status().isBadRequest());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSousCategories() throws Exception {
        // Initialize the database
        sousCategorieRepository.saveAndFlush(sousCategorie);

        // Get all the sousCategorieList
        restSousCategorieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousCategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeSousCategorie").value(hasItem(DEFAULT_TYPE_SOUS_CATEGORIE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getSousCategorie() throws Exception {
        // Initialize the database
        sousCategorieRepository.saveAndFlush(sousCategorie);

        // Get the sousCategorie
        restSousCategorieMockMvc
            .perform(get(ENTITY_API_URL_ID, sousCategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousCategorie.getId().intValue()))
            .andExpect(jsonPath("$.typeSousCategorie").value(DEFAULT_TYPE_SOUS_CATEGORIE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSousCategorie() throws Exception {
        // Get the sousCategorie
        restSousCategorieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSousCategorie() throws Exception {
        // Initialize the database
        sousCategorieRepository.saveAndFlush(sousCategorie);

        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();

        // Update the sousCategorie
        SousCategorie updatedSousCategorie = sousCategorieRepository.findById(sousCategorie.getId()).get();
        // Disconnect from session so that the updates on updatedSousCategorie are not directly saved in db
        em.detach(updatedSousCategorie);
        updatedSousCategorie.typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE).createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        restSousCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousCategorie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousCategorie))
            )
            .andExpect(status().isOk());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(UPDATED_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void putNonExistingSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();
        sousCategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousCategorie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousCategorieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousCategorieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousCategorie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousCategorieWithPatch() throws Exception {
        // Initialize the database
        sousCategorieRepository.saveAndFlush(sousCategorie);

        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();

        // Update the sousCategorie using partial update
        SousCategorie partialUpdatedSousCategorie = new SousCategorie();
        partialUpdatedSousCategorie.setId(sousCategorie.getId());

        partialUpdatedSousCategorie.typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE).createdAt(UPDATED_CREATED_AT);

        restSousCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousCategorie))
            )
            .andExpect(status().isOk());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(UPDATED_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateSousCategorieWithPatch() throws Exception {
        // Initialize the database
        sousCategorieRepository.saveAndFlush(sousCategorie);

        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();

        // Update the sousCategorie using partial update
        SousCategorie partialUpdatedSousCategorie = new SousCategorie();
        partialUpdatedSousCategorie.setId(sousCategorie.getId());

        partialUpdatedSousCategorie
            .typeSousCategorie(UPDATED_TYPE_SOUS_CATEGORIE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restSousCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousCategorie))
            )
            .andExpect(status().isOk());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
        SousCategorie testSousCategorie = sousCategorieList.get(sousCategorieList.size() - 1);
        assertThat(testSousCategorie.getTypeSousCategorie()).isEqualTo(UPDATED_TYPE_SOUS_CATEGORIE);
        assertThat(testSousCategorie.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSousCategorie.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();
        sousCategorie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousCategorie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousCategorie))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousCategorie() throws Exception {
        int databaseSizeBeforeUpdate = sousCategorieRepository.findAll().size();
        sousCategorie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousCategorieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousCategorie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousCategorie in the database
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousCategorie() throws Exception {
        // Initialize the database
        sousCategorieRepository.saveAndFlush(sousCategorie);

        int databaseSizeBeforeDelete = sousCategorieRepository.findAll().size();

        // Delete the sousCategorie
        restSousCategorieMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousCategorie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousCategorie> sousCategorieList = sousCategorieRepository.findAll();
        assertThat(sousCategorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

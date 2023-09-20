//package com.ecom.art_courses.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.ecom.art_courses.IntegrationTest;
////import com.ecom.art_courses.domain.Acheteur;
//import com.ecom.art_courses.domain.Commande;
//import com.ecom.art_courses.domain.ReleveFacture;
//import com.ecom.art_courses.domain.User;
////import com.ecom.art_courses.repository.AcheteurRepository;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link AcheteurResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class AcheteurResourceIT {
//
//    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
//    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";
//
//    private static final LocalDate DEFAULT_DATE_NAISS = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_DATE_NAISS = LocalDate.now(ZoneId.systemDefault());
//
//    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
//    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";
//
//    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final String ENTITY_API_URL = "/api/acheteurs";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private AcheteurRepository acheteurRepository;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restAcheteurMockMvc;
//
//    private Acheteur acheteur;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Acheteur createEntity(EntityManager em) {
//        Acheteur acheteur = new Acheteur()
//            .adresse(DEFAULT_ADRESSE)
//            .dateNaiss(DEFAULT_DATE_NAISS)
//            .numTel(DEFAULT_NUM_TEL)
//            .createdAt(DEFAULT_CREATED_AT)
//            .updateAt(DEFAULT_UPDATE_AT);
//        // Add required entity
//        User user = UserResourceIT.createEntity(em);
//        em.persist(user);
//        em.flush();
//        acheteur.setInternalUser(user);
//        // Add required entity
//        ReleveFacture releveFacture;
//        if (TestUtil.findAll(em, ReleveFacture.class).isEmpty()) {
//            releveFacture = ReleveFactureResourceIT.createEntity(em);
//            em.persist(releveFacture);
//            em.flush();
//        } else {
//            releveFacture = TestUtil.findAll(em, ReleveFacture.class).get(0);
//        }
//        acheteur.getReleveFactures().add(releveFacture);
//        // Add required entity
//        Commande commande;
//        if (TestUtil.findAll(em, Commande.class).isEmpty()) {
//            commande = CommandeResourceIT.createEntity(em);
//            em.persist(commande);
//            em.flush();
//        } else {
//            commande = TestUtil.findAll(em, Commande.class).get(0);
//        }
//        acheteur.getCommandes().add(commande);
//        return acheteur;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Acheteur createUpdatedEntity(EntityManager em) {
//        Acheteur acheteur = new Acheteur()
//            .adresse(UPDATED_ADRESSE)
//            .dateNaiss(UPDATED_DATE_NAISS)
//            .numTel(UPDATED_NUM_TEL)
//            .createdAt(UPDATED_CREATED_AT)
//            .updateAt(UPDATED_UPDATE_AT);
//        // Add required entity
//        User user = UserResourceIT.createEntity(em);
//        em.persist(user);
//        em.flush();
//        acheteur.setInternalUser(user);
//        // Add required entity
//        ReleveFacture releveFacture;
//        releveFacture = ReleveFactureResourceIT.createUpdatedEntity(em);
//        em.persist(releveFacture);
//        em.flush();
//        acheteur.getReleveFactures().add(releveFacture);
//        // Add required entity
//        Commande commande;
//        commande = CommandeResourceIT.createUpdatedEntity(em);
//        em.persist(commande);
//        em.flush();
//        acheteur.getCommandes().add(commande);
//        return acheteur;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        acheteur = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createAcheteur() throws Exception {
//        int databaseSizeBeforeCreate = acheteurRepository.findAll().size();
//        // Create the Acheteur
//        restAcheteurMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteur)))
//            .andExpect(status().isCreated());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate + 1);
//        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
//        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
//        assertThat(testAcheteur.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
//        assertThat(testAcheteur.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
//        assertThat(testAcheteur.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
//        assertThat(testAcheteur.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
//        // Validate the id for MapsId, the ids must be same
//        //        assertThat(testAcheteur.getId()).isEqualTo(testAcheteur.getUser().getId());
//    }
//
//    @Test
//    @Transactional
//    void createAcheteurWithExistingId() throws Exception {
//        // Create the Acheteur with an existing ID
//        acheteur.setId(1L);
//
//        int databaseSizeBeforeCreate = acheteurRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restAcheteurMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteur)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void updateAcheteurMapsIdAssociationWithNewId() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//        int databaseSizeBeforeCreate = acheteurRepository.findAll().size();
//        // Add a new parent entity
//        User user = UserResourceIT.createEntity(em);
//        em.persist(user);
//        em.flush();
//
//        // Load the acheteur
//        Acheteur updatedAcheteur = acheteurRepository.findById(acheteur.getId()).get();
//        assertThat(updatedAcheteur).isNotNull();
//        // Disconnect from session so that the updates on updatedAcheteur are not directly saved in db
//        em.detach(updatedAcheteur);
//
//        // Update the User with new association value
//        //        updatedAcheteur.setUser(user);
//
//        // Update the entity
//        restAcheteurMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedAcheteur.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedAcheteur))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeCreate);
//        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
//        // Validate the id for MapsId, the ids must be same
//        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
//        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
//        // assertThat(testAcheteur.getId()).isEqualTo(testAcheteur.getUser().getId());
//    }
//
//    @Test
//    @Transactional
//    void getAllAcheteurs() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//
//        // Get all the acheteurList
//        restAcheteurMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(acheteur.getId().intValue())))
//            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
//            .andExpect(jsonPath("$.[*].dateNaiss").value(hasItem(DEFAULT_DATE_NAISS.toString())))
//            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
//            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
//            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
//    }
//
//    @Test
//    @Transactional
//    void getAcheteur() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//
//        // Get the acheteur
//        restAcheteurMockMvc
//            .perform(get(ENTITY_API_URL_ID, acheteur.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(acheteur.getId().intValue()))
//            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
//            .andExpect(jsonPath("$.dateNaiss").value(DEFAULT_DATE_NAISS.toString()))
//            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
//            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingAcheteur() throws Exception {
//        // Get the acheteur
//        restAcheteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putExistingAcheteur() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//
//        // Update the acheteur
//        Acheteur updatedAcheteur = acheteurRepository.findById(acheteur.getId()).get();
//        // Disconnect from session so that the updates on updatedAcheteur are not directly saved in db
//        em.detach(updatedAcheteur);
//        updatedAcheteur
//            .adresse(UPDATED_ADRESSE)
//            .dateNaiss(UPDATED_DATE_NAISS)
//            .numTel(UPDATED_NUM_TEL)
//            .createdAt(UPDATED_CREATED_AT)
//            .updateAt(UPDATED_UPDATE_AT);
//
//        restAcheteurMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedAcheteur.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedAcheteur))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
//        assertThat(testAcheteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
//        assertThat(testAcheteur.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
//        assertThat(testAcheteur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
//        assertThat(testAcheteur.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testAcheteur.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingAcheteur() throws Exception {
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//        acheteur.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restAcheteurMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, acheteur.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(acheteur))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchAcheteur() throws Exception {
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//        acheteur.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAcheteurMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(acheteur))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamAcheteur() throws Exception {
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//        acheteur.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAcheteurMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acheteur)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateAcheteurWithPatch() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//
//        // Update the acheteur using partial update
//        Acheteur partialUpdatedAcheteur = new Acheteur();
//        partialUpdatedAcheteur.setId(acheteur.getId());
//
//        partialUpdatedAcheteur.createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);
//
//        restAcheteurMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedAcheteur.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcheteur))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
//        assertThat(testAcheteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
//        assertThat(testAcheteur.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
//        assertThat(testAcheteur.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
//        assertThat(testAcheteur.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testAcheteur.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateAcheteurWithPatch() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//
//        // Update the acheteur using partial update
//        Acheteur partialUpdatedAcheteur = new Acheteur();
//        partialUpdatedAcheteur.setId(acheteur.getId());
//
//        partialUpdatedAcheteur
//            .adresse(UPDATED_ADRESSE)
//            .dateNaiss(UPDATED_DATE_NAISS)
//            .numTel(UPDATED_NUM_TEL)
//            .createdAt(UPDATED_CREATED_AT)
//            .updateAt(UPDATED_UPDATE_AT);
//
//        restAcheteurMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedAcheteur.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcheteur))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//        Acheteur testAcheteur = acheteurList.get(acheteurList.size() - 1);
//        assertThat(testAcheteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
//        assertThat(testAcheteur.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
//        assertThat(testAcheteur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
//        assertThat(testAcheteur.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testAcheteur.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingAcheteur() throws Exception {
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//        acheteur.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restAcheteurMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, acheteur.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(acheteur))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchAcheteur() throws Exception {
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//        acheteur.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAcheteurMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(acheteur))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamAcheteur() throws Exception {
//        int databaseSizeBeforeUpdate = acheteurRepository.findAll().size();
//        acheteur.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAcheteurMockMvc
//            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acheteur)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Acheteur in the database
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteAcheteur() throws Exception {
//        // Initialize the database
//        acheteurRepository.saveAndFlush(acheteur);
//
//        int databaseSizeBeforeDelete = acheteurRepository.findAll().size();
//
//        // Delete the acheteur
//        restAcheteurMockMvc
//            .perform(delete(ENTITY_API_URL_ID, acheteur.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Acheteur> acheteurList = acheteurRepository.findAll();
//        assertThat(acheteurList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}

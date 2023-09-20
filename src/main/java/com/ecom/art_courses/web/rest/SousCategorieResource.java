package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.SousCategorie;
import com.ecom.art_courses.repository.SousCategorieRepository;
import com.ecom.art_courses.service.SousCategorieService;
import com.ecom.art_courses.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link SousCategorie}.
 */
@RestController
@RequestMapping("/api")
public class SousCategorieResource {

    private final Logger log = LoggerFactory.getLogger(SousCategorieResource.class);

    private static final String ENTITY_NAME = "sousCategorie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousCategorieService sousCategorieService;

    private final SousCategorieRepository sousCategorieRepository;

    public SousCategorieResource(SousCategorieService sousCategorieService, SousCategorieRepository sousCategorieRepository) {
        this.sousCategorieService = sousCategorieService;
        this.sousCategorieRepository = sousCategorieRepository;
    }

    /**
     * {@code POST  /sous-categories} : Create a new sousCategorie.
     *
     * @param sousCategorie the sousCategorie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousCategorie, or with status {@code 400 (Bad Request)} if the sousCategorie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-categories")
    public ResponseEntity<SousCategorie> createSousCategorie(@Valid @RequestBody SousCategorie sousCategorie) throws URISyntaxException {
        log.debug("REST request to save SousCategorie : {}", sousCategorie);
        if (sousCategorie.getId() != null) {
            throw new BadRequestAlertException("A new sousCategorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousCategorie result = sousCategorieService.save(sousCategorie);
        return ResponseEntity
            .created(new URI("/api/sous-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-categories/:id} : Updates an existing sousCategorie.
     *
     * @param id the id of the sousCategorie to save.
     * @param sousCategorie the sousCategorie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousCategorie,
     * or with status {@code 400 (Bad Request)} if the sousCategorie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousCategorie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-categories/{id}")
    public ResponseEntity<SousCategorie> updateSousCategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousCategorie sousCategorie
    ) throws URISyntaxException {
        log.debug("REST request to update SousCategorie : {}, {}", id, sousCategorie);
        if (sousCategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousCategorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousCategorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousCategorie result = sousCategorieService.update(sousCategorie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousCategorie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-categories/:id} : Partial updates given fields of an existing sousCategorie, field will ignore if it is null
     *
     * @param id the id of the sousCategorie to save.
     * @param sousCategorie the sousCategorie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousCategorie,
     * or with status {@code 400 (Bad Request)} if the sousCategorie is not valid,
     * or with status {@code 404 (Not Found)} if the sousCategorie is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousCategorie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousCategorie> partialUpdateSousCategorie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousCategorie sousCategorie
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousCategorie partially : {}, {}", id, sousCategorie);
        if (sousCategorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousCategorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousCategorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousCategorie> result = sousCategorieService.partialUpdate(sousCategorie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousCategorie.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-categories} : get all the sousCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousCategories in body.
     */
    @GetMapping("/sous-categories")
    public List<SousCategorie> getAllSousCategories() {
        log.debug("REST request to get all SousCategories");
        return sousCategorieService.findAll();
    }

    /**
     * {@code GET  /sous-categories/:id} : get the "id" sousCategorie.
     *
     * @param id the id of the sousCategorie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousCategorie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-categories/{id}")
    public ResponseEntity<SousCategorie> getSousCategorie(@PathVariable Long id) {
        log.debug("REST request to get SousCategorie : {}", id);
        Optional<SousCategorie> sousCategorie = sousCategorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousCategorie);
    }

    /**
     * {@code DELETE  /sous-categories/:id} : delete the "id" sousCategorie.
     *
     * @param id the id of the sousCategorie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-categories/{id}")
    public ResponseEntity<Void> deleteSousCategorie(@PathVariable Long id) {
        log.debug("REST request to delete SousCategorie : {}", id);
        sousCategorieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.ReleveFactureRepository;
import com.ecom.art_courses.service.ReleveFactureService;
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
 * REST controller for managing {@link ReleveFacture}.
 */
@RestController
@RequestMapping("/api")
public class ReleveFactureResource {

    private final Logger log = LoggerFactory.getLogger(ReleveFactureResource.class);

    private static final String ENTITY_NAME = "releveFacture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleveFactureService releveFactureService;

    private final ReleveFactureRepository releveFactureRepository;

    public ReleveFactureResource(ReleveFactureService releveFactureService, ReleveFactureRepository releveFactureRepository) {
        this.releveFactureService = releveFactureService;
        this.releveFactureRepository = releveFactureRepository;
    }

    /**
     * {@code POST  /releve-factures} : Create a new releveFacture.
     *
     * @param releveFacture the releveFacture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releveFacture, or with status {@code 400 (Bad Request)} if the releveFacture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/releve-factures")
    public ResponseEntity<ReleveFacture> createReleveFacture(@Valid @RequestBody ReleveFacture releveFacture) throws URISyntaxException {
        log.debug("REST request to save ReleveFacture : {}", releveFacture);
        if (releveFacture.getId() != null) {
            throw new BadRequestAlertException("A new releveFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleveFacture result = releveFactureService.save(releveFacture);
        return ResponseEntity
            .created(new URI("/api/releve-factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /releve-factures/:id} : Updates an existing releveFacture.
     *
     * @param id the id of the releveFacture to save.
     * @param releveFacture the releveFacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releveFacture,
     * or with status {@code 400 (Bad Request)} if the releveFacture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releveFacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/releve-factures/{id}")
    public ResponseEntity<ReleveFacture> updateReleveFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReleveFacture releveFacture
    ) throws URISyntaxException {
        log.debug("REST request to update ReleveFacture : {}, {}", id, releveFacture);
        if (releveFacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releveFacture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releveFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReleveFacture result = releveFactureService.update(releveFacture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releveFacture.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /releve-factures/:id} : Partial updates given fields of an existing releveFacture, field will ignore if it is null
     *
     * @param id the id of the releveFacture to save.
     * @param releveFacture the releveFacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releveFacture,
     * or with status {@code 400 (Bad Request)} if the releveFacture is not valid,
     * or with status {@code 404 (Not Found)} if the releveFacture is not found,
     * or with status {@code 500 (Internal Server Error)} if the releveFacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/releve-factures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReleveFacture> partialUpdateReleveFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReleveFacture releveFacture
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReleveFacture partially : {}, {}", id, releveFacture);
        if (releveFacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releveFacture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releveFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReleveFacture> result = releveFactureService.partialUpdate(releveFacture);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releveFacture.getId().toString())
        );
    }

    /**
     * {@code GET  /releve-factures} : get all the releveFactures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releveFactures in body.
     */
    @GetMapping("/releve-factures")
    public List<ReleveFacture> getAllReleveFactures() {
        log.debug("REST request to get all ReleveFactures");
        return releveFactureService.findAll();
    }

    /**
     * {@code GET  /releve-factures/:id} : get the "id" releveFacture.
     *
     * @param id the id of the releveFacture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releveFacture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/releve-factures/{id}")
    public ResponseEntity<ReleveFacture> getReleveFacture(@PathVariable Long id) {
        log.debug("REST request to get ReleveFacture : {}", id);
        Optional<ReleveFacture> releveFacture = releveFactureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(releveFacture);
    }

    /**
     * {@code DELETE  /releve-factures/:id} : delete the "id" releveFacture.
     *
     * @param id the id of the releveFacture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/releve-factures/{id}")
    public ResponseEntity<Void> deleteReleveFacture(@PathVariable Long id) {
        log.debug("REST request to delete ReleveFacture : {}", id);
        releveFactureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.ReleveFactureRepository;
import com.ecom.art_courses.service.ReleveFactureService;
import com.ecom.art_courses.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.ecom.art_courses.domain.ReleveFacture}.
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
    public Mono<ResponseEntity<ReleveFacture>> createReleveFacture(@Valid @RequestBody ReleveFacture releveFacture)
        throws URISyntaxException {
        log.debug("REST request to save ReleveFacture : {}", releveFacture);
        if (releveFacture.getId() != null) {
            throw new BadRequestAlertException("A new releveFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return releveFactureService
            .save(releveFacture)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/releve-factures/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<ReleveFacture>> updateReleveFacture(
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

        return releveFactureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return releveFactureService
                    .update(releveFacture)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<ReleveFacture>> partialUpdateReleveFacture(
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

        return releveFactureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReleveFacture> result = releveFactureService.partialUpdate(releveFacture);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /releve-factures} : get all the releveFactures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releveFactures in body.
     */
    @GetMapping(value = "/releve-factures", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReleveFacture>> getAllReleveFactures() {
        log.debug("REST request to get all ReleveFactures");
        return releveFactureService.findAll().collectList();
    }

    /**
     * {@code GET  /releve-factures} : get all the releveFactures as a stream.
     * @return the {@link Flux} of releveFactures.
     */
    @GetMapping(value = "/releve-factures", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReleveFacture> getAllReleveFacturesAsStream() {
        log.debug("REST request to get all ReleveFactures as a stream");
        return releveFactureService.findAll();
    }

    /**
     * {@code GET  /releve-factures/:id} : get the "id" releveFacture.
     *
     * @param id the id of the releveFacture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releveFacture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/releve-factures/{id}")
    public Mono<ResponseEntity<ReleveFacture>> getReleveFacture(@PathVariable Long id) {
        log.debug("REST request to get ReleveFacture : {}", id);
        Mono<ReleveFacture> releveFacture = releveFactureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(releveFacture);
    }

    /**
     * {@code DELETE  /releve-factures/:id} : delete the "id" releveFacture.
     *
     * @param id the id of the releveFacture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/releve-factures/{id}")
    public Mono<ResponseEntity<Void>> deleteReleveFacture(@PathVariable Long id) {
        log.debug("REST request to delete ReleveFacture : {}", id);
        return releveFactureService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

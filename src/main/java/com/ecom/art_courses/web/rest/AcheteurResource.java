package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.Acheteur;
import com.ecom.art_courses.repository.AcheteurRepository;
import com.ecom.art_courses.service.AcheteurService;
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
 * REST controller for managing {@link com.ecom.art_courses.domain.Acheteur}.
 */
@RestController
@RequestMapping("/api")
public class AcheteurResource {

    private final Logger log = LoggerFactory.getLogger(AcheteurResource.class);

    private static final String ENTITY_NAME = "acheteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcheteurService acheteurService;

    private final AcheteurRepository acheteurRepository;

    public AcheteurResource(AcheteurService acheteurService, AcheteurRepository acheteurRepository) {
        this.acheteurService = acheteurService;
        this.acheteurRepository = acheteurRepository;
    }

    /**
     * {@code POST  /acheteurs} : Create a new acheteur.
     *
     * @param acheteur the acheteur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acheteur, or with status {@code 400 (Bad Request)} if the acheteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acheteurs")
    public Mono<ResponseEntity<Acheteur>> createAcheteur(@Valid @RequestBody Acheteur acheteur) throws URISyntaxException {
        log.debug("REST request to save Acheteur : {}", acheteur);
        if (acheteur.getId() != null) {
            throw new BadRequestAlertException("A new acheteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return acheteurService
            .save(acheteur)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/acheteurs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /acheteurs/:id} : Updates an existing acheteur.
     *
     * @param id the id of the acheteur to save.
     * @param acheteur the acheteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acheteur,
     * or with status {@code 400 (Bad Request)} if the acheteur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acheteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acheteurs/{id}")
    public Mono<ResponseEntity<Acheteur>> updateAcheteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Acheteur acheteur
    ) throws URISyntaxException {
        log.debug("REST request to update Acheteur : {}, {}", id, acheteur);
        if (acheteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acheteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return acheteurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return acheteurService
                    .update(acheteur)
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
     * {@code PATCH  /acheteurs/:id} : Partial updates given fields of an existing acheteur, field will ignore if it is null
     *
     * @param id the id of the acheteur to save.
     * @param acheteur the acheteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acheteur,
     * or with status {@code 400 (Bad Request)} if the acheteur is not valid,
     * or with status {@code 404 (Not Found)} if the acheteur is not found,
     * or with status {@code 500 (Internal Server Error)} if the acheteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acheteurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Acheteur>> partialUpdateAcheteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Acheteur acheteur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Acheteur partially : {}, {}", id, acheteur);
        if (acheteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acheteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return acheteurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Acheteur> result = acheteurService.partialUpdate(acheteur);

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
     * {@code GET  /acheteurs} : get all the acheteurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acheteurs in body.
     */
    @GetMapping(value = "/acheteurs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Acheteur>> getAllAcheteurs() {
        log.debug("REST request to get all Acheteurs");
        return acheteurService.findAll().collectList();
    }

    /**
     * {@code GET  /acheteurs} : get all the acheteurs as a stream.
     * @return the {@link Flux} of acheteurs.
     */
    @GetMapping(value = "/acheteurs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Acheteur> getAllAcheteursAsStream() {
        log.debug("REST request to get all Acheteurs as a stream");
        return acheteurService.findAll();
    }

    /**
     * {@code GET  /acheteurs/:id} : get the "id" acheteur.
     *
     * @param id the id of the acheteur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acheteur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acheteurs/{id}")
    public Mono<ResponseEntity<Acheteur>> getAcheteur(@PathVariable Long id) {
        log.debug("REST request to get Acheteur : {}", id);
        Mono<Acheteur> acheteur = acheteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acheteur);
    }

    /**
     * {@code DELETE  /acheteurs/:id} : delete the "id" acheteur.
     *
     * @param id the id of the acheteur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acheteurs/{id}")
    public Mono<ResponseEntity<Void>> deleteAcheteur(@PathVariable Long id) {
        log.debug("REST request to delete Acheteur : {}", id);
        return acheteurService
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

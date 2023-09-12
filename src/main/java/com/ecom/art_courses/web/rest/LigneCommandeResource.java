package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.repository.LigneCommandeRepository;
import com.ecom.art_courses.service.LigneCommandeService;
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
 * REST controller for managing {@link com.ecom.art_courses.domain.LigneCommande}.
 */
@RestController
@RequestMapping("/api")
public class LigneCommandeResource {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeResource.class);

    private static final String ENTITY_NAME = "ligneCommande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneCommandeService ligneCommandeService;

    private final LigneCommandeRepository ligneCommandeRepository;

    public LigneCommandeResource(LigneCommandeService ligneCommandeService, LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeService = ligneCommandeService;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    /**
     * {@code POST  /ligne-commandes} : Create a new ligneCommande.
     *
     * @param ligneCommande the ligneCommande to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneCommande, or with status {@code 400 (Bad Request)} if the ligneCommande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligne-commandes")
    public Mono<ResponseEntity<LigneCommande>> createLigneCommande(@Valid @RequestBody LigneCommande ligneCommande)
        throws URISyntaxException {
        log.debug("REST request to save LigneCommande : {}", ligneCommande);
        if (ligneCommande.getId() != null) {
            throw new BadRequestAlertException("A new ligneCommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ligneCommandeService
            .save(ligneCommande)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ligne-commandes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ligne-commandes/:id} : Updates an existing ligneCommande.
     *
     * @param id the id of the ligneCommande to save.
     * @param ligneCommande the ligneCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommande,
     * or with status {@code 400 (Bad Request)} if the ligneCommande is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligne-commandes/{id}")
    public Mono<ResponseEntity<LigneCommande>> updateLigneCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to update LigneCommande : {}, {}", id, ligneCommande);
        if (ligneCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ligneCommandeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ligneCommandeService
                    .update(ligneCommande)
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
     * {@code PATCH  /ligne-commandes/:id} : Partial updates given fields of an existing ligneCommande, field will ignore if it is null
     *
     * @param id the id of the ligneCommande to save.
     * @param ligneCommande the ligneCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommande,
     * or with status {@code 400 (Bad Request)} if the ligneCommande is not valid,
     * or with status {@code 404 (Not Found)} if the ligneCommande is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligne-commandes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LigneCommande>> partialUpdateLigneCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to partial update LigneCommande partially : {}, {}", id, ligneCommande);
        if (ligneCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ligneCommandeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LigneCommande> result = ligneCommandeService.partialUpdate(ligneCommande);

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
     * {@code GET  /ligne-commandes} : get all the ligneCommandes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneCommandes in body.
     */
    @GetMapping(value = "/ligne-commandes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<LigneCommande>> getAllLigneCommandes() {
        log.debug("REST request to get all LigneCommandes");
        return ligneCommandeService.findAll().collectList();
    }

    /**
     * {@code GET  /ligne-commandes} : get all the ligneCommandes as a stream.
     * @return the {@link Flux} of ligneCommandes.
     */
    @GetMapping(value = "/ligne-commandes", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LigneCommande> getAllLigneCommandesAsStream() {
        log.debug("REST request to get all LigneCommandes as a stream");
        return ligneCommandeService.findAll();
    }

    /**
     * {@code GET  /ligne-commandes/:id} : get the "id" ligneCommande.
     *
     * @param id the id of the ligneCommande to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneCommande, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligne-commandes/{id}")
    public Mono<ResponseEntity<LigneCommande>> getLigneCommande(@PathVariable Long id) {
        log.debug("REST request to get LigneCommande : {}", id);
        Mono<LigneCommande> ligneCommande = ligneCommandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneCommande);
    }

    /**
     * {@code DELETE  /ligne-commandes/:id} : delete the "id" ligneCommande.
     *
     * @param id the id of the ligneCommande to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligne-commandes/{id}")
    public Mono<ResponseEntity<Void>> deleteLigneCommande(@PathVariable Long id) {
        log.debug("REST request to delete LigneCommande : {}", id);
        return ligneCommandeService
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

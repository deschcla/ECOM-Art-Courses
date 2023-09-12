package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.CarteBancaire;
import com.ecom.art_courses.repository.CarteBancaireRepository;
import com.ecom.art_courses.service.CarteBancaireService;
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
 * REST controller for managing {@link com.ecom.art_courses.domain.CarteBancaire}.
 */
@RestController
@RequestMapping("/api")
public class CarteBancaireResource {

    private final Logger log = LoggerFactory.getLogger(CarteBancaireResource.class);

    private static final String ENTITY_NAME = "carteBancaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarteBancaireService carteBancaireService;

    private final CarteBancaireRepository carteBancaireRepository;

    public CarteBancaireResource(CarteBancaireService carteBancaireService, CarteBancaireRepository carteBancaireRepository) {
        this.carteBancaireService = carteBancaireService;
        this.carteBancaireRepository = carteBancaireRepository;
    }

    /**
     * {@code POST  /carte-bancaires} : Create a new carteBancaire.
     *
     * @param carteBancaire the carteBancaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carteBancaire, or with status {@code 400 (Bad Request)} if the carteBancaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carte-bancaires")
    public Mono<ResponseEntity<CarteBancaire>> createCarteBancaire(@Valid @RequestBody CarteBancaire carteBancaire)
        throws URISyntaxException {
        log.debug("REST request to save CarteBancaire : {}", carteBancaire);
        if (carteBancaire.getId() != null) {
            throw new BadRequestAlertException("A new carteBancaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return carteBancaireService
            .save(carteBancaire)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/carte-bancaires/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /carte-bancaires/:id} : Updates an existing carteBancaire.
     *
     * @param id the id of the carteBancaire to save.
     * @param carteBancaire the carteBancaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteBancaire,
     * or with status {@code 400 (Bad Request)} if the carteBancaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carteBancaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carte-bancaires/{id}")
    public Mono<ResponseEntity<CarteBancaire>> updateCarteBancaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarteBancaire carteBancaire
    ) throws URISyntaxException {
        log.debug("REST request to update CarteBancaire : {}, {}", id, carteBancaire);
        if (carteBancaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteBancaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return carteBancaireRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return carteBancaireService
                    .update(carteBancaire)
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
     * {@code PATCH  /carte-bancaires/:id} : Partial updates given fields of an existing carteBancaire, field will ignore if it is null
     *
     * @param id the id of the carteBancaire to save.
     * @param carteBancaire the carteBancaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteBancaire,
     * or with status {@code 400 (Bad Request)} if the carteBancaire is not valid,
     * or with status {@code 404 (Not Found)} if the carteBancaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the carteBancaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carte-bancaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CarteBancaire>> partialUpdateCarteBancaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarteBancaire carteBancaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarteBancaire partially : {}, {}", id, carteBancaire);
        if (carteBancaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteBancaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return carteBancaireRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CarteBancaire> result = carteBancaireService.partialUpdate(carteBancaire);

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
     * {@code GET  /carte-bancaires} : get all the carteBancaires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carteBancaires in body.
     */
    @GetMapping(value = "/carte-bancaires", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CarteBancaire>> getAllCarteBancaires() {
        log.debug("REST request to get all CarteBancaires");
        return carteBancaireService.findAll().collectList();
    }

    /**
     * {@code GET  /carte-bancaires} : get all the carteBancaires as a stream.
     * @return the {@link Flux} of carteBancaires.
     */
    @GetMapping(value = "/carte-bancaires", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CarteBancaire> getAllCarteBancairesAsStream() {
        log.debug("REST request to get all CarteBancaires as a stream");
        return carteBancaireService.findAll();
    }

    /**
     * {@code GET  /carte-bancaires/:id} : get the "id" carteBancaire.
     *
     * @param id the id of the carteBancaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carteBancaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carte-bancaires/{id}")
    public Mono<ResponseEntity<CarteBancaire>> getCarteBancaire(@PathVariable Long id) {
        log.debug("REST request to get CarteBancaire : {}", id);
        Mono<CarteBancaire> carteBancaire = carteBancaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carteBancaire);
    }

    /**
     * {@code DELETE  /carte-bancaires/:id} : delete the "id" carteBancaire.
     *
     * @param id the id of the carteBancaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carte-bancaires/{id}")
    public Mono<ResponseEntity<Void>> deleteCarteBancaire(@PathVariable Long id) {
        log.debug("REST request to delete CarteBancaire : {}", id);
        return carteBancaireService
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

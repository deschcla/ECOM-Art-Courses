package com.ecom.art_courses.web.rest;

import com.ecom.art_courses.domain.Commande;
import com.ecom.art_courses.domain.LigneCommande;
import com.ecom.art_courses.domain.Produit;
import com.ecom.art_courses.domain.ReleveFacture;
import com.ecom.art_courses.repository.CommandeRepository;
import com.ecom.art_courses.service.CommandeService;
import com.ecom.art_courses.service.LigneCommandeService;
import com.ecom.art_courses.service.ProduitService;
import com.ecom.art_courses.service.ReleveFactureService;
import com.ecom.art_courses.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
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
 * REST controller for managing {@link Commande}.
 */
@RestController
@RequestMapping("/api")
public class CommandeResource {

    private final Logger log = LoggerFactory.getLogger(CommandeResource.class);

    private static final String ENTITY_NAME = "commande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandeService commandeService;

    private final LigneCommandeService ligneCommandeService;

    private final ProduitService produitService;

    private final ReleveFactureService releveFactureService;

    private final CommandeRepository commandeRepository;

    public CommandeResource(
        CommandeService commandeService,
        CommandeRepository commandeRepository,
        LigneCommandeService ligneCommandeService,
        ProduitService produitService,
        ReleveFactureService releveFactureService
    ) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
        this.ligneCommandeService = ligneCommandeService;
        this.produitService = produitService;
        this.releveFactureService = releveFactureService;
    }

    /**
     * {@code POST  /commandes} : Create a new commande.
     *
     * @param commande the commande to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commande, or with status {@code 400 (Bad Request)} if the commande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commandes")
    public ResponseEntity<Commande> createCommande(@Valid @RequestBody Commande commande) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", commande);
        if (commande.getId() != null) {
            throw new BadRequestAlertException("A new commande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commande result = commandeService.save(commande);
        return ResponseEntity
            .created(new URI("/api/commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commandes/:id} : Updates an existing commande.
     *
     * @param id the id of the commande to save.
     * @param commande the commande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commande,
     * or with status {@code 400 (Bad Request)} if the commande is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commandes/{id}")
    public ResponseEntity<Commande> updateCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Commande commande
    ) throws URISyntaxException {
        log.debug("REST request to update Commande : {}, {}", id, commande);
        if (commande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Commande result = commandeService.update(commande);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commande.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commandes/:id} : Partial updates given fields of an existing commande, field will ignore if it is null
     *
     * @param id the id of the commande to save.
     * @param commande the commande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commande,
     * or with status {@code 400 (Bad Request)} if the commande is not valid,
     * or with status {@code 404 (Not Found)} if the commande is not found,
     * or with status {@code 500 (Internal Server Error)} if the commande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commandes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Commande> partialUpdateCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Commande commande
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commande partially : {}, {}", id, commande);
        if (commande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Commande> result = commandeService.partialUpdate(commande);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commande.getId().toString())
        );
    }

    /**
     * {@code GET  /commandes} : get all the commandes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commandes in body.
     */
    @GetMapping("/commandes")
    public List<Commande> getAllCommandes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Commandes");
        return commandeService.findAll();
    }

    /**
     * {@code GET  /commandes/:id} : get the "id" commande.
     *
     * @param id the id of the commande to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commande, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commandes/{id}")
    public ResponseEntity<Commande> getCommande(@PathVariable Long id) {
        log.debug("REST request to get Commande : {}", id);
        Optional<Commande> commande = commandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commande);
    }

    /**
     * {@code DELETE  /commandes/:id} : delete the "id" commande.
     *
     * @param id the id of the commande to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commandes/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        log.debug("REST request to delete Commande : {}", id);
        commandeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/commandes/{id}/ligne-commandes/unvalidated")
    public ResponseEntity<List<LigneCommande>> getUnvalidatedLigneCommandesForCommande(@PathVariable Long id) {
        log.debug("REST request to get unvalidated LigneCommande for Commande : {}", id);

        Optional<Commande> commande = commandeService.findOne(id);

        Set<LigneCommande> ligneCommandes = commande.get().getLigneCommandes();

        List<LigneCommande> nonVerifieesLigneCommandes = ligneCommandes
            .stream()
            .filter(lc -> lc.getValidated() == 0)
            .filter(lc -> lc.getQuantite() > 0)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(nonVerifieesLigneCommandes);
    }

    @PutMapping("/commandes/validate/{id}")
    public ResponseEntity<Commande> updateAll(@PathVariable Long id) {
        log.debug("REST request to update all the things after validation Commande : {}", id);

        Optional<Commande> commande = commandeService.findOne(id);

        Set<LigneCommande> ligneCommandes = commande.get().getLigneCommandes();
        Float montantTotal = 0F;
        for (LigneCommande ligne : ligneCommandes) {
            ligne.setValidated(1);
            ligneCommandeService.update(ligne);

            Produit produit = ligne.getProduit();
            produit.setQuantiteDispo(produit.getQuantiteDispo() - ligne.getQuantite());
            produitService.update(produit);

            montantTotal += ligne.getMontant();
        }

        // if(commande.isPresent()){
        Commande validatedCommande = commande.get();
        ReleveFacture releveFacture = new ReleveFacture();
        releveFacture.addCommande(validatedCommande);
        releveFacture.montant(montantTotal);
        releveFacture.createdAt(Instant.now());
        releveFacture.updateAt(Instant.now());
        releveFacture.user(validatedCommande.getUser());
        releveFactureService.save(releveFacture);
        //  }

        //        List<LigneCommande> nonVerifieesLigneCommandes = ligneCommandes
        //            .stream()
        //            .filter(lc -> lc.getValidated() == 0)
        //            .collect(Collectors.toList());

        Commande result = commandeService.update(validatedCommande);
        return ResponseEntity.ok().body(result);
    }
}

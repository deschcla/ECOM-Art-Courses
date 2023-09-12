import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommandeFormService } from './commande-form.service';
import { CommandeService } from '../service/commande.service';
import { ICommande } from '../commande.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IReleveFacture } from 'app/entities/releve-facture/releve-facture.model';
import { ReleveFactureService } from 'app/entities/releve-facture/service/releve-facture.service';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { AcheteurService } from 'app/entities/acheteur/service/acheteur.service';

import { CommandeUpdateComponent } from './commande-update.component';

describe('Commande Management Update Component', () => {
  let comp: CommandeUpdateComponent;
  let fixture: ComponentFixture<CommandeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandeFormService: CommandeFormService;
  let commandeService: CommandeService;
  let produitService: ProduitService;
  let releveFactureService: ReleveFactureService;
  let acheteurService: AcheteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CommandeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommandeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandeFormService = TestBed.inject(CommandeFormService);
    commandeService = TestBed.inject(CommandeService);
    produitService = TestBed.inject(ProduitService);
    releveFactureService = TestBed.inject(ReleveFactureService);
    acheteurService = TestBed.inject(AcheteurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Produit query and add missing value', () => {
      const commande: ICommande = { id: 456 };
      const produits: IProduit[] = [{ id: 13423 }];
      commande.produits = produits;

      const produitCollection: IProduit[] = [{ id: 16435 }];
      jest.spyOn(produitService, 'query').mockReturnValue(of(new HttpResponse({ body: produitCollection })));
      const additionalProduits = [...produits];
      const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
      jest.spyOn(produitService, 'addProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(produitService.query).toHaveBeenCalled();
      expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(
        produitCollection,
        ...additionalProduits.map(expect.objectContaining)
      );
      expect(comp.produitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ReleveFacture query and add missing value', () => {
      const commande: ICommande = { id: 456 };
      const releveFacture: IReleveFacture = { id: 17084 };
      commande.releveFacture = releveFacture;

      const releveFactureCollection: IReleveFacture[] = [{ id: 22650 }];
      jest.spyOn(releveFactureService, 'query').mockReturnValue(of(new HttpResponse({ body: releveFactureCollection })));
      const additionalReleveFactures = [releveFacture];
      const expectedCollection: IReleveFacture[] = [...additionalReleveFactures, ...releveFactureCollection];
      jest.spyOn(releveFactureService, 'addReleveFactureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(releveFactureService.query).toHaveBeenCalled();
      expect(releveFactureService.addReleveFactureToCollectionIfMissing).toHaveBeenCalledWith(
        releveFactureCollection,
        ...additionalReleveFactures.map(expect.objectContaining)
      );
      expect(comp.releveFacturesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Acheteur query and add missing value', () => {
      const commande: ICommande = { id: 456 };
      const acheteur: IAcheteur = { id: 17257 };
      commande.acheteur = acheteur;

      const acheteurCollection: IAcheteur[] = [{ id: 9592 }];
      jest.spyOn(acheteurService, 'query').mockReturnValue(of(new HttpResponse({ body: acheteurCollection })));
      const additionalAcheteurs = [acheteur];
      const expectedCollection: IAcheteur[] = [...additionalAcheteurs, ...acheteurCollection];
      jest.spyOn(acheteurService, 'addAcheteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(acheteurService.query).toHaveBeenCalled();
      expect(acheteurService.addAcheteurToCollectionIfMissing).toHaveBeenCalledWith(
        acheteurCollection,
        ...additionalAcheteurs.map(expect.objectContaining)
      );
      expect(comp.acheteursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commande: ICommande = { id: 456 };
      const produit: IProduit = { id: 21508 };
      commande.produits = [produit];
      const releveFacture: IReleveFacture = { id: 7805 };
      commande.releveFacture = releveFacture;
      const acheteur: IAcheteur = { id: 19442 };
      commande.acheteur = acheteur;

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(comp.produitsSharedCollection).toContain(produit);
      expect(comp.releveFacturesSharedCollection).toContain(releveFacture);
      expect(comp.acheteursSharedCollection).toContain(acheteur);
      expect(comp.commande).toEqual(commande);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommande>>();
      const commande = { id: 123 };
      jest.spyOn(commandeFormService, 'getCommande').mockReturnValue(commande);
      jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commande }));
      saveSubject.complete();

      // THEN
      expect(commandeFormService.getCommande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandeService.update).toHaveBeenCalledWith(expect.objectContaining(commande));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommande>>();
      const commande = { id: 123 };
      jest.spyOn(commandeFormService, 'getCommande').mockReturnValue({ id: null });
      jest.spyOn(commandeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commande }));
      saveSubject.complete();

      // THEN
      expect(commandeFormService.getCommande).toHaveBeenCalled();
      expect(commandeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommande>>();
      const commande = { id: 123 };
      jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduit', () => {
      it('Should forward to produitService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(produitService, 'compareProduit');
        comp.compareProduit(entity, entity2);
        expect(produitService.compareProduit).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReleveFacture', () => {
      it('Should forward to releveFactureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(releveFactureService, 'compareReleveFacture');
        comp.compareReleveFacture(entity, entity2);
        expect(releveFactureService.compareReleveFacture).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAcheteur', () => {
      it('Should forward to acheteurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(acheteurService, 'compareAcheteur');
        comp.compareAcheteur(entity, entity2);
        expect(acheteurService.compareAcheteur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

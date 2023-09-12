import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LigneCommandeFormService } from './ligne-commande-form.service';
import { LigneCommandeService } from '../service/ligne-commande.service';
import { ILigneCommande } from '../ligne-commande.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

import { LigneCommandeUpdateComponent } from './ligne-commande-update.component';

describe('LigneCommande Management Update Component', () => {
  let comp: LigneCommandeUpdateComponent;
  let fixture: ComponentFixture<LigneCommandeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ligneCommandeFormService: LigneCommandeFormService;
  let ligneCommandeService: LigneCommandeService;
  let produitService: ProduitService;
  let commandeService: CommandeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LigneCommandeUpdateComponent],
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
      .overrideTemplate(LigneCommandeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigneCommandeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ligneCommandeFormService = TestBed.inject(LigneCommandeFormService);
    ligneCommandeService = TestBed.inject(LigneCommandeService);
    produitService = TestBed.inject(ProduitService);
    commandeService = TestBed.inject(CommandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Produit query and add missing value', () => {
      const ligneCommande: ILigneCommande = { id: 456 };
      const produit: IProduit = { id: 675 };
      ligneCommande.produit = produit;

      const produitCollection: IProduit[] = [{ id: 17246 }];
      jest.spyOn(produitService, 'query').mockReturnValue(of(new HttpResponse({ body: produitCollection })));
      const additionalProduits = [produit];
      const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
      jest.spyOn(produitService, 'addProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligneCommande });
      comp.ngOnInit();

      expect(produitService.query).toHaveBeenCalled();
      expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(
        produitCollection,
        ...additionalProduits.map(expect.objectContaining)
      );
      expect(comp.produitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Commande query and add missing value', () => {
      const ligneCommande: ILigneCommande = { id: 456 };
      const commande: ICommande = { id: 25083 };
      ligneCommande.commande = commande;

      const commandeCollection: ICommande[] = [{ id: 12025 }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [commande];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligneCommande });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(
        commandeCollection,
        ...additionalCommandes.map(expect.objectContaining)
      );
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ligneCommande: ILigneCommande = { id: 456 };
      const produit: IProduit = { id: 11580 };
      ligneCommande.produit = produit;
      const commande: ICommande = { id: 16024 };
      ligneCommande.commande = commande;

      activatedRoute.data = of({ ligneCommande });
      comp.ngOnInit();

      expect(comp.produitsSharedCollection).toContain(produit);
      expect(comp.commandesSharedCollection).toContain(commande);
      expect(comp.ligneCommande).toEqual(ligneCommande);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigneCommande>>();
      const ligneCommande = { id: 123 };
      jest.spyOn(ligneCommandeFormService, 'getLigneCommande').mockReturnValue(ligneCommande);
      jest.spyOn(ligneCommandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligneCommande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligneCommande }));
      saveSubject.complete();

      // THEN
      expect(ligneCommandeFormService.getLigneCommande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ligneCommandeService.update).toHaveBeenCalledWith(expect.objectContaining(ligneCommande));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigneCommande>>();
      const ligneCommande = { id: 123 };
      jest.spyOn(ligneCommandeFormService, 'getLigneCommande').mockReturnValue({ id: null });
      jest.spyOn(ligneCommandeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligneCommande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligneCommande }));
      saveSubject.complete();

      // THEN
      expect(ligneCommandeFormService.getLigneCommande).toHaveBeenCalled();
      expect(ligneCommandeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigneCommande>>();
      const ligneCommande = { id: 123 };
      jest.spyOn(ligneCommandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligneCommande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ligneCommandeService.update).toHaveBeenCalled();
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

    describe('compareCommande', () => {
      it('Should forward to commandeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(commandeService, 'compareCommande');
        comp.compareCommande(entity, entity2);
        expect(commandeService.compareCommande).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

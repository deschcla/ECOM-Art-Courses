import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CarteBancaireFormService } from './carte-bancaire-form.service';
import { CarteBancaireService } from '../service/carte-bancaire.service';
import { ICarteBancaire } from '../carte-bancaire.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

import { CarteBancaireUpdateComponent } from './carte-bancaire-update.component';

describe('CarteBancaire Management Update Component', () => {
  let comp: CarteBancaireUpdateComponent;
  let fixture: ComponentFixture<CarteBancaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carteBancaireFormService: CarteBancaireFormService;
  let carteBancaireService: CarteBancaireService;
  let commandeService: CommandeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CarteBancaireUpdateComponent],
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
      .overrideTemplate(CarteBancaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarteBancaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carteBancaireFormService = TestBed.inject(CarteBancaireFormService);
    carteBancaireService = TestBed.inject(CarteBancaireService);
    commandeService = TestBed.inject(CommandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Commande query and add missing value', () => {
      const carteBancaire: ICarteBancaire = { id: 456 };
      const commande: ICommande = { id: 26398 };
      carteBancaire.commande = commande;

      const commandeCollection: ICommande[] = [{ id: 24968 }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [commande];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(
        commandeCollection,
        ...additionalCommandes.map(expect.objectContaining)
      );
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const carteBancaire: ICarteBancaire = { id: 456 };
      const commande: ICommande = { id: 14894 };
      carteBancaire.commande = commande;

      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      expect(comp.commandesSharedCollection).toContain(commande);
      expect(comp.carteBancaire).toEqual(carteBancaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICarteBancaire>>();
      const carteBancaire = { id: 123 };
      jest.spyOn(carteBancaireFormService, 'getCarteBancaire').mockReturnValue(carteBancaire);
      jest.spyOn(carteBancaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carteBancaire }));
      saveSubject.complete();

      // THEN
      expect(carteBancaireFormService.getCarteBancaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(carteBancaireService.update).toHaveBeenCalledWith(expect.objectContaining(carteBancaire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICarteBancaire>>();
      const carteBancaire = { id: 123 };
      jest.spyOn(carteBancaireFormService, 'getCarteBancaire').mockReturnValue({ id: null });
      jest.spyOn(carteBancaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carteBancaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carteBancaire }));
      saveSubject.complete();

      // THEN
      expect(carteBancaireFormService.getCarteBancaire).toHaveBeenCalled();
      expect(carteBancaireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICarteBancaire>>();
      const carteBancaire = { id: 123 };
      jest.spyOn(carteBancaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carteBancaireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

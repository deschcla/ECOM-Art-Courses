import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReleveFactureFormService } from './releve-facture-form.service';
import { ReleveFactureService } from '../service/releve-facture.service';
import { IReleveFacture } from '../releve-facture.model';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { AcheteurService } from 'app/entities/acheteur/service/acheteur.service';

import { ReleveFactureUpdateComponent } from './releve-facture-update.component';

describe('ReleveFacture Management Update Component', () => {
  let comp: ReleveFactureUpdateComponent;
  let fixture: ComponentFixture<ReleveFactureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let releveFactureFormService: ReleveFactureFormService;
  let releveFactureService: ReleveFactureService;
  let acheteurService: AcheteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ReleveFactureUpdateComponent],
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
      .overrideTemplate(ReleveFactureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReleveFactureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    releveFactureFormService = TestBed.inject(ReleveFactureFormService);
    releveFactureService = TestBed.inject(ReleveFactureService);
    acheteurService = TestBed.inject(AcheteurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Acheteur query and add missing value', () => {
      const releveFacture: IReleveFacture = { id: 456 };
      const acheteur: IAcheteur = { id: 29017 };
      releveFacture.acheteur = acheteur;

      const acheteurCollection: IAcheteur[] = [{ id: 9284 }];
      jest.spyOn(acheteurService, 'query').mockReturnValue(of(new HttpResponse({ body: acheteurCollection })));
      const additionalAcheteurs = [acheteur];
      const expectedCollection: IAcheteur[] = [...additionalAcheteurs, ...acheteurCollection];
      jest.spyOn(acheteurService, 'addAcheteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ releveFacture });
      comp.ngOnInit();

      expect(acheteurService.query).toHaveBeenCalled();
      expect(acheteurService.addAcheteurToCollectionIfMissing).toHaveBeenCalledWith(
        acheteurCollection,
        ...additionalAcheteurs.map(expect.objectContaining)
      );
      expect(comp.acheteursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const releveFacture: IReleveFacture = { id: 456 };
      const acheteur: IAcheteur = { id: 14249 };
      releveFacture.acheteur = acheteur;

      activatedRoute.data = of({ releveFacture });
      comp.ngOnInit();

      expect(comp.acheteursSharedCollection).toContain(acheteur);
      expect(comp.releveFacture).toEqual(releveFacture);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReleveFacture>>();
      const releveFacture = { id: 123 };
      jest.spyOn(releveFactureFormService, 'getReleveFacture').mockReturnValue(releveFacture);
      jest.spyOn(releveFactureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ releveFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: releveFacture }));
      saveSubject.complete();

      // THEN
      expect(releveFactureFormService.getReleveFacture).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(releveFactureService.update).toHaveBeenCalledWith(expect.objectContaining(releveFacture));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReleveFacture>>();
      const releveFacture = { id: 123 };
      jest.spyOn(releveFactureFormService, 'getReleveFacture').mockReturnValue({ id: null });
      jest.spyOn(releveFactureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ releveFacture: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: releveFacture }));
      saveSubject.complete();

      // THEN
      expect(releveFactureFormService.getReleveFacture).toHaveBeenCalled();
      expect(releveFactureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReleveFacture>>();
      const releveFacture = { id: 123 };
      jest.spyOn(releveFactureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ releveFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(releveFactureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

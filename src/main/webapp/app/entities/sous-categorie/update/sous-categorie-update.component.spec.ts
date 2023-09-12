import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SousCategorieFormService } from './sous-categorie-form.service';
import { SousCategorieService } from '../service/sous-categorie.service';
import { ISousCategorie } from '../sous-categorie.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

import { SousCategorieUpdateComponent } from './sous-categorie-update.component';

describe('SousCategorie Management Update Component', () => {
  let comp: SousCategorieUpdateComponent;
  let fixture: ComponentFixture<SousCategorieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousCategorieFormService: SousCategorieFormService;
  let sousCategorieService: SousCategorieService;
  let categorieService: CategorieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SousCategorieUpdateComponent],
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
      .overrideTemplate(SousCategorieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousCategorieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousCategorieFormService = TestBed.inject(SousCategorieFormService);
    sousCategorieService = TestBed.inject(SousCategorieService);
    categorieService = TestBed.inject(CategorieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categorie query and add missing value', () => {
      const sousCategorie: ISousCategorie = { id: 456 };
      const categorie: ICategorie = { id: 9291 };
      sousCategorie.categorie = categorie;

      const categorieCollection: ICategorie[] = [{ id: 28195 }];
      jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
      const additionalCategories = [categorie];
      const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
      jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousCategorie });
      comp.ngOnInit();

      expect(categorieService.query).toHaveBeenCalled();
      expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(
        categorieCollection,
        ...additionalCategories.map(expect.objectContaining)
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousCategorie: ISousCategorie = { id: 456 };
      const categorie: ICategorie = { id: 5233 };
      sousCategorie.categorie = categorie;

      activatedRoute.data = of({ sousCategorie });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(categorie);
      expect(comp.sousCategorie).toEqual(sousCategorie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISousCategorie>>();
      const sousCategorie = { id: 123 };
      jest.spyOn(sousCategorieFormService, 'getSousCategorie').mockReturnValue(sousCategorie);
      jest.spyOn(sousCategorieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousCategorie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousCategorie }));
      saveSubject.complete();

      // THEN
      expect(sousCategorieFormService.getSousCategorie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousCategorieService.update).toHaveBeenCalledWith(expect.objectContaining(sousCategorie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISousCategorie>>();
      const sousCategorie = { id: 123 };
      jest.spyOn(sousCategorieFormService, 'getSousCategorie').mockReturnValue({ id: null });
      jest.spyOn(sousCategorieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousCategorie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousCategorie }));
      saveSubject.complete();

      // THEN
      expect(sousCategorieFormService.getSousCategorie).toHaveBeenCalled();
      expect(sousCategorieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISousCategorie>>();
      const sousCategorie = { id: 123 };
      jest.spyOn(sousCategorieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousCategorie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousCategorieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategorie', () => {
      it('Should forward to categorieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categorieService, 'compareCategorie');
        comp.compareCategorie(entity, entity2);
        expect(categorieService.compareCategorie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

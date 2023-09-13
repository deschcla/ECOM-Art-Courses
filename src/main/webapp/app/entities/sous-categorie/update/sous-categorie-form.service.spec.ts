import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sous-categorie.test-samples';

import { SousCategorieFormService } from './sous-categorie-form.service';

describe('SousCategorie Form Service', () => {
  let service: SousCategorieFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SousCategorieFormService);
  });

  describe('Service methods', () => {
    describe('createSousCategorieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSousCategorieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeSousCategorie: expect.any(Object),
            createdAt: expect.any(Object),
            updateAt: expect.any(Object),
            categorie: expect.any(Object),
          })
        );
      });

      it('passing ISousCategorie should create a new form with FormGroup', () => {
        const formGroup = service.createSousCategorieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeSousCategorie: expect.any(Object),
            createdAt: expect.any(Object),
            updateAt: expect.any(Object),
            categorie: expect.any(Object),
          })
        );
      });
    });

    describe('getSousCategorie', () => {
      it('should return NewSousCategorie for default SousCategorie initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSousCategorieFormGroup(sampleWithNewData);

        const sousCategorie = service.getSousCategorie(formGroup) as any;

        expect(sousCategorie).toMatchObject(sampleWithNewData);
      });

      it('should return NewSousCategorie for empty SousCategorie initial value', () => {
        const formGroup = service.createSousCategorieFormGroup();

        const sousCategorie = service.getSousCategorie(formGroup) as any;

        expect(sousCategorie).toMatchObject({});
      });

      it('should return ISousCategorie', () => {
        const formGroup = service.createSousCategorieFormGroup(sampleWithRequiredData);

        const sousCategorie = service.getSousCategorie(formGroup) as any;

        expect(sousCategorie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISousCategorie should not enable id FormControl', () => {
        const formGroup = service.createSousCategorieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSousCategorie should disable id FormControl', () => {
        const formGroup = service.createSousCategorieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

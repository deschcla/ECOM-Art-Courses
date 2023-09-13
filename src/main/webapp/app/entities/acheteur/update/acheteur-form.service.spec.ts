import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../acheteur.test-samples';

import { AcheteurFormService } from './acheteur-form.service';

describe('Acheteur Form Service', () => {
  let service: AcheteurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AcheteurFormService);
  });

  describe('Service methods', () => {
    describe('createAcheteurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAcheteurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            adresse: expect.any(Object),
            dateNaiss: expect.any(Object),
            numTel: expect.any(Object),
            createdAt: expect.any(Object),
            updateAt: expect.any(Object),
            internalUser: expect.any(Object),
          })
        );
      });

      it('passing IAcheteur should create a new form with FormGroup', () => {
        const formGroup = service.createAcheteurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            adresse: expect.any(Object),
            dateNaiss: expect.any(Object),
            numTel: expect.any(Object),
            createdAt: expect.any(Object),
            updateAt: expect.any(Object),
            internalUser: expect.any(Object),
          })
        );
      });
    });

    describe('getAcheteur', () => {
      it('should return NewAcheteur for default Acheteur initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAcheteurFormGroup(sampleWithNewData);

        const acheteur = service.getAcheteur(formGroup) as any;

        expect(acheteur).toMatchObject(sampleWithNewData);
      });

      it('should return NewAcheteur for empty Acheteur initial value', () => {
        const formGroup = service.createAcheteurFormGroup();

        const acheteur = service.getAcheteur(formGroup) as any;

        expect(acheteur).toMatchObject({});
      });

      it('should return IAcheteur', () => {
        const formGroup = service.createAcheteurFormGroup(sampleWithRequiredData);

        const acheteur = service.getAcheteur(formGroup) as any;

        expect(acheteur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAcheteur should not enable id FormControl', () => {
        const formGroup = service.createAcheteurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAcheteur should disable id FormControl', () => {
        const formGroup = service.createAcheteurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../releve-facture.test-samples';

import { ReleveFactureFormService } from './releve-facture-form.service';

describe('ReleveFacture Form Service', () => {
  let service: ReleveFactureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReleveFactureFormService);
  });

  describe('Service methods', () => {
    describe('createReleveFactureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReleveFactureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montant: expect.any(Object),
            createdAt: expect.any(Object),
            updateAt: expect.any(Object),
            acheteur: expect.any(Object),
          })
        );
      });

      it('passing IReleveFacture should create a new form with FormGroup', () => {
        const formGroup = service.createReleveFactureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montant: expect.any(Object),
            createdAt: expect.any(Object),
            updateAt: expect.any(Object),
            acheteur: expect.any(Object),
          })
        );
      });
    });

    describe('getReleveFacture', () => {
      it('should return NewReleveFacture for default ReleveFacture initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createReleveFactureFormGroup(sampleWithNewData);

        const releveFacture = service.getReleveFacture(formGroup) as any;

        expect(releveFacture).toMatchObject(sampleWithNewData);
      });

      it('should return NewReleveFacture for empty ReleveFacture initial value', () => {
        const formGroup = service.createReleveFactureFormGroup();

        const releveFacture = service.getReleveFacture(formGroup) as any;

        expect(releveFacture).toMatchObject({});
      });

      it('should return IReleveFacture', () => {
        const formGroup = service.createReleveFactureFormGroup(sampleWithRequiredData);

        const releveFacture = service.getReleveFacture(formGroup) as any;

        expect(releveFacture).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReleveFacture should not enable id FormControl', () => {
        const formGroup = service.createReleveFactureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReleveFacture should disable id FormControl', () => {
        const formGroup = service.createReleveFactureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

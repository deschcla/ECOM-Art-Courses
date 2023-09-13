import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAcheteur } from '../acheteur.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../acheteur.test-samples';

import { AcheteurService, RestAcheteur } from './acheteur.service';

const requireRestSample: RestAcheteur = {
  ...sampleWithRequiredData,
  dateNaiss: sampleWithRequiredData.dateNaiss?.format(DATE_FORMAT),
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
};

describe('Acheteur Service', () => {
  let service: AcheteurService;
  let httpMock: HttpTestingController;
  let expectedResult: IAcheteur | IAcheteur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AcheteurService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Acheteur', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const acheteur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(acheteur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Acheteur', () => {
      const acheteur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(acheteur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Acheteur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Acheteur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Acheteur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAcheteurToCollectionIfMissing', () => {
      it('should add a Acheteur to an empty array', () => {
        const acheteur: IAcheteur = sampleWithRequiredData;
        expectedResult = service.addAcheteurToCollectionIfMissing([], acheteur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acheteur);
      });

      it('should not add a Acheteur to an array that contains it', () => {
        const acheteur: IAcheteur = sampleWithRequiredData;
        const acheteurCollection: IAcheteur[] = [
          {
            ...acheteur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, acheteur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Acheteur to an array that doesn't contain it", () => {
        const acheteur: IAcheteur = sampleWithRequiredData;
        const acheteurCollection: IAcheteur[] = [sampleWithPartialData];
        expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, acheteur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acheteur);
      });

      it('should add only unique Acheteur to an array', () => {
        const acheteurArray: IAcheteur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const acheteurCollection: IAcheteur[] = [sampleWithRequiredData];
        expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, ...acheteurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const acheteur: IAcheteur = sampleWithRequiredData;
        const acheteur2: IAcheteur = sampleWithPartialData;
        expectedResult = service.addAcheteurToCollectionIfMissing([], acheteur, acheteur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acheteur);
        expect(expectedResult).toContain(acheteur2);
      });

      it('should accept null and undefined values', () => {
        const acheteur: IAcheteur = sampleWithRequiredData;
        expectedResult = service.addAcheteurToCollectionIfMissing([], null, acheteur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acheteur);
      });

      it('should return initial array if no Acheteur is added', () => {
        const acheteurCollection: IAcheteur[] = [sampleWithRequiredData];
        expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, undefined, null);
        expect(expectedResult).toEqual(acheteurCollection);
      });
    });

    describe('compareAcheteur', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAcheteur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAcheteur(entity1, entity2);
        const compareResult2 = service.compareAcheteur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAcheteur(entity1, entity2);
        const compareResult2 = service.compareAcheteur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAcheteur(entity1, entity2);
        const compareResult2 = service.compareAcheteur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

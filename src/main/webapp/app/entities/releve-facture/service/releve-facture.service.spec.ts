import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReleveFacture } from '../releve-facture.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../releve-facture.test-samples';

import { ReleveFactureService, RestReleveFacture } from './releve-facture.service';

const requireRestSample: RestReleveFacture = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
};

describe('ReleveFacture Service', () => {
  let service: ReleveFactureService;
  let httpMock: HttpTestingController;
  let expectedResult: IReleveFacture | IReleveFacture[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReleveFactureService);
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

    it('should create a ReleveFacture', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const releveFacture = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(releveFacture).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReleveFacture', () => {
      const releveFacture = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(releveFacture).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReleveFacture', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReleveFacture', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReleveFacture', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReleveFactureToCollectionIfMissing', () => {
      it('should add a ReleveFacture to an empty array', () => {
        const releveFacture: IReleveFacture = sampleWithRequiredData;
        expectedResult = service.addReleveFactureToCollectionIfMissing([], releveFacture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(releveFacture);
      });

      it('should not add a ReleveFacture to an array that contains it', () => {
        const releveFacture: IReleveFacture = sampleWithRequiredData;
        const releveFactureCollection: IReleveFacture[] = [
          {
            ...releveFacture,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReleveFactureToCollectionIfMissing(releveFactureCollection, releveFacture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReleveFacture to an array that doesn't contain it", () => {
        const releveFacture: IReleveFacture = sampleWithRequiredData;
        const releveFactureCollection: IReleveFacture[] = [sampleWithPartialData];
        expectedResult = service.addReleveFactureToCollectionIfMissing(releveFactureCollection, releveFacture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(releveFacture);
      });

      it('should add only unique ReleveFacture to an array', () => {
        const releveFactureArray: IReleveFacture[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const releveFactureCollection: IReleveFacture[] = [sampleWithRequiredData];
        expectedResult = service.addReleveFactureToCollectionIfMissing(releveFactureCollection, ...releveFactureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const releveFacture: IReleveFacture = sampleWithRequiredData;
        const releveFacture2: IReleveFacture = sampleWithPartialData;
        expectedResult = service.addReleveFactureToCollectionIfMissing([], releveFacture, releveFacture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(releveFacture);
        expect(expectedResult).toContain(releveFacture2);
      });

      it('should accept null and undefined values', () => {
        const releveFacture: IReleveFacture = sampleWithRequiredData;
        expectedResult = service.addReleveFactureToCollectionIfMissing([], null, releveFacture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(releveFacture);
      });

      it('should return initial array if no ReleveFacture is added', () => {
        const releveFactureCollection: IReleveFacture[] = [sampleWithRequiredData];
        expectedResult = service.addReleveFactureToCollectionIfMissing(releveFactureCollection, undefined, null);
        expect(expectedResult).toEqual(releveFactureCollection);
      });
    });

    describe('compareReleveFacture', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReleveFacture(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReleveFacture(entity1, entity2);
        const compareResult2 = service.compareReleveFacture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReleveFacture(entity1, entity2);
        const compareResult2 = service.compareReleveFacture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReleveFacture(entity1, entity2);
        const compareResult2 = service.compareReleveFacture(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousCategorie } from '../sous-categorie.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sous-categorie.test-samples';

import { SousCategorieService, RestSousCategorie } from './sous-categorie.service';

const requireRestSample: RestSousCategorie = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
};

describe('SousCategorie Service', () => {
  let service: SousCategorieService;
  let httpMock: HttpTestingController;
  let expectedResult: ISousCategorie | ISousCategorie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousCategorieService);
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

    it('should create a SousCategorie', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sousCategorie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sousCategorie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousCategorie', () => {
      const sousCategorie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sousCategorie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SousCategorie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousCategorie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SousCategorie', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSousCategorieToCollectionIfMissing', () => {
      it('should add a SousCategorie to an empty array', () => {
        const sousCategorie: ISousCategorie = sampleWithRequiredData;
        expectedResult = service.addSousCategorieToCollectionIfMissing([], sousCategorie);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousCategorie);
      });

      it('should not add a SousCategorie to an array that contains it', () => {
        const sousCategorie: ISousCategorie = sampleWithRequiredData;
        const sousCategorieCollection: ISousCategorie[] = [
          {
            ...sousCategorie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSousCategorieToCollectionIfMissing(sousCategorieCollection, sousCategorie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousCategorie to an array that doesn't contain it", () => {
        const sousCategorie: ISousCategorie = sampleWithRequiredData;
        const sousCategorieCollection: ISousCategorie[] = [sampleWithPartialData];
        expectedResult = service.addSousCategorieToCollectionIfMissing(sousCategorieCollection, sousCategorie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousCategorie);
      });

      it('should add only unique SousCategorie to an array', () => {
        const sousCategorieArray: ISousCategorie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sousCategorieCollection: ISousCategorie[] = [sampleWithRequiredData];
        expectedResult = service.addSousCategorieToCollectionIfMissing(sousCategorieCollection, ...sousCategorieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousCategorie: ISousCategorie = sampleWithRequiredData;
        const sousCategorie2: ISousCategorie = sampleWithPartialData;
        expectedResult = service.addSousCategorieToCollectionIfMissing([], sousCategorie, sousCategorie2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousCategorie);
        expect(expectedResult).toContain(sousCategorie2);
      });

      it('should accept null and undefined values', () => {
        const sousCategorie: ISousCategorie = sampleWithRequiredData;
        expectedResult = service.addSousCategorieToCollectionIfMissing([], null, sousCategorie, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousCategorie);
      });

      it('should return initial array if no SousCategorie is added', () => {
        const sousCategorieCollection: ISousCategorie[] = [sampleWithRequiredData];
        expectedResult = service.addSousCategorieToCollectionIfMissing(sousCategorieCollection, undefined, null);
        expect(expectedResult).toEqual(sousCategorieCollection);
      });
    });

    describe('compareSousCategorie', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSousCategorie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSousCategorie(entity1, entity2);
        const compareResult2 = service.compareSousCategorie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSousCategorie(entity1, entity2);
        const compareResult2 = service.compareSousCategorie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSousCategorie(entity1, entity2);
        const compareResult2 = service.compareSousCategorie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

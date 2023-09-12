import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousCategorie, NewSousCategorie } from '../sous-categorie.model';

export type PartialUpdateSousCategorie = Partial<ISousCategorie> & Pick<ISousCategorie, 'id'>;

type RestOf<T extends ISousCategorie | NewSousCategorie> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

export type RestSousCategorie = RestOf<ISousCategorie>;

export type NewRestSousCategorie = RestOf<NewSousCategorie>;

export type PartialUpdateRestSousCategorie = RestOf<PartialUpdateSousCategorie>;

export type EntityResponseType = HttpResponse<ISousCategorie>;
export type EntityArrayResponseType = HttpResponse<ISousCategorie[]>;

@Injectable({ providedIn: 'root' })
export class SousCategorieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousCategorie: NewSousCategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sousCategorie);
    return this.http
      .post<RestSousCategorie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sousCategorie: ISousCategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sousCategorie);
    return this.http
      .put<RestSousCategorie>(`${this.resourceUrl}/${this.getSousCategorieIdentifier(sousCategorie)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sousCategorie: PartialUpdateSousCategorie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sousCategorie);
    return this.http
      .patch<RestSousCategorie>(`${this.resourceUrl}/${this.getSousCategorieIdentifier(sousCategorie)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSousCategorie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSousCategorie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSousCategorieIdentifier(sousCategorie: Pick<ISousCategorie, 'id'>): number {
    return sousCategorie.id;
  }

  compareSousCategorie(o1: Pick<ISousCategorie, 'id'> | null, o2: Pick<ISousCategorie, 'id'> | null): boolean {
    return o1 && o2 ? this.getSousCategorieIdentifier(o1) === this.getSousCategorieIdentifier(o2) : o1 === o2;
  }

  addSousCategorieToCollectionIfMissing<Type extends Pick<ISousCategorie, 'id'>>(
    sousCategorieCollection: Type[],
    ...sousCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sousCategories: Type[] = sousCategoriesToCheck.filter(isPresent);
    if (sousCategories.length > 0) {
      const sousCategorieCollectionIdentifiers = sousCategorieCollection.map(
        sousCategorieItem => this.getSousCategorieIdentifier(sousCategorieItem)!
      );
      const sousCategoriesToAdd = sousCategories.filter(sousCategorieItem => {
        const sousCategorieIdentifier = this.getSousCategorieIdentifier(sousCategorieItem);
        if (sousCategorieCollectionIdentifiers.includes(sousCategorieIdentifier)) {
          return false;
        }
        sousCategorieCollectionIdentifiers.push(sousCategorieIdentifier);
        return true;
      });
      return [...sousCategoriesToAdd, ...sousCategorieCollection];
    }
    return sousCategorieCollection;
  }

  protected convertDateFromClient<T extends ISousCategorie | NewSousCategorie | PartialUpdateSousCategorie>(sousCategorie: T): RestOf<T> {
    return {
      ...sousCategorie,
      createdAt: sousCategorie.createdAt?.toJSON() ?? null,
      updateAt: sousCategorie.updateAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSousCategorie: RestSousCategorie): ISousCategorie {
    return {
      ...restSousCategorie,
      createdAt: restSousCategorie.createdAt ? dayjs(restSousCategorie.createdAt) : undefined,
      updateAt: restSousCategorie.updateAt ? dayjs(restSousCategorie.updateAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSousCategorie>): HttpResponse<ISousCategorie> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSousCategorie[]>): HttpResponse<ISousCategorie[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

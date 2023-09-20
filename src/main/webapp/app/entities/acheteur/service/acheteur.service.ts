import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAcheteur, NewAcheteur } from '../acheteur.model';

export type PartialUpdateAcheteur = Partial<IAcheteur> & Pick<IAcheteur, 'id'>;

type RestOf<T extends IAcheteur | NewAcheteur> = Omit<T, 'dateNaiss' | 'createdAt' | 'updateAt'> & {
  dateNaiss?: string | null;
  createdAt?: string | null;
  updateAt?: string | null;
};

export type RestAcheteur = RestOf<IAcheteur>;

export type NewRestAcheteur = RestOf<NewAcheteur>;

export type PartialUpdateRestAcheteur = RestOf<PartialUpdateAcheteur>;

export type EntityResponseType = HttpResponse<IAcheteur>;
export type EntityArrayResponseType = HttpResponse<IAcheteur[]>;

@Injectable({ providedIn: 'root' })
export class AcheteurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acheteurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(acheteur: NewAcheteur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(acheteur);
    return this.http
      .post<RestAcheteur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(acheteur: IAcheteur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(acheteur);
    return this.http
      .put<RestAcheteur>(`${this.resourceUrl}/${this.getAcheteurIdentifier(acheteur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(acheteur: PartialUpdateAcheteur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(acheteur);
    return this.http
      .patch<RestAcheteur>(`${this.resourceUrl}/${this.getAcheteurIdentifier(acheteur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAcheteur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAcheteur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAcheteurIdentifier(acheteur: Pick<IAcheteur, 'id'>): number {
    return acheteur.id;
  }

  compareAcheteur(o1: Pick<IAcheteur, 'id'> | null, o2: Pick<IAcheteur, 'id'> | null): boolean {
    return o1 && o2 ? this.getAcheteurIdentifier(o1) === this.getAcheteurIdentifier(o2) : o1 === o2;
  }

  addAcheteurToCollectionIfMissing<Type extends Pick<IAcheteur, 'id'>>(
    acheteurCollection: Type[],
    ...acheteursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const acheteurs: Type[] = acheteursToCheck.filter(isPresent);
    if (acheteurs.length > 0) {
      const acheteurCollectionIdentifiers = acheteurCollection.map(acheteurItem => this.getAcheteurIdentifier(acheteurItem)!);
      const acheteursToAdd = acheteurs.filter(acheteurItem => {
        const acheteurIdentifier = this.getAcheteurIdentifier(acheteurItem);
        if (acheteurCollectionIdentifiers.includes(acheteurIdentifier)) {
          return false;
        }
        acheteurCollectionIdentifiers.push(acheteurIdentifier);
        return true;
      });
      return [...acheteursToAdd, ...acheteurCollection];
    }
    return acheteurCollection;
  }

  protected convertDateFromClient<T extends IAcheteur | NewAcheteur | PartialUpdateAcheteur>(acheteur: T): RestOf<T> {
    return {
      ...acheteur,
      dateNaiss: acheteur.dateNaiss?.format(DATE_FORMAT) ?? null,
      createdAt: acheteur.createdAt?.toJSON() ?? null,
      updateAt: acheteur.updateAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAcheteur: RestAcheteur): IAcheteur {
    return {
      ...restAcheteur,
      dateNaiss: restAcheteur.dateNaiss ? dayjs(restAcheteur.dateNaiss) : undefined,
      createdAt: restAcheteur.createdAt ? dayjs(restAcheteur.createdAt) : undefined,
      updateAt: restAcheteur.updateAt ? dayjs(restAcheteur.updateAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAcheteur>): HttpResponse<IAcheteur> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAcheteur[]>): HttpResponse<IAcheteur[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

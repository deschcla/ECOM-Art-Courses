import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReleveFacture, NewReleveFacture } from '../releve-facture.model';

export type PartialUpdateReleveFacture = Partial<IReleveFacture> & Pick<IReleveFacture, 'id'>;

type RestOf<T extends IReleveFacture | NewReleveFacture> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

export type RestReleveFacture = RestOf<IReleveFacture>;

export type NewRestReleveFacture = RestOf<NewReleveFacture>;

export type PartialUpdateRestReleveFacture = RestOf<PartialUpdateReleveFacture>;

export type EntityResponseType = HttpResponse<IReleveFacture>;
export type EntityArrayResponseType = HttpResponse<IReleveFacture[]>;

@Injectable({ providedIn: 'root' })
export class ReleveFactureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/releve-factures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(releveFacture: NewReleveFacture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releveFacture);
    return this.http
      .post<RestReleveFacture>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(releveFacture: IReleveFacture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releveFacture);
    return this.http
      .put<RestReleveFacture>(`${this.resourceUrl}/${this.getReleveFactureIdentifier(releveFacture)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(releveFacture: PartialUpdateReleveFacture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releveFacture);
    return this.http
      .patch<RestReleveFacture>(`${this.resourceUrl}/${this.getReleveFactureIdentifier(releveFacture)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReleveFacture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReleveFacture[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReleveFactureIdentifier(releveFacture: Pick<IReleveFacture, 'id'>): number {
    return releveFacture.id;
  }

  compareReleveFacture(o1: Pick<IReleveFacture, 'id'> | null, o2: Pick<IReleveFacture, 'id'> | null): boolean {
    return o1 && o2 ? this.getReleveFactureIdentifier(o1) === this.getReleveFactureIdentifier(o2) : o1 === o2;
  }

  addReleveFactureToCollectionIfMissing<Type extends Pick<IReleveFacture, 'id'>>(
    releveFactureCollection: Type[],
    ...releveFacturesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const releveFactures: Type[] = releveFacturesToCheck.filter(isPresent);
    if (releveFactures.length > 0) {
      const releveFactureCollectionIdentifiers = releveFactureCollection.map(
        releveFactureItem => this.getReleveFactureIdentifier(releveFactureItem)!
      );
      const releveFacturesToAdd = releveFactures.filter(releveFactureItem => {
        const releveFactureIdentifier = this.getReleveFactureIdentifier(releveFactureItem);
        if (releveFactureCollectionIdentifiers.includes(releveFactureIdentifier)) {
          return false;
        }
        releveFactureCollectionIdentifiers.push(releveFactureIdentifier);
        return true;
      });
      return [...releveFacturesToAdd, ...releveFactureCollection];
    }
    return releveFactureCollection;
  }

  protected convertDateFromClient<T extends IReleveFacture | NewReleveFacture | PartialUpdateReleveFacture>(releveFacture: T): RestOf<T> {
    return {
      ...releveFacture,
      createdAt: releveFacture.createdAt?.toJSON() ?? null,
      updateAt: releveFacture.updateAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReleveFacture: RestReleveFacture): IReleveFacture {
    return {
      ...restReleveFacture,
      createdAt: restReleveFacture.createdAt ? dayjs(restReleveFacture.createdAt) : undefined,
      updateAt: restReleveFacture.updateAt ? dayjs(restReleveFacture.updateAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReleveFacture>): HttpResponse<IReleveFacture> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReleveFacture[]>): HttpResponse<IReleveFacture[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

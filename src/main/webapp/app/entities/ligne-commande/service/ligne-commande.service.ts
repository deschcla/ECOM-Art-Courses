import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigneCommande, NewLigneCommande } from '../ligne-commande.model';

export type PartialUpdateLigneCommande = Partial<ILigneCommande> & Pick<ILigneCommande, 'id'>;

type RestOf<T extends ILigneCommande | NewLigneCommande> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

export type RestLigneCommande = RestOf<ILigneCommande>;

export type NewRestLigneCommande = RestOf<NewLigneCommande>;

export type PartialUpdateRestLigneCommande = RestOf<PartialUpdateLigneCommande>;

export type EntityResponseType = HttpResponse<ILigneCommande>;
export type EntityArrayResponseType = HttpResponse<ILigneCommande[]>;

@Injectable({ providedIn: 'root' })
export class LigneCommandeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligne-commandes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ligneCommande: NewLigneCommande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ligneCommande);
    return this.http
      .post<RestLigneCommande>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ligneCommande: ILigneCommande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ligneCommande);
    return this.http
      .put<RestLigneCommande>(`${this.resourceUrl}/${this.getLigneCommandeIdentifier(ligneCommande)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ligneCommande: PartialUpdateLigneCommande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ligneCommande);
    return this.http
      .patch<RestLigneCommande>(`${this.resourceUrl}/${this.getLigneCommandeIdentifier(ligneCommande)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLigneCommande>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLigneCommande[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLigneCommandeIdentifier(ligneCommande: Pick<ILigneCommande, 'id'>): number {
    return ligneCommande.id;
  }

  compareLigneCommande(o1: Pick<ILigneCommande, 'id'> | null, o2: Pick<ILigneCommande, 'id'> | null): boolean {
    return o1 && o2 ? this.getLigneCommandeIdentifier(o1) === this.getLigneCommandeIdentifier(o2) : o1 === o2;
  }

  addLigneCommandeToCollectionIfMissing<Type extends Pick<ILigneCommande, 'id'>>(
    ligneCommandeCollection: Type[],
    ...ligneCommandesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ligneCommandes: Type[] = ligneCommandesToCheck.filter(isPresent);
    if (ligneCommandes.length > 0) {
      const ligneCommandeCollectionIdentifiers = ligneCommandeCollection.map(
        ligneCommandeItem => this.getLigneCommandeIdentifier(ligneCommandeItem)!
      );
      const ligneCommandesToAdd = ligneCommandes.filter(ligneCommandeItem => {
        const ligneCommandeIdentifier = this.getLigneCommandeIdentifier(ligneCommandeItem);
        if (ligneCommandeCollectionIdentifiers.includes(ligneCommandeIdentifier)) {
          return false;
        }
        ligneCommandeCollectionIdentifiers.push(ligneCommandeIdentifier);
        return true;
      });
      return [...ligneCommandesToAdd, ...ligneCommandeCollection];
    }
    return ligneCommandeCollection;
  }

  protected convertDateFromClient<T extends ILigneCommande | NewLigneCommande | PartialUpdateLigneCommande>(ligneCommande: T): RestOf<T> {
    return {
      ...ligneCommande,
      createdAt: ligneCommande.createdAt?.toJSON() ?? null,
      updateAt: ligneCommande.updateAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLigneCommande: RestLigneCommande): ILigneCommande {
    return {
      ...restLigneCommande,
      createdAt: restLigneCommande.createdAt ? dayjs(restLigneCommande.createdAt) : undefined,
      updateAt: restLigneCommande.updateAt ? dayjs(restLigneCommande.updateAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLigneCommande>): HttpResponse<ILigneCommande> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLigneCommande[]>): HttpResponse<ILigneCommande[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

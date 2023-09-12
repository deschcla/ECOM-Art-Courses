import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigneCommande } from '../ligne-commande.model';
import { LigneCommandeService } from '../service/ligne-commande.service';

export const ligneCommandeResolve = (route: ActivatedRouteSnapshot): Observable<null | ILigneCommande> => {
  const id = route.params['id'];
  if (id) {
    return inject(LigneCommandeService)
      .find(id)
      .pipe(
        mergeMap((ligneCommande: HttpResponse<ILigneCommande>) => {
          if (ligneCommande.body) {
            return of(ligneCommande.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ligneCommandeResolve;

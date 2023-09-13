import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousCategorie } from '../sous-categorie.model';
import { SousCategorieService } from '../service/sous-categorie.service';

export const sousCategorieResolve = (route: ActivatedRouteSnapshot): Observable<null | ISousCategorie> => {
  const id = route.params['id'];
  if (id) {
    return inject(SousCategorieService)
      .find(id)
      .pipe(
        mergeMap((sousCategorie: HttpResponse<ISousCategorie>) => {
          if (sousCategorie.body) {
            return of(sousCategorie.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default sousCategorieResolve;

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';

export const acheteurResolve = (route: ActivatedRouteSnapshot): Observable<null | IAcheteur> => {
  const id = route.params['id'];
  if (id) {
    return inject(AcheteurService)
      .find(id)
      .pipe(
        mergeMap((acheteur: HttpResponse<IAcheteur>) => {
          if (acheteur.body) {
            return of(acheteur.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default acheteurResolve;

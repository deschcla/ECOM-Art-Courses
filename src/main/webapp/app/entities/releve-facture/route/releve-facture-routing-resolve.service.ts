import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReleveFacture } from '../releve-facture.model';
import { ReleveFactureService } from '../service/releve-facture.service';

export const releveFactureResolve = (route: ActivatedRouteSnapshot): Observable<null | IReleveFacture> => {
  const id = route.params['id'];
  if (id) {
    return inject(ReleveFactureService)
      .find(id)
      .pipe(
        mergeMap((releveFacture: HttpResponse<IReleveFacture>) => {
          if (releveFacture.body) {
            return of(releveFacture.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default releveFactureResolve;

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';

export const carteBancaireResolve = (route: ActivatedRouteSnapshot): Observable<null | ICarteBancaire> => {
  const id = route.params['id'];
  if (id) {
    return inject(CarteBancaireService)
      .find(id)
      .pipe(
        mergeMap((carteBancaire: HttpResponse<ICarteBancaire>) => {
          if (carteBancaire.body) {
            return of(carteBancaire.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default carteBancaireResolve;

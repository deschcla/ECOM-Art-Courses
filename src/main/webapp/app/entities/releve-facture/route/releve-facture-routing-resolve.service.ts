import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReleveFacture } from '../releve-facture.model';
import { ReleveFactureService } from '../service/releve-facture.service';

@Injectable({ providedIn: 'root' })
export class ReleveFactureRoutingResolveService implements Resolve<IReleveFacture | null> {
  constructor(protected service: ReleveFactureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReleveFacture | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((releveFacture: HttpResponse<IReleveFacture>) => {
          if (releveFacture.body) {
            return of(releveFacture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

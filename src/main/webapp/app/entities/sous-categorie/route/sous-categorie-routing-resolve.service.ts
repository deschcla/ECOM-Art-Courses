import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousCategorie } from '../sous-categorie.model';
import { SousCategorieService } from '../service/sous-categorie.service';

@Injectable({ providedIn: 'root' })
export class SousCategorieRoutingResolveService implements Resolve<ISousCategorie | null> {
  constructor(protected service: SousCategorieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousCategorie | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousCategorie: HttpResponse<ISousCategorie>) => {
          if (sousCategorie.body) {
            return of(sousCategorie.body);
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

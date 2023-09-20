import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';

@Injectable({ providedIn: 'root' })
export class CategorieRoutingResolveService implements Resolve<ICategorie | null> {
  constructor(protected service: CategorieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategorie | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categorie: HttpResponse<ICategorie>) => {
          if (categorie.body) {
            return of(categorie.body);
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

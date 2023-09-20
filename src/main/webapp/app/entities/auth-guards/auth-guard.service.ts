import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from "./auth.service";


@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{
  constructor(private auth: AuthService, private router: Router) {}


  canActivate():boolean {
    if (this.auth.isAuthenticated()) {
      return true;
    }
    this.router.navigate(['']);
    return false
  }

}


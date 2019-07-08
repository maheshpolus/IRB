import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private _router: Router) {}

  /** watches for a 'ActivatedUser' in sessionstorage
   * reurn true if found
   * else 'IRB/dashboard' route is closed and routed to login
   */

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      if (sessionStorage.getItem('ActivatedUser')) {
      return true;
      } else {
        this._router.navigate(['/login']);
        return false;
      }
    }
}

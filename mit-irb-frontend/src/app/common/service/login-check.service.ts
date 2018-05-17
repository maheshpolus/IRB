import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class LoginCheckService {
    private loggedIn = new BehaviorSubject<boolean>( false );

    get isLoggedIn() {
        return this.loggedIn.asObservable();
    }

    constructor( private router: Router, private _http: HttpClient ) {
        if ( localStorage.getItem( 'currentUser' ) != null ) {
            this.loggedIn.next( true );
        } else {
            this.loggedIn.next( false );
            this.router.navigate( ['/login'] );
        }
    }

    login(params) {debugger;
        return this._http.post('/loginCheck', params);
    }

    logout() {
        this.loggedIn.next( false );
        this.router.navigate( ['/login'] );
    }
}

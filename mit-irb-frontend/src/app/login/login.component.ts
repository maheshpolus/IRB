import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Idle } from '@ng-idle/core';
import { LoginService } from './login.service';

@Component( {
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
} )

export class LoginComponent{

    result: any;
    loginFail = false;
    username: String;
    password: String;

    constructor(  private idle: Idle, private _loginService: LoginService, private _router: Router ) {
       // if (sessionStorage.getItem('ActivatedUser') != null) {
       //     this._router.navigate( ['/irb/dashboard'] );
       // }
        this.idle.stop();
    }

    login() {
        const requestObject = {
            userName: this.username,
            password: this.password
        };
        this._loginService.login( requestObject ).subscribe(
            data => {
                this.result = data || [];
                /**update ActivatedUser with logged in user once login service completed */
                sessionStorage.setItem('ActivatedUser', this.result.userName);
                if ( this.result != null ) {
                    this.idle.watch();
                    this._router.navigate( ['/irb/dashboard'] );
                }
            },
            error => {
                console.log( "Error in method login()", error );
            },
        );
    }
}

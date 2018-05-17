import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
// import { LoginCheckService } from '../common/service/login-check.service';
import { LoginService } from './login.service';
@Component( {
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
} )
export class LoginComponent implements OnInit {
    result: any;
    loginFail = false;
    username: String;
    password: String;
    constructor( private _loginService: LoginService, private router: Router ) { }

    ngOnInit() {  }

    login() {debugger;
        const requestObject = {
            username: this.username,
            password: this.password
        };
        /*this.router.navigate( ['/irb/dashboard'] );*/
        this._loginService.login( requestObject ).subscribe(
            data => {
                this.result = data || [];
                //**update ActivatedUser with logged in user once login service complated *//*
                sessionStorage.setItem('ActivatedUser', 'admin');
                if ( this.result != null ) {
                    this.router.navigate( ['/irb/dashboard'] );
                }
            },
            error => {
                console.log( error );
            },
        );
    }
    login1() {debugger;
    const requestObject = {
        username: this.username,
        password: this.password
    };
    /*this.router.navigate( ['/irb/dashboard'] );*/
    this._loginService.login1( requestObject ).subscribe(
        data => {
            this.result = data || [];
            //**update ActivatedUser with logged in user once login service complated *//*
            sessionStorage.setItem('ActivatedUser', 'admin');
            if ( this.result != null ) {
                this.router.navigate( ['/irb/dashboard'] );
            }
        },
        error => {
            console.log( error );
        },
    );
}
}

import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router} from '@angular/router';
import { HttpClient } from "@angular/common/http";
import { LoginService } from "../../login/login.service";

@Component( {
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
} )
export class HeaderComponent implements OnInit, AfterViewInit{
    userDTO: any;
    result: any;

    constructor( private _http: HttpClient, private _loginService: LoginService, private router: Router) { }

    ngOnInit() {
        this._http.get('/mit-irb/getUserDetails').subscribe( data => {
            this.result = data || [];
        },
            error => {
                 console.log( error );
            },
        );
    }
    ngAfterViewInit() {}

    logout() {debugger;
    this._loginService.logout().subscribe(data=>{
                this.router.navigate( ['/login'] );
        });
    }
}

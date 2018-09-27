import { Component, OnInit } from '@angular/core';
import { Router} from '@angular/router';

import { LoginService } from '../../login/login.service';

@Component( {
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
} )

export class HeaderComponent implements OnInit {
    requestObject = {
            userName :  sessionStorage.getItem('ActivatedUser')
    };
    
    result: any;

    constructor( private _loginService: LoginService, private _router: Router ) { }

    ngOnInit() { 
        this._loginService.getUserDetail(this.requestObject).subscribe( data => { 
            this.result = data || [];
            localStorage.setItem('userName', this.result.userName);
        },
            error => {
                 console.log( "Error in method getUserDetail()", error );
            },
        );
    }

    logout() {
    this._loginService.logout().subscribe(data => {
                sessionStorage.removeItem('ActivatedUser');
                if(data == true){
                    this._router.navigate( ['/login'] );
                } else{
                    this._router.navigate( ['/irb/dashboard'] );
                }
        });
    }
}

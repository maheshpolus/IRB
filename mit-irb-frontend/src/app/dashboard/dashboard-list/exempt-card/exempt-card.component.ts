import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';


@Component( {
    selector: 'app-exempt-card',
    templateUrl: './exempt-card.component.html',
    styleUrls: ['./exempt-card.component.css']
} )
export class ExemptCardComponent implements OnInit {

    /**input from parent componet Exempt list */
    @Input() exemptList: any = [];
    @Input() userDTO: any;
    @Input() showBtn: boolean;

    statusCode: string[];
    tabSelected: string = "STUDIES";
    
    ngOnInit() {
        this.showData( this.userDTO.role, 'STUDIES' )
    }

    constructor( private _router: Router ) { }

    /**Opens Expemt Exempt Component
     * @exemptId - unique id for exempt form
     * @mode - view or edit mode
     * @add question ID to route as query params
    */
    openExempt( exemptId, mode, exemptTitle, exemptFormID ) {
        if ( mode == null ) {
            mode = 1;
        }
        this._router.navigate( ['/irb/exempt-questionaire'],
            { queryParams: { exempHeaderId: exemptId, mode: mode, title: exemptTitle, exemptFormID: exemptFormID } } );
    }

    public showData( userRole, tabClicked ) {
        this.tabSelected = tabClicked;
        this.statusCode = this.getStatusCode( userRole, tabClicked );
    }
    
    getStatusCode( role, tabClicked ) {
        if ( role == 'PI' && this.userDTO.jobTitle !== null && tabClicked == "PENDING" )
            return ["2", "5"];
        else if ( role == 'PI' && this.userDTO.jobTitle !== null && tabClicked == "STUDIES" )
            return ["1", "2", "3", "4"];
        else if ( ( role == 'ADMIN' || role == 'CHAIR' ) && tabClicked == "STUDIES" )
            return ["1"];
        else if ( ( role == 'ADMIN' || role == 'CHAIR' ) && tabClicked == "PENDING" )
            return ["3", "5"];
        else if ( ( role == 'ADMIN' || role == 'CHAIR' ) && tabClicked == "SUBMITTED" )
            return ["4"];
        else if ( role == 'PI' && this.userDTO.jobTitle == null && tabClicked == "STUDIES" )
            return ["1", "2", "3", "4"];
        else if ( role == 'PI' && this.userDTO.jobTitle == null && tabClicked == "PENDING" )
            return ["5"];
    }
}

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class CommitteCreateEditService {
    constructor( private http: HttpClient) {

    }

    getCommitteeData(): Observable<JSON> {
        const params = null;
        return this.http.post( '/irb/createCommittee', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    loadCommittee( committeeId: string ): Observable<JSON> {
        const params = {
            committeeId: committeeId,
        };
        return this.http.post( '/irb/loadCommitteeById', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    loadScheduleDetailsById( params ): Observable<JSON> {
        return this.http.post( '/irb/loadScheduleDetailsById', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    addMember( Id: string, committeeId: string, nonEmployeeFlag: boolean, committeeObj: Object ): Observable<JSON> {
        let params: any = {};
        if ( nonEmployeeFlag === true ) {
            params = {
                rolodexId: Id,
                committeeId: committeeId,
                nonEmployeeFlag: nonEmployeeFlag,
                committee: committeeObj
            };
        }
        if ( nonEmployeeFlag === false ) {
            params = {
                personId: Id,
                committeeId: committeeId,
                nonEmployeeFlag: nonEmployeeFlag,
                committee: committeeObj
            };

        }
        return this.http.post( '/irb/addCommitteeMembership', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    saveCommitteeMembers( CommiteeeObj: Object ): Observable<JSON> {
        return this.http.post( '/irb/saveCommitteeMembers', CommiteeeObj )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    deleteRoles( commMemberRolesId: number, commMembershipId: string, committeeId: string ) {
        const params = {
            commMemberRolesId: commMemberRolesId,
            committeeId: committeeId,
            commMembershipId: commMembershipId

        };
        return this.http.post( '/irb/deleteMemberRoles', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }


    deleteExpertises( commMemberExpertiseId: number, commMembershipId: string, committeeId: string ) {
        const params = {
            commMemberExpertiseId: commMemberExpertiseId,
            committeeId: committeeId,
            commMembershipId: commMembershipId
        };
        return this.http.post( '/irb/deleteMemberExpertise', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    deleteMember( commMembershipId: string, committeeId: string ) {
        const params = {
            committeeId: committeeId,
            commMembershipId: commMembershipId
        };
        return this.http.post( '/irb/deleteCommitteeMembers', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    saveCommMemberRole( commMembershipId: string, committeeId: string, committeeMemberRole: object ) {
        const params = {
            committeeId: committeeId,
            commMembershipId: commMembershipId,
            committeeMemberRole: committeeMemberRole
        };
        return this.http.post( '/irb/saveCommitteeMembersRole', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    saveCommMemberExpertise( commMembershipId: string, committeeId: string, committeeMemberExpertise: object ) {
        const params = {
            committeeId: committeeId,
            commMembershipId: commMembershipId,
            committeeMemberExpertise: committeeMemberExpertise
        };
        return this.http.post( '/irb/saveCommitteeMembersExpertise', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    updateMemberRoles( commMemberRolesId, committeeId, commMembershipId, role ) {
        const params = {
            commMemberRolesId: commMemberRolesId,
            committeeId: committeeId,
            commMembershipId: commMembershipId,
            committeeMemberRole: role

        };
        return this.http.post( '/irb/updateMemberRoles', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }
}

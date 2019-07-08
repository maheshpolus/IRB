import { Component, OnInit } from '@angular/core';

import { CompleterService, CompleterData } from 'ng2-completer';
import * as _ from 'lodash';

import { SearchService } from './search.service';

@Component({
    selector: 'app-search',
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit {
    public dataService: CompleterData;
    codeTableProperty: any = {};
    tableProperty: any = {};
    checkedList: any = {};
    codeTableValues: any = [];
    groupNames: any = [];
    groupNameSelected: any;
    SearchTableName: any;
    codeTableLength: number;
    selectedId: number;
    isVisible = false;
    isCollapse = false;
    isSavedFlag = {
        isSaved: null,
        isgrpNameChange: false
    };
    helpInfo;

    constructor(private _tableService: SearchService,
        private _completerService: CompleterService) { }

    ngOnInit() {
        this._tableService.getTableProperties()
            .subscribe(
                data => {
                    this.tableProperty = data;
                    this.groupNames = this.tableProperty.configFile.groups;
                    this.checkedList.groupName = this.groupNames[0];
                    this.dataService = this._completerService.local
                        (this.tableProperty.configFile.codetables,
                            'group,codetable_name', 'codetable_name');
                }, err => {
                    console.log('Error in Fetching Data', err);
                });
    }
    /**
     * @param  {} groupName
     * filters the codetable names with respect to group name,
     * and returns them on the radio button click.
     */
    getCodeTableList(groupName: any) {
        if (groupName !== undefined) {
            return _.filter(this.tableProperty.configFile.codetables, { 'group': groupName });
        }
    }
    /**
     * @param  {} tablename
     * filter out properties of searched table selected from search dropdown in the top.
     * when a table name is selcted from the dropdown collapses the codetable name list which shows under radio button list.
     */
    getSearchedTable(tablename: any) {
        if (tablename != null) {
            const group = tablename.originalObject.group;
            const codetable_name = tablename.originalObject.codetable_name;
            if (this.isCollapse === false) {
                document.getElementById('advancesearch').click();
            }
            this.getSelectedTable(group, codetable_name, 1);
            this.getSelectedId(1, codetable_name);
        }
    }
    /**
     * @param  {} modulename
     * @param  {} tablename
     * filter out properties of searched table selected from advance search box
     * isvisible is set for visibility of child component(codetable component).
     * the selected tables properties are passed into the child component.
     * defaultdly set the value to '0' to distinguish between the caller of the function 'getSelectedTable()'.
     * value is used for managing accordion working.
     */
    getSelectedTable(modulename, tablename, value = 0) {
        if (this.isSavedFlag.isSaved === true || this.isSavedFlag.isSaved === null) {
            this.SearchTableName = tablename;
            this.isVisible = true;
            this.codeTableProperty = {};
            this.codeTableProperty = this.tableProperty.configFile.codetables.filter(fieldList => (
                (modulename === fieldList.group) && (tablename === fieldList.codetable_name)))[0];
            if (value === 0) {
                document.getElementById('advancesearch').click();
            }
        }
    }
    /**
     * @param  {} id
     * sets the selected column name index value and group name to sets the color change for the selected codetable name
     */
    getSelectedId(id, groupName) {
        this.selectedId = id;
        this.groupNameSelected = groupName;
    }
    /**
     * to set accordion arrow.
     */
    collapseAdvanceSearch() {
        this.isCollapse === false ? this.isCollapse = true : this.isCollapse = false;
    }
    /**
     * opens accordion when module radio button is clicked.
     */
    accordionShow() {
        this.SearchTableName = '';
        this.isVisible = false;
        this.selectedId = -1;
        document.getElementById('collapseSearch').className = 'show';
    }
}

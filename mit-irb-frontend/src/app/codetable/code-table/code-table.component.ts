import { Component, Input,  OnChanges } from '@angular/core';
import { CodeTableService } from './code-table.service';


@Component({
  selector   : 'app-code-table',
  templateUrl: './code-table.component.html',
  styleUrls  : ['./code-table.component.css']
})

export class CodeTableComponent implements OnChanges {

  @Input() codeTableProperty: any = {};
  @Input() isSavedFlag: any = {};
  updatedCodeTableData: any = {};
  newCodeTableData: any = {};
  codeTableData:  any;
  codeTableValues:  any = [];
  isEditEnable:  any = [];
  tableFieldList: any = [];
  dataType: any = [];
  isPrimary: any = [];
  isValueEmpty: any = [];
  updatedCodeTable: any = {
    codetable: {},
    tableData: [],
    updatedUser: ''
  };
  isLength = false;
  isEmpty  = false;
  isType = false;
  isNoData = false;
  isDependency = false;
  toastMessage: any;
  updatedData:  any;
  deleteIndex: number;
  id: number;
  tempId: number;
  Id: number;
  cancelData: any;
  totalColumnNo;
  placeHolder = 'Search ';
  searchText: any;

  constructor(private _codeTableService: CodeTableService) { }

  ngOnChanges() {
    this.dataType = [];
    this.tableFieldList = [];
    this.isValueEmpty = [];
    this.codeTableValues = [];
    this.isEditEnable = [];
    this.updatedCodeTableData = {};
    this.placeHolder = 'Search ';
    this.searchText = '';
    localStorage.setItem('currentUser', 'admin');
    this.updatedCodeTable.updatedUser = localStorage.getItem('currentUser');
    this.setValueChaged();
    this.tableFieldList = this.codeTableProperty.fields.filter(fieldList => fieldList.visible === 'true');
    this.totalColumnNo = this.tableFieldList.length;
    (this.codeTableProperty.dependency.length !== 0) ? this.isDependency = true : this.isDependency = false;
    this.tableFieldList.forEach(element => {
          this.placeHolder = this.placeHolder + element.display_name + ', ';
          this.isValueEmpty.push(false);
          if (element.data_type === 'Integer') {
            this.dataType.push({'type': 'number', 'display_type': 'numbers'});
          } else if (element.data_type === 'String') {
            this.dataType.push({'type': 'text', 'display_type': 'characters'});
          } else if (element.data_type === 'Clob') {
            this.dataType.push({'type': 'file', 'display_type': 'files'});
          } else if (element.data_type === 'Date') {
            this.dataType.push({'type': 'date', 'display_type': ''});
          } else {
            this.dataType.push({'type': '', 'display_type': ''});
          }
    });
    this.placeHolder = this.placeHolder + '. . .';
    this.updatedCodeTable.codetable = this.codeTableProperty;
    this.getCodeTable();
  }
  /**
   * returns the selected codetable values
   * stores a true value for isEditEnable for all rows of values
   */
  getCodeTable() {
    this._codeTableService.getCodetableValues(this.updatedCodeTable)
    .subscribe(data => {
          this.codeTableData = data;
          this.codeTableValues = this.codeTableData.tableData;
          this.codeTableValues.length === 0 ? this.isNoData = true : this.isNoData = false;
          if (!this.isNoData) {
            this.codeTableValues.forEach(element => {
                this.isEditEnable.push(true);
            });
          }
      }, err => {
          console.log('Error in Fetching Data', err);
    });
  }
  /**
   * @param  {} id
   * @param  {} values
   * sets isEditEnable to false for the currently selected row for editing
   * sets isValueEmpty to true for all the fields in the row; for field can be empty or not validations.
   */
  editCodeTable(id, values) {
    if (this.isSavedFlag.isSaved || this.isSavedFlag.isSaved == null) {
      this.updatedCodeTableData = {};
      this.setValueChaged();
    }
    if (Object.getOwnPropertyNames(this.updatedCodeTableData).length === 0) {
      this.Id = id;
      this.isEmpty = false;
      this.isType = false;
      this.isLength = false;
      this.updatedCodeTableData = {};
      this.codeTableValues.forEach((value, index) => {
                      if (index === id) {
                          this.isEditEnable[index] = false;
                      } else {
                          this.isEditEnable[index] = true;
                      }
                    });
      this.updatedCodeTableData = values;
      this.cancelData = Object.assign({}, values);
      this.tableFieldList.forEach((field, index) => {
              this.isValueEmpty[index] = true;
      });
    }
  }
  /**
   * @param  {} index
   * saves changes on edit and updates it
   */
  updateCodetable(index) {
    this.isEmpty = false;
    this.isEditEnable[index] = true;
    delete this.updatedCodeTableData.isEditable;
    this.updatedCodeTableData.UPDATE_TIMESTAMP = '1234';
    this.updatedCodeTable.tableData[0] = this.updatedCodeTableData;
    if (this.isSavedFlag.isSaved === false) {
      this._codeTableService.getUpdatedTableValues(this.updatedCodeTable)
      .subscribe(data => {
          this.updatedData  = data;
          if (this.updatedData.promptCode === 1) {
              this.codeTableValues[index] = this.updatedData.tableData[0];
              this.toastMessage = this.updatedData.promptMessage;
              this.toastSuccess(this.toastMessage);
          } else {
              this.toastMessage = this.updatedData.promptMessage;
              this.toastWarning(this.toastMessage);
          }
      });
    }
    this.updatedCodeTableData = {};
    this.isSavedFlag.isSaved = true;
  }
  /**
   * @param  {} column_name
   * sets the updated column name,
   * sets value_changed of updated rows to true.
   */
  setCodeTableField(column_name, i, type, value = 0) {
      const index = this.codeTableProperty.fields.findIndex(fields => column_name === fields.column_name);
      this.codeTableProperty.fields[index].value_changed = 'true';
      this.tempId = i;
      if (type === 'date') {
          this.isEmpty = false;
          this.isValueEmpty[i] = true;
      }
      if (value === 1) {
        this.isSavedFlag.isSaved = false;
      }
  }
  /**
   * @param  {} event
   * @param  {} length
   * @param  {} type
   * @param  {} id
   * @param  {} canEmpty
   * @param  {} columnname
   * checks validation
   */
  setValidation(event, length, type, id, canEmpty, columnname) {
    this.id = id;
    if ( event.target.value.length < length ) {
      this.isLength = false;
    } else {
        this.isLength = true;
        event.target.value = event.target.value.slice(0, length);
        this.newCodeTableData[columnname] = event.target.value;
        this.updatedCodeTableData[columnname] = event.target.value;
      }
    if ( type === 'Integer' && event.keyCode >= 65 && event.keyCode <= 90 ) {
      this.isType = true;
      event.target.value = event.target.value.slice(0, 1);
    } else {
        this.isType = false;
      }
    if ( event.target.value.length === 0 && canEmpty === 'false' ) {
      this.isEmpty = true;
      this.isValueEmpty[id] = false;
    } else {
        this.isEmpty = false;
        this.isValueEmpty[id] = true;
      }
  }
  /**
   * add new row of values to code table
   */
  addNewCodeTableData() {
    this.updatedCodeTableData = {};
    if ( this.isValueEmpty.filter(empty => empty === false).length !== 0 ) {
      this.isEmpty = true;
    } else {
        this.isEmpty = false;
        document.getElementById('closeModal').click();
        // if (Object.getOwnPropertyNames(this.newCodeTableData).length !== 0) {
            this.updatedCodeTable.UPDATE_TIMESTAMP = '1234';
            this.updatedCodeTable.tableData[0] = this.newCodeTableData;
            this.updatedCodeTable.tableData[0].UPDATE_TIMESTAMP = '1234';
            this._codeTableService.addNewCodeTableData(this.updatedCodeTable)
            .subscribe(data => {
              this.updatedData = data;
              if ( this.updatedData.promptCode === 1) {
                this.updatedData.tableData.length === 0 ? this.isNoData = true : this.isNoData = false;
                this.toastMessage = this.updatedData.promptMessage;
                this.toastSuccess(this.toastMessage);
                this.isEditEnable[this.codeTableValues.length] = 'true';
                this.codeTableValues.splice( 0, 0, this.updatedData.tableData[0]);
              } else {
                  this.toastMessage = this.updatedData.promptMessage;
                  this.toastWarning(this.toastMessage);
                }
              this.setNewCreation();
            });
      //  }
    }
  }
  /**
   * @param  {} index
   * Sets index of the row , selected for delete to deleteIndex
   */
  codeTableDataIndex(index) {
    this.deleteIndex = index;
  }
  /**
   * removes the selected row from codetable
   */
  deleteCodeTableData() {
    const REMOVEDDATA = this.codeTableValues[this.deleteIndex];
    this.updatedCodeTable.tableData[0] = REMOVEDDATA;
    this._codeTableService.removeSelectedData(this.updatedCodeTable)
      .subscribe(data => {
          this.updatedData  = data;
          if ( this.updatedData.promptCode === 1 ) {
            this.codeTableValues.length === 1 ? this.isNoData = true : this.isNoData = false;
            this.toastMessage = this.updatedData.promptMessage;
            this.toastSuccess(this.toastMessage);
            this.codeTableValues.splice(this.deleteIndex, 1);
          } else {
              this.toastMessage = this.updatedData.promptMessage;
              this.toastWarning(this.toastMessage);
            }
    });
    this.isSavedFlag.isSaved = true;
    this.isEditEnable[this.deleteIndex] = true;
    this.updatedCodeTableData = {};
  }
  /**
   * sets requirements for new creation:
   * sets true value to primary keys which are not editable
   * sets true value to fields which can be empty.
   */
  setNewCreation() {
    this.isPrimary = [];
    this.isLength = false;
    this.isType = false;
    this.isEmpty = false;
    this.newCodeTableData = {};
    if (this.isSavedFlag.isSaved === false ) {
      this.toastMessage = 'Save the changes';
      this.toastWarning(this.toastMessage);
    } else {
        this.isEditEnable[this.Id] = true;
        this.updatedCodeTableData = {};
      }
    this.tableFieldList.forEach((field, index) => {
      if (this.codeTableProperty.primary_key.find((key) => key === field.column_name && field.is_editable === 'false')) {
        this.isPrimary.push(true);
        this.isValueEmpty[index] = true;
      } else if (field.can_empty === 'true' || field.is_editable === 'false') {
          this.isValueEmpty[index] = true;
          this.isPrimary.push(false);
      } else {
          this.isPrimary.push(false);
          this.isValueEmpty[index] = false;
        }
    });
   this.setValueChaged();
  }
  /**
   * @param  {} toastMessage
   * shows success message
   */
  toastSuccess(toastMessage) {
    const TOASTID = document.getElementById('toast-success');
    this._codeTableService.showToast(TOASTID);
    this.toastMessage = toastMessage;
  }
  /**
   * @param  {} toastMessage
   * shows warning message
   */
  toastWarning(toastMessage) {
    const TOASTID = document.getElementById('toast-warning');
    this._codeTableService.showToast(TOASTID);
    this.toastMessage = toastMessage;
  }
  /**
   * @param  {} index
   * To cancel changes in the previos edit or cancel changes in the current edit
   */
  cancelChanges(index) {
    this.isSavedFlag.isSaved = true;
    this.isEditEnable[index] = true;
    this.codeTableValues[index] = this.cancelData;
    this.updatedCodeTableData = {};
  }
  /**
   * Initialises the value_changed property of UPDATE_TIMESTAP to true and others to false.
   */
  setValueChaged() {
    this.codeTableProperty.fields.forEach(element => {
      (element.column_name === 'UPDATE_TIMESTAMP') ? element.value_changed = 'true' : element.value_changed = 'null';
    });
  }
}


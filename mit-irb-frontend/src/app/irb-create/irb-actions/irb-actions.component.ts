import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-irb-actions',
  templateUrl: './irb-actions.component.html',
  styleUrls: ['./irb-actions.component.css']
})
export class IrbActionsComponent implements OnInit {

  generalInfo = {};
  submissionTypes = [];
  typeQualifier = [];
  reviewTypes = [];
  committees = [];
  scheduleDates = [];


  constructor() { }

  ngOnInit() {
    this.submissionTypes = [{ type: 'Initial Protocol Application for Approval' },
    { type: 'Continuing Review/Continuation without Amendment' },
    { type: 'Amendment' },
    { type: 'Response to Previous IRB Notification' },
    { type: 'Self Report of Non-Compliance' },
    { type: 'Complaint' }
    ];
    this.typeQualifier = [{ type: 'AE/UADE' },
    { type: 'Annual Report' },
    { type: 'Annual Scheduled by IRB' },
    { type: 'Contingent/Conditional Approval/Deffered Approval/Non Approval' },
    { type: 'DSMB Report' },
    { type: 'Deviation' },
    { type: 'Eligible Exceptions/Protocol Deviation' },
    ];
    this.reviewTypes = [{ type: 'Full' },
    { type: 'Expedited' },
    { type: 'Exempt' },
    { type: 'Committee' },
    { type: 'COUHES' },
    ];
    this.scheduleDates = [{ type: '10-06-2018, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '11-06-2018, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '12-06-2018, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '01-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '02-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '30-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '04-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    ];
    this.committees = [{ type: 'COUHES' },
    ];
  }

}

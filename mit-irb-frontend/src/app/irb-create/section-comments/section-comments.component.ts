import { Component, OnInit, Input } from '@angular/core';
import { IrbCreateService } from '../irb-create.service';
import { ISubscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-section-comments',
  templateUrl: './section-comments.component.html',
  styleUrls: ['./section-comments.component.css']
})
export class SectionCommentsComponent implements OnInit {

  constructor(private _irbCreateService: IrbCreateService) { }
  @Input() CommentSection: string;
  @Input() minimize: boolean;
  @Input() maximize: boolean;
  showCommentBox = false;
  $showCommentBox: ISubscription;

  ngOnInit() {
    this.$showCommentBox = this._irbCreateService.showSectionComment.subscribe( (data: any) => {
      this.showCommentBox = true;
      this.CommentSection = data;
    });
  }
}

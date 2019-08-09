import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-comment-box',
  templateUrl: './comment-box.component.html',
  styleUrls: ['./comment-box.component.css']
})
export class CommentBoxComponent implements OnInit {

  constructor() { }
  @Input() commentedSection: String;
  @Input() reviewPersons: Array<object>;

  selectedPerson: any;
  personName = '';
  selectedpersonCode: number;
  isPublic = false;
  commentObject = {
    selectedPerson : 'Carol Wood',
    isPublic: false,
    comment: '',
    commentedSection: this.commentedSection,
    reply: {
      isShowReply: false,
      person: '',
      reply: '',
      isReplied: false,
      repliedTime: ''
    },
    resolved: false,
    commentedTime: ''
  };
  commentsList = [];

  ngOnInit() {
  }
  addComment() {
    if (this.commentObject.comment  !== '' ) {
      this.commentObject.commentedTime = new Date(Date.now()).toString().slice(0, 25);
      this.commentsList.push(JSON.parse(JSON.stringify(this.commentObject)));
      this.clearCommentObject();
    }
  }
  onPersonSelect() {
    if (this.selectedPerson) {
      this.personName = this.selectedPerson.description;
      this.selectedpersonCode = this.selectedPerson.code;
    }
  }

  clearCommentObject() {
    this.commentObject = {
      selectedPerson : 'Carol Wood',
      isPublic: false,
      comment: '',
      commentedSection: this.commentedSection,
      reply: {
        isShowReply: false,
        person: '',
        reply: '',
        isReplied: false,
        repliedTime: ''
      },
      resolved: false,
      commentedTime: ''
    };
  }
  addReply(index) {
    this.commentsList[index].reply.repliedTime = new Date(Date.now()).toString().slice(0, 25);;
    this.commentsList[index].reply.isShowReply = false;
    this.commentsList[index].reply.isReplied = true;
  }

}

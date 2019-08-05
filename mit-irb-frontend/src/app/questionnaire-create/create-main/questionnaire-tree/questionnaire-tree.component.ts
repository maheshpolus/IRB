import { Component, Input, OnChanges, ChangeDetectionStrategy, OnInit, ChangeDetectorRef} from '@angular/core';
import * as _ from 'lodash';
import { CreateQuestionnaireService } from '../../services/create.service';
import {easeIn } from '../../services/animations';

@Component({
  selector: 'app-questionnaire-tree',
  templateUrl: './questionnaire-tree.component.html',
  styleUrls: ['./questionnaire-tree.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [easeIn],
})
export class QuestionnaireTreeComponent implements OnInit, OnChanges {
  selectedNode: any;
  groupLabels: any;
  isMaximized: boolean;
  expandedTreeData = [];

  constructor(private _createQuestionnaireService: CreateQuestionnaireService, 
    private _changeRef: ChangeDetectorRef) { }
  @Input() data: any;
  @Input() nodes: any;
  highlightNode = null;
  treeData = [];

  ngOnInit() {
    this.updateSelectedQuestion();
    this.updateTree();
  }
  ngOnChanges() {
    this.createTreeNodes();
    // this.highlightNode = this.editIndex;
  }
  updateHighlightNode(questionId) {
    this.highlightNode = questionId;
    this._createQuestionnaireService.updateSelectedQuestionId.next(questionId);
  }
  /** updates the width of the tree to maximun according to the size of the current screen
   */
  maximizeTree() {
    this.isMaximized = true;
    (document.getElementsByClassName('qst-tree-content')[0] as HTMLElement).style.width = window.innerWidth - 85 + 'px' ;
    (document.getElementsByClassName('qst-tree-heading')[0] as HTMLElement).style.width = window.innerWidth - 85 + 'px' ;
      this.createExpandedTreeData();
  }
   /** updates the width of the tree to 192px which fits into the left size of the screen
   */
  minimizeTree() {
    this.isMaximized = false;
    (document.getElementsByClassName('qst-tree-content')[0] as HTMLElement).style.width = '14vw' ;
    (document.getElementsByClassName('qst-tree-heading')[0] as HTMLElement).style.width = '14vw' ;
  }
  /**
   * creates the questionnaire heirarchy tree for a given questionnaire.
   * creates the node to be appended to the tree
   * creates an object with respective groupname and group labels
   */
  createTreeNodes() {
    this.nodes.nodes = [];
    _.forEach( this.data.questionnaire.questions , (question, key) => {
      const newNode = {
        questionId: question.QUESTION_ID,
        name : 'Q ' + question.QUESTION_ID,
        children : [],
      };
      if (question.GROUP_NAME === 'G0') {
        this.nodes.nodes.push(newNode);
      } else {
        this.addChildToTree(this.nodes.nodes, question.PARENT_QUESTION_ID , newNode);
      }
    });
    this.treeData = this.nodes.nodes;
  }
  /**
   * @param  {} nodes
   * @param  {} parentId
   * @param  {} childId
   * @param  {} groupName
   * Traverese the existing tree to find the exact postion of parent node.
   * pushes the created node to the children of parent tree and breaks the tree traversal
   */
  addChildToTree( nodes, parentId, newNode) {
    _.forEach(nodes, (node) => {
      if (node.questionId === parentId) {
        node.children.push(newNode);
        return false;
      } else if ( node.children.length > 0) {
        this.addChildToTree(node.children, parentId, newNode);
      }
    });
  }
  updateTree() {
    this._createQuestionnaireService.updateTree.subscribe(
      (data: any) => {
        // if (data.groupName === 'G0') {
        //   this.addParentToTree(data.questionId, data.groupName);
        // } else if (data.groupName) {
        //   const newNode = {
        //     questionId: data.questionId,
        //     name : 'Q ' + data.questionId,
        //     groupName: data.groupName,
        //     children : [],
        //   };
        //   this.addChildToTree(this.nodes.nodes, data.parentId, newNode);
        // } else {
          this.createTreeNodes();
        // }
        this._changeRef.markForCheck();
      });
  }
  updateSelectedQuestion() {
    this._createQuestionnaireService.updateSelectedQuestionId.subscribe(
      (data: number) => {
         this.highlightNode = data;
         this._changeRef.markForCheck();
      });
  }
  /**
   * @param  {} questionId
   * @param  {} groupName
   * adds a base node to tree(G0) simply pushes the created node to the nodes array
   */
  addParentToTree( questionId, groupName) {
    this.nodes.nodes.push({ questionId: questionId, name: 'Q ' + questionId, groupName: groupName, children: [] });
  }
  /**u
   * used to create expanded tree with full question and its conditions.
   *@returns create a tree heirachy with all data
   */
  createExpandedTreeData() {
    this.expandedTreeData = [];
    _.forEach( this.data.questionnaire.questions , (question) => {
      const newNode = {
        questionId: question.QUESTION_ID,
        name : 'Q ' + question.QUESTION_ID,
        content: question.QUESTION,
        children : [],
        groupName : question.GROUP_NAME
      };
      const conditons = _.filter(this.data.questionnaire.conditions , {'QUESTION_ID' : question.QUESTION_ID});
      conditons.forEach((conditon: any) => {
        newNode.children.push({'condition': conditon.CONDITION_VALUE, children: [],
        'conditionGroup': conditon.GROUP_NAME, 'type': conditon.CONDITION_TYPE, 'questionId': conditon.QUESTION_ID});
      });
      if (question.GROUP_NAME === 'G0') {
        this.expandedTreeData.push(newNode);
      } else {
        this.addChildToExpandedTree(this.expandedTreeData, newNode, question.PARENT_QUESTION_ID);
      }
    });
  }

  /**
   * @param  {} nodes
   * @param  {} newNode
   * @param  {} parentId
   * add child to expandedTreeData according to the group name in the data groupname of the
   * node is used to match with treeData
   */
  addChildToExpandedTree(nodes, newNode, parentId) {
    _.forEach(nodes, (node) => {
      if (node.questionId === parentId) {
        node.children.forEach(child => {
          if (child.conditionGroup === newNode.groupName) {
            child.children.push(newNode);
          }
        });
        return false;
      } else if ( node.children.length > 0) {
        this.addChildToExpandedTree(node.children, parentId, newNode);
      }
    });
  }
}

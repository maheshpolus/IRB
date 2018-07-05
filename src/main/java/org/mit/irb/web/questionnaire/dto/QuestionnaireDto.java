package org.mit.irb.web.questionnaire.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionnaireDto {
	private List<QuestionnaireQuestionDto> questionnaireQuestions;
	private ArrayList<HashMap<String, Object>> questionnaireConditions;
	private ArrayList<HashMap<String, Object>>  questionnaireOptions;
	private ArrayList<HashMap<String, Object>>  questionnaireAnswers;	
	private List<QuestionnaireAttachmentDto> quesAttachmentList;

	public ArrayList<HashMap<String, Object>> getQuestionnaireConditions() {
		return questionnaireConditions;
	}

	public void setQuestionnaireConditions(ArrayList<HashMap<String, Object>> questionnaireConditions) {
		this.questionnaireConditions = questionnaireConditions;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireOptions() {
		return questionnaireOptions;
	}

	public void setQuestionnaireOptions(ArrayList<HashMap<String, Object>> questionnaireOptions) {
		this.questionnaireOptions = questionnaireOptions;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireAnswers() {
		return questionnaireAnswers;
	}

	public void setQuestionnaireAnswers(ArrayList<HashMap<String, Object>> questionnaireAnswers) {
		this.questionnaireAnswers = questionnaireAnswers;
	}

	public List<QuestionnaireQuestionDto> getQuestionnaireQuestions() {
		return questionnaireQuestions;
	}

	public void setQuestionnaireQuestions(List<QuestionnaireQuestionDto> questionnaireQuestions) {
		this.questionnaireQuestions = questionnaireQuestions;
	}

	public List<QuestionnaireAttachmentDto> getQuesAttachmentList() {
		return quesAttachmentList;
	}

	public void setQuesAttachmentList(List<QuestionnaireAttachmentDto> quesAttachmentList) {
		this.quesAttachmentList = quesAttachmentList;
	}
}

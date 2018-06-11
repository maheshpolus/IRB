package org.mit.irb.web.questionnaire.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.questionnaire.dto.QuestionnaireAnswerDto;
import org.mit.irb.web.questionnaire.dto.QuestionnaireAttachmentDto;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.mit.irb.web.questionnaire.dto.QuestionnaireQuestionDto;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.stereotype.Service;

@Service(value = "questionnaireService")
public class QuestionnaireServiceImpl implements QuestionnaireService{
	protected static Logger logger = Logger.getLogger(QuestionnaireServiceImpl.class.getName());
	private DBEngine dbEngine; 
	public QuestionnaireServiceImpl(){
		dbEngine = new DBEngine();
	}

	@Override
	public QuestionnaireDto getQuestionnaireDetails(Integer moduleCode, String moduleItemId) throws Exception {
		QuestionnaireDto QuestionnaireDto = new QuestionnaireDto(); 
		try{
			ArrayList<InParameter> inParam = new ArrayList<>();	
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("av_questionnaire_id", DBEngineConstants.TYPE_INTEGER,generateQuestionnaireId(moduleCode, moduleItemId)));
			outParam.add(new OutParameter("resultset",DBEngineConstants.TYPE_RESULTSET));		
			List<QuestionnaireAnswerDto> answerList =  new ArrayList<QuestionnaireAnswerDto>();
			if(moduleItemId != null){
				ArrayList<InParameter> inputParam = new ArrayList<>();
				inputParam.add(new InParameter("av_module_item_id", DBEngineConstants.TYPE_INTEGER,Integer.parseInt(moduleItemId)));
				inputParam.add(new InParameter("av_module_code", DBEngineConstants.TYPE_INTEGER,moduleCode));
				ArrayList<HashMap<String,Object>> QuestionnaireAnswerDtoMap = 
						dbEngine.executeProcedure(inputParam,"get_mitkc_qnr_qstn_answer",outParam);
				if(QuestionnaireAnswerDtoMap != null && !QuestionnaireAnswerDtoMap.isEmpty()){
					for(HashMap<String,Object> answersDetails: QuestionnaireAnswerDtoMap){
						QuestionnaireAnswerDto QuestionnaireAnswerDto = new QuestionnaireAnswerDto();	
						QuestionnaireAnswerDto.setQuestionnaireAnswerHeaderId(Integer.parseInt((answersDetails.get("QUESTIONNAIRE_ANSWER_ID").toString())));
						if(answersDetails.get("ANSWER_NUMBER") != null){
							QuestionnaireAnswerDto.setAnswerNumber(Integer.parseInt((answersDetails.get("ANSWER_NUMBER").toString())));
						}				
						if(answersDetails.get("ANSWER_LOOKUP_CODE") != null){
							QuestionnaireAnswerDto.setAnswerLookupCode(answersDetails.get("ANSWER_LOOKUP_CODE").toString());
						}
						if(answersDetails.get("QUESTION_ID") != null){
							QuestionnaireAnswerDto.setQuestionId(Integer.parseInt((answersDetails.get("QUESTION_ID").toString())));
							}
						if(answersDetails.get("ANSWER") != null){
							QuestionnaireAnswerDto.setSelectedAnswer(answersDetails.get("ANSWER").toString());
							}
						if(answersDetails.get("EXPLANATION") != null){
							QuestionnaireAnswerDto.setExplanation(answersDetails.get("EXPLANATION").toString());
							}
						if(answersDetails.get("ATTACHMENT_ID") != null){
							QuestionnaireAnswerDto.setAttachmentId(Integer.parseInt((answersDetails.get("ATTACHMENT_ID").toString())));
							}
						QuestionnaireAnswerDto.setQuestionnaireAnswerHeaderId(Integer.parseInt((answersDetails.get("QUESTIONNAIRE_ANS_HEADER_ID").toString())));
						answerList.add(QuestionnaireAnswerDto);
					}
				}			
			}	
			ArrayList<HashMap<String,Object>> questionnaireQuestionsMap = 
					dbEngine.executeProcedure(inParam,"get_mitkc_qnr_qstn",outParam);
			List<QuestionnaireQuestionDto> questionsList =  new ArrayList<QuestionnaireQuestionDto>();
			if(questionnaireQuestionsMap != null && !questionnaireQuestionsMap.isEmpty()){
				for(HashMap<String,Object> questionDetails: questionnaireQuestionsMap){
					QuestionnaireQuestionDto questionnaireQuestion = new QuestionnaireQuestionDto();
					if(questionDetails.get("HAS_CONDITION") != null){
						questionnaireQuestion.setHasCondition(questionDetails.get("HAS_CONDITION").toString());
						}
					if(questionDetails.get("LOOKUP_TYPE") != null){
						questionnaireQuestion.setLookUpType(questionDetails.get("LOOKUP_TYPE").toString());
						}
					if(questionDetails.get("NO_OF_ANSWERS") != null){
						questionnaireQuestion.setNumofAnswers(Integer.parseInt((questionDetails.get("NO_OF_ANSWERS").toString())));
						}
					if(questionDetails.get("DESCRIPTION") != null){
						questionnaireQuestion.setDescription(questionDetails.get("DESCRIPTION").toString());
						}
					questionnaireQuestion.setQuestion(questionDetails.get("QUESTION").toString());
					if(questionDetails.get("GROUP_NAME") != null){
						questionnaireQuestion.setGroupName(questionDetails.get("GROUP_NAME").toString());
						if(questionnaireQuestion.getGroupName().equalsIgnoreCase("G0")){
							questionnaireQuestion.setShowQuestion(true);						
						}else{
							questionnaireQuestion.setShowQuestion(false);
						}
					}
					if(questionDetails.get("HELP_LINK") != null){
						questionnaireQuestion.setHelpLink(questionDetails.get("HELP_LINK").toString());
					}
					if(questionDetails.get("LOOKUP_NAME") != null){
						questionnaireQuestion.setLookUpName(questionDetails.get("LOOKUP_NAME").toString());
					}
					if(questionDetails.get("ANSWER_TYPE") != null){
						questionnaireQuestion.setAnswerType(questionDetails.get("ANSWER_TYPE").toString());
					}
					questionnaireQuestion.setQuestionId(Integer.parseInt((questionDetails.get("QUESTION_ID").toString())));

					for(QuestionnaireAnswerDto QuestionnaireAnswerDto : answerList){
					if(questionnaireQuestion.getQuestionId() == QuestionnaireAnswerDto.getQuestionId()){
						questionnaireQuestion.setSelectedAnswer(QuestionnaireAnswerDto.getSelectedAnswer());
						questionnaireQuestion.setAttachmentId(QuestionnaireAnswerDto.getAttachmentId());
						questionnaireQuestion.setQuestionnaireAnswerHeaderId(QuestionnaireAnswerDto.getQuestionnaireAnswerHeaderId());
						questionnaireQuestion.setQuestionnaireAnswerId(QuestionnaireAnswerDto.getQuestionnaireAnswerId());
						questionnaireQuestion.setAcType("U");
						}
					}
					if(questionDetails.get("ANSWER_LENGTH") != null){
						questionnaireQuestion.setAnswerLength(Integer.parseInt((questionDetails.get("ANSWER_LENGTH").toString())));
						}
					if(questionDetails.get("LOOKUP_FIELD") != null){
						questionnaireQuestion.setLookUpFiled(questionDetails.get("LOOKUP_FIELD").toString());
					}
					questionsList.add(questionnaireQuestion);
				
				}
				QuestionnaireDto.setQuestionnaireQuestions(questionsList);
			}	
		
			ArrayList<HashMap<String,Object>> questionnaireConditionsMap = 
					dbEngine.executeProcedure(inParam,"get_mitkc_qnr_qstn_condition",outParam);
			if(questionnaireConditionsMap != null && !questionnaireConditionsMap.isEmpty()){
				QuestionnaireDto.setQuestionnaireConditions(questionnaireConditionsMap);
			}			
			ArrayList<HashMap<String,Object>> questionnaireOptionsMap = 
					dbEngine.executeProcedure(inParam,"get_mitkc_qnr_qstn_option",outParam);
			if(questionnaireOptionsMap != null && !questionnaireOptionsMap.isEmpty()){
				QuestionnaireDto.setQuestionnaireOptions(questionnaireOptionsMap);
			}
		}catch(Exception e){
			logger.error("Error in methord getQuestionnaireDetails",e);
		}
		return QuestionnaireDto;
	}

	@Override
	public void saveQuestionnaireAnswers(QuestionnaireDto questionnaireDto, String questionnaireInfobean,Integer moduleCode, String moduleItemId,PersonDTO personDTO) throws Exception {
		try{
			Integer questionnaireAnsHeaderId = null;
			Integer questionnaireId =  generateQuestionnaireId(moduleCode, moduleItemId);
			JSONObject questionnaireJsnobject = new JSONObject(questionnaireInfobean);
			JSONArray questionnaireJsnArray = questionnaireJsnobject.getJSONArray("answerlist");
			String questAnsHeaderId = questionnaireJsnobject.get("QuestionnaireAnswerHeaderId").toString();
			String questionnaireCompletionFlag = questionnaireJsnobject.get("QuestionnaireCompletionFlag").toString();
			if(!questAnsHeaderId.equals("null")){
			questionnaireAnsHeaderId = Integer.parseInt(questAnsHeaderId);
			}else{
		    	questionnaireAnsHeaderId = generateQuesAnsHeaderId(moduleCode,moduleItemId,personDTO,questionnaireId);
		    }
			updateQuestionnaireAnswerHeader(questionnaireAnsHeaderId,questionnaireCompletionFlag,personDTO);
			for (int i = 0; i < questionnaireJsnArray.length(); i++) {
		    	JSONObject explrObject = questionnaireJsnArray.getJSONObject(i);
				ArrayList<InParameter> inParam = new ArrayList<>();	
				ArrayList<OutParameter> outParam = new ArrayList<>();
				if(!explrObject.get("answerType").equals("Attachment") && !explrObject.get("acType").equals(null)){
						inParam.add(new InParameter("av_module_item_id", DBEngineConstants.TYPE_INTEGER,moduleItemId));
						inParam.add(new InParameter("av_module_item_code", DBEngineConstants.TYPE_INTEGER,moduleCode));
						inParam.add(new InParameter("av_questionnaire_id", DBEngineConstants.TYPE_INTEGER,questionnaireId));
						inParam.add(new InParameter("av_questionnaire_ans_header_id", DBEngineConstants.TYPE_INTEGER,questionnaireAnsHeaderId));
						inParam.add(new InParameter("av_question_id", DBEngineConstants.TYPE_INTEGER,explrObject.get("questionId")));
						if(explrObject.get("answerNumber").equals(null)){
							inParam.add(new InParameter("av_answer_number", DBEngineConstants.TYPE_INTEGER,1)); // have to add logic in future for multiple answers
						}else{
							inParam.add(new InParameter("av_answer_number", DBEngineConstants.TYPE_INTEGER,explrObject.get("answerNumber")));
						}
						if(explrObject.get("selectedAnswer").equals(null)){
							inParam.add(new InParameter("av_answer", DBEngineConstants.TYPE_STRING,null));
						}else{
							inParam.add(new InParameter("av_answer", DBEngineConstants.TYPE_STRING,explrObject.get("selectedAnswer")));
						}						
						if(explrObject.get("lookUpType").equals(null)){
							inParam.add(new InParameter("av_answer_lookup_code", DBEngineConstants.TYPE_STRING,null));
						}else{
							inParam.add(new InParameter("av_answer_lookup_code", DBEngineConstants.TYPE_STRING,explrObject.get("lookUpType")));
						}
						if(explrObject.get("explanation").equals(null)){
							inParam.add(new InParameter("av_explanation", DBEngineConstants.TYPE_STRING,null));
						}else{
							inParam.add(new InParameter("av_explanation", DBEngineConstants.TYPE_STRING,explrObject.get("explanation")));
						}			
						inParam.add(new InParameter("av_update_user", DBEngineConstants.TYPE_STRING,personDTO.getPersonID()));
						inParam.add(new InParameter("av_attachment", DBEngineConstants.TYPE_BLOB,new byte[0]));
						inParam.add(new InParameter("av_file_name", DBEngineConstants.TYPE_STRING,null));
						inParam.add(new InParameter("av_content_type", DBEngineConstants.TYPE_STRING,null));
						if(explrObject.get("answerType").equals("Checkbox") && explrObject.get("acType").equals("U") && explrObject.get("selectedAnswer").equals(null)){
							inParam.add(new InParameter("av_type", DBEngineConstants.TYPE_STRING,"D"));	
						}else{
							inParam.add(new InParameter("av_type", DBEngineConstants.TYPE_STRING,explrObject.get("acType")));
						}
						if(explrObject.get("QuestionnaireAnswerId").equals(null)){
							inParam.add(new InParameter("av_questionnaire_answer_id", DBEngineConstants.TYPE_INTEGER,null));	
						}else{
							inParam.add(new InParameter("av_questionnaire_answer_id", DBEngineConstants.TYPE_INTEGER,explrObject.get("QuestionnaireAnswerId")));
						}
								dbEngine.executeProcedure(inParam,"upd_mitkc_qnr_answer",outParam);

			}else if (explrObject.get("answerType").equals("Attachment") && !explrObject.get("acType").equals(null) && !explrObject.get("acType").equals("U") ) {
				inParam.add(new InParameter("av_module_item_id", DBEngineConstants.TYPE_INTEGER,moduleItemId));
				inParam.add(new InParameter("av_module_item_code", DBEngineConstants.TYPE_INTEGER,moduleCode));
				inParam.add(new InParameter("av_questionnaire_id", DBEngineConstants.TYPE_INTEGER,questionnaireId));
				inParam.add(new InParameter("av_questionnaire_ans_header_id", DBEngineConstants.TYPE_INTEGER,questionnaireAnsHeaderId));
				inParam.add(new InParameter("av_question_id", DBEngineConstants.TYPE_INTEGER,explrObject.get("questionId")));
				if(explrObject.get("answerNumber").equals(null)){
					inParam.add(new InParameter("av_answer_number", DBEngineConstants.TYPE_INTEGER,1)); // have to add logic in future for multiple answers
				}else{
					inParam.add(new InParameter("av_answer_number", DBEngineConstants.TYPE_INTEGER,explrObject.get("answerNumber")));
				}
				if(explrObject.get("selectedAnswer").equals(null)){
					inParam.add(new InParameter("av_answer", DBEngineConstants.TYPE_STRING,null));
				}else{
					inParam.add(new InParameter("av_answer", DBEngineConstants.TYPE_STRING,explrObject.get("selectedAnswer")));
				}
				
				if(explrObject.get("lookUpType").equals(null)){
					inParam.add(new InParameter("av_answer_lookup_code", DBEngineConstants.TYPE_STRING,null));
				}else{
					inParam.add(new InParameter("av_answer_lookup_code", DBEngineConstants.TYPE_STRING,explrObject.get("lookUpType")));
				}
				if(explrObject.get("explanation").equals(null)){
					inParam.add(new InParameter("av_explanation", DBEngineConstants.TYPE_STRING,null));
				}else{
					inParam.add(new InParameter("av_explanation", DBEngineConstants.TYPE_STRING,explrObject.get("explanation")));
				}			
				inParam.add(new InParameter("av_update_user", DBEngineConstants.TYPE_STRING,personDTO.getPersonID()));
				if(!questionnaireDto.getQuesAttachmentList().isEmpty()){
				for(QuestionnaireAttachmentDto quesAttachments : questionnaireDto.getQuesAttachmentList()){
					if(quesAttachments.getQuestionId().toString().equalsIgnoreCase(explrObject.get("questionId").toString())){
						inParam.add(new InParameter("av_attachment", DBEngineConstants.TYPE_BLOB,quesAttachments.getAttachment()));
						inParam.add(new InParameter("av_file_name", DBEngineConstants.TYPE_STRING,quesAttachments.getFileName()));
						inParam.add(new InParameter("av_content_type", DBEngineConstants.TYPE_STRING,quesAttachments.getContentType()));
						inParam.add(new InParameter("av_type", DBEngineConstants.TYPE_STRING,quesAttachments.getAcType()));
						break;
						}
					}
				}if(explrObject.get("acType").equals("D")){
						inParam.add(new InParameter("av_attachment", DBEngineConstants.TYPE_BLOB,new byte[0]));
						inParam.add(new InParameter("av_file_name", DBEngineConstants.TYPE_STRING,null));
						inParam.add(new InParameter("av_content_type", DBEngineConstants.TYPE_STRING,null));
						inParam.add(new InParameter("av_type", DBEngineConstants.TYPE_STRING,explrObject.get("acType")));
					}
				if(explrObject.get("QuestionnaireAnswerId").equals(null)){
					inParam.add(new InParameter("av_questionnaire_answer_id", DBEngineConstants.TYPE_INTEGER,null));	
				}else{
					inParam.add(new InParameter("av_questionnaire_answer_id", DBEngineConstants.TYPE_INTEGER,explrObject.get("QuestionnaireAnswerId")));
				}
						dbEngine.executeProcedure(inParam,"upd_mitkc_qnr_answer",outParam);
			}	    
		    }
		}catch(Exception e){
			logger.error("Error in methord saveQuestionnaireAnswers",e);
		}
	}

	private Integer generateQuesAnsHeaderId(Integer moduleCode, String moduleItemId,PersonDTO personDTO,Integer questionnaireId) {
		Integer questionnaireAnsHeaderId = null;
		try{
			ArrayList<InParameter> inParam = new ArrayList<>();	
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("av_module_item_id", DBEngineConstants.TYPE_INTEGER,moduleItemId));
			inParam.add(new InParameter("av_module_item_code", DBEngineConstants.TYPE_INTEGER,moduleCode));
			inParam.add(new InParameter("av_questionnaire_id", DBEngineConstants.TYPE_INTEGER,questionnaireId));			
			inParam.add(new InParameter("av_update_user", DBEngineConstants.TYPE_STRING,personDTO.getPersonID()));
			outParam.add(new OutParameter("questionnaireAnsHeaderId",DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String,Object>> result = 
					dbEngine.executeFunction(inParam,"fn_upd_mitkc_qnr_ans_header",outParam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				questionnaireAnsHeaderId = Integer.parseInt((String)hmResult.get("questionnaireAnsHeaderId"));
			}		
		}catch(Exception e){
			logger.error("Error in methord generateQuesAnsHeaderId",e);
		}
		return questionnaireAnsHeaderId;
	}
	
	private Integer updateQuestionnaireAnswerHeader(Integer questionnaireAnsHeaderId, String completionFlag,PersonDTO personDTO) {		
		try{
			ArrayList<InParameter> inParam = new ArrayList<>();	
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("av_qnr_ans_header_id", DBEngineConstants.TYPE_INTEGER,questionnaireAnsHeaderId));
			inParam.add(new InParameter("av_qnr_completed_flag", DBEngineConstants.TYPE_STRING,completionFlag));
			inParam.add(new InParameter("av_update_user", DBEngineConstants.TYPE_STRING,personDTO.getPersonID()));
			outParam.add(new OutParameter("questionnaireAnsHeaderId",DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String,Object>> result = 
					dbEngine.executeFunction(inParam,"fn_upd_mitkc_qnr_header",outParam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				questionnaireAnsHeaderId = Integer.parseInt((String)hmResult.get("questionnaireAnsHeaderId"));
			}		
		}catch(Exception e){
			logger.error("Error in methord generateQuesAnsHeaderId",e);
		}
		return questionnaireAnsHeaderId;
	}
	private Integer generateQuestionnaireId(Integer moduleCode, String moduleItemId) {
		Integer questionnaireId = null;
		try{
			ArrayList<OutParameter> outParam = new ArrayList<>();
			outParam.add(new OutParameter("questionnaireId",DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String,Object>> result = 
					dbEngine.executeFunction("fn_mitkc_irb_questionaire_id",outParam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				questionnaireId = Integer.parseInt((String)hmResult.get("questionnaireId"));
			}		
		}catch(Exception e){
			logger.error("Error in methord generateQuestionnaireId",e);
		}
		return questionnaireId;
	}
	
	@Override
	public List<String> parseSelectedAnswer(String jsonData) throws Exception {
		List<String> selectedAnswer = new ArrayList<String>();
		try{
		 JSONObject object = new JSONObject(jsonData);
		 selectedAnswer =   parse(object,selectedAnswer);
		}catch(Exception e){
			logger.error("Error in methord parseSelectedAnswer",e);
		}
		return selectedAnswer;
	}
	
	private  List<String> parse(JSONObject json , List<String> out) throws Exception{
	    Iterator<String> keys = json.keys();
	    while(keys.hasNext()){
	        String key = keys.next();
	        String  value = null;
	        try{
	            value  = json.get(key).toString();
	        }catch(Exception e){
	        }

	        if(value != null){	        	
	            out.add(value);
	        }
	    }
	    return out;
	}

	@Override
	public boolean isQuestionnaireComplete(Integer moduleCode, String moduleItemKey) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isQuestionnaireComplete(Integer questionnaireAnswerHeaderId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}

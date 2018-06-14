create or replace function FN_UPD_MITKC_QNR_HEADER(
 av_qnr_ans_header_id   IN mitkc_questionnaire_ans_header.questionnaire_ans_header_id%TYPE,
 av_qnr_completed_flag   IN mitkc_questionnaire_ans_header.questionnaire_completed_flag%TYPE,
 av_update_user        IN mitkc_questionnaire_ans_header.update_user%TYPE) 
 
return number is

begin
	 
		UPDATE mitkc_questionnaire_ans_header
		SET questionnaire_completed_flag = av_qnr_completed_flag
		WHERE questionnaire_ans_header_id = av_qnr_ans_header_id;  


   return av_qnr_ans_header_id;

end;
/
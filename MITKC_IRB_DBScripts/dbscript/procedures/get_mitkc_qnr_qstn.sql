create or replace procedure get_mitkc_qnr_qstn(
av_questionnaire_id IN mitkc_questionnaire_question.questionnaire_id%TYPE,
cur_generic         OUT SYS_REFCURSOR)
is

begin
    OPEN cur_generic FOR
    SELECT question_id, 
       question, 
       description, 
       help_link, 
       answer_type, 
       answer_length, 
       no_of_answers, 
       lookup_type, 
       lookup_name, 
       lookup_field, 
       group_name, 
       group_label,
       has_condition 
       FROM   mitkc_questionnaire_question 
       WHERE  questionnaire_id = av_questionnaire_id 
       ORDER  BY sort_order; 

end;
/
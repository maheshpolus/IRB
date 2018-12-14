CREATE OR REPLACE PROCEDURE get_mitkc_qnr_qstn_option(
av_questionnaire_id IN mitkc_questionnaire_question.questionnaire_id%TYPE,
cur_generic         OUT SYS_REFCURSOR)
is

begin
    OPEN cur_generic FOR
    SELECT t1.question_option_id, 
       t1.question_id, 
       t1.option_number, 
       t1.option_label, 
       t1.require_explanation, 
       t1.explantion_label 
    FROM   mitkc_qnr_question_option t1 
    INNER  JOIN mitkc_questionnaire_question t2 ON t1.question_id = t2.question_id 
    WHERE  t2.questionnaire_id = av_questionnaire_id
    ORDER  BY t1.question_option_id,t1.question_id,t1.option_number; 

end;
/

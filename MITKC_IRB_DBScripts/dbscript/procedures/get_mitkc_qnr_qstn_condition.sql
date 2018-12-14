create or replace procedure get_mitkc_qnr_qstn_condition(
av_questionnaire_id IN mitkc_questionnaire_question.questionnaire_id%TYPE,
cur_generic         OUT SYS_REFCURSOR)
is

begin
    OPEN cur_generic FOR
       SELECT t1.question_condition_id, 
              t1.question_id, 
              t1.condition_type, 
              t1.condition_value, 
              t1.group_name 
        FROM  mitkc_qnr_question_condition t1 
        INNER JOIN mitkc_questionnaire_question t2 ON t1.question_id = t2.question_id 
        WHERE t2.questionnaire_id = av_questionnaire_id
        ORDER BY to_number(substr(t1.group_name,2));

end;
/
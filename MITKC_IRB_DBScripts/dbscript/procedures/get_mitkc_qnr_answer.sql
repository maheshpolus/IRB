create or replace procedure GET_MITKC_QNR_ANSWER(
av_qnr_ans_header_id IN mitkc_questionnaire_ans_header.questionnaire_ans_header_id%TYPE,
cur_generic       OUT SYS_REFCURSOR)
is

begin
    OPEN cur_generic FOR
        SELECT t1.questionnaire_answer_id, 
               t1.question_id, 
               t1.answer_number, 
               t1.answer, 
               t1.answer_lookup_code, 
               t1.explanation ,
               T3.Questionnaire_Answer_Att_Id AS Attachment_id,
               T2.Questionnaire_Ans_Header_Id
        FROM  mitkc_questionnaire_answer t1 
        INNER JOIN mitkc_questionnaire_ans_header t2 ON t1.questionnaire_ans_header_id = t2.questionnaire_ans_header_id
        Left Outer JOIN mitkc_questionnaire_answer_att t3 ON T3.Questionnaire_Answer_Id=T1.Questionnaire_Answer_Id
        WHERE t2.questionnaire_ans_header_id = av_qnr_ans_header_id
        ORDER BY t1.questionnaire_answer_id,t1.question_id; 
 
end;
/
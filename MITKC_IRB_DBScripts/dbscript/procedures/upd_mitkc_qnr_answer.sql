CREATE OR REPLACE PROCEDURE upd_mitkc_qnr_answer (
 av_module_item_id     IN mitkc_questionnaire_ans_header.module_item_id%TYPE,
 av_module_item_code   IN mitkc_questionnaire_ans_header.module_item_code%TYPE,
 av_questionnaire_id   IN mitkc_questionnaire_ans_header.questionnaire_id%TYPE,
 av_questionnaire_ans_header_id IN mitkc_questionnaire_ans_header.questionnaire_ans_header_id%TYPE,
 av_question_id        IN mitkc_questionnaire_answer.question_id%TYPE,
 av_answer_number      IN mitkc_questionnaire_answer.answer_number%TYPE,
 av_answer             IN mitkc_questionnaire_answer.answer%TYPE,
 av_answer_lookup_code IN mitkc_questionnaire_answer.answer_lookup_code%TYPE,
 av_explanation        IN mitkc_questionnaire_answer.explanation%TYPE,
 av_update_user        IN mitkc_questionnaire_answer.update_user%TYPE,
 av_attachment         IN MITKC_QUESTIONNAIRE_ANSWER_ATT.attachment%TYPE,
 av_file_name          IN Mitkc_Questionnaire_Answer_Att.File_Name%TYPE,
 av_content_type       IN Mitkc_Questionnaire_Answer_Att.Content_Type%Type,
 av_type IN varchar2,
 av_questionnaire_answer_id IN Mitkc_Questionnaire_Answer_Att.QUESTIONNAIRE_ANSWER_ID%Type
 )

is
     li_questionnaire_ans_header_id number(12);
     li_questionnaire_answer_id number(12);
     li_flag number(2);
begin
   --  li_questionnaire_ans_header_id:=fn_upd_mitkc_qnr_ans_header(av_module_item_id,av_module_item_code,av_questionnaire_id,av_update_user);

 if av_type='I' then
    select seq_questionnaire_answer_id.nextval into li_questionnaire_answer_id from dual;

    INSERT INTO mitkc_questionnaire_answer 
            (questionnaire_answer_id, 
             questionnaire_ans_header_id, 
             question_id, 
             answer_number, 
             answer, 
             answer_lookup_code, 
             explanation, 
             update_timestamp, 
             update_user) 
    VALUES   (li_questionnaire_answer_id, 
             av_questionnaire_ans_header_id, 
             av_question_id, 
             av_answer_number, 
             av_answer, 
             av_answer_lookup_code, 
             av_explanation, 
             sysdate, 
             av_update_user); 

    if av_attachment is not null then
    
    begin
          SELECT Count(1) 
          INTO   li_flag 
          FROM   mitkc_questionnaire_answer_att 
          WHERE  questionnaire_answer_id=av_questionnaire_answer_id;
          
         IF li_flag>0 then 
         
            DELETE FROM   mitkc_questionnaire_answer_att 
            WHERE  questionnaire_answer_id=av_questionnaire_answer_id;
            
            delete from mitkc_questionnaire_answer 
            where questionnaire_answer_id=av_questionnaire_answer_id;
            
         END IF;
    exception 
    when others then
        null;
    end;    
       INSERT INTO mitkc_questionnaire_answer_att 
            (questionnaire_answer_att_id, 
             questionnaire_answer_id, 
             attachment, 
             file_name, 
             content_type, 
             update_timestamp, 
             update_user) 
       VALUES(seq_qnr_answer_att_id.nextval, 
            li_questionnaire_answer_id, 
            av_attachment, 
            av_file_name, 
            av_content_type, 
            sysdate, 
            av_update_user );
    end if;
   
end if;
if av_type='D' then

    
    delete from mitkc_questionnaire_answer_att where questionnaire_answer_id=av_questionnaire_answer_id;
    
    delete from mitkc_questionnaire_answer where questionnaire_answer_id=av_questionnaire_answer_id;

end if;
if av_type='U' then

    
   UPDATE  mitkc_questionnaire_answer 
    SET    questionnaire_ans_header_id = av_questionnaire_ans_header_id, 
           question_id = av_question_id, 
           answer_number = av_answer_number, 
           answer = av_answer, 
           answer_lookup_code = av_answer_lookup_code, 
           explanation = av_explanation, 
           update_timestamp = sysdate, 
           update_user = av_update_user 
    WHERE  questionnaire_answer_id = av_questionnaire_answer_id; 
   
end if;
end;
/
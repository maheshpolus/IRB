create or replace function fn_upd_mitkc_qnr_ans_header(
 av_module_item_id     IN mitkc_questionnaire_ans_header.module_item_id%TYPE,
 av_module_item_code   IN mitkc_questionnaire_ans_header.module_item_code%TYPE,
 av_questionnaire_id   IN mitkc_questionnaire_ans_header.questionnaire_id%TYPE,
 av_update_user        IN mitkc_questionnaire_ans_header.update_user%TYPE)

return number is

 li_flag number(2);
 li_questionnaire_ans_header_id number(12);
begin

    SELECT Count(1)
           INTO   li_flag
    FROM   mitkc_questionnaire_ans_header
    WHERE  module_item_id = av_module_item_id
           AND module_item_code = av_module_item_code
           AND questionnaire_id = av_questionnaire_id;

    if li_flag=0 then

        select seq_qnr_ans_header_id.nextval
        into li_questionnaire_ans_header_id from dual;

        INSERT INTO mitkc_questionnaire_ans_header
            (questionnaire_ans_header_id,
             questionnaire_id,
             module_item_code,
             module_item_id,
             questionnaire_completed_flag,
             update_timestamp,
             update_user)
      VALUES(li_questionnaire_ans_header_id,
             av_questionnaire_id,
             av_module_item_code,
             av_module_item_id,
             'Y',
             sysdate,
             av_update_user);
    else
        SELECT questionnaire_ans_header_id
               INTO  li_questionnaire_ans_header_id
        FROM   mitkc_questionnaire_ans_header
        WHERE  module_item_id = av_module_item_id
               AND module_item_code = av_module_item_code
               AND questionnaire_id = av_questionnaire_id;
    end if;

   return li_questionnaire_ans_header_id;

end;
/
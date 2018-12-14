create or replace function FN_MITKC_IRB_QUESTIONAIRE_ID
return varchar2 

is
ls_qnr_id varchar2(10);
begin

      ls_qnr_id := 1003;
     return ls_qnr_id;
end;
/
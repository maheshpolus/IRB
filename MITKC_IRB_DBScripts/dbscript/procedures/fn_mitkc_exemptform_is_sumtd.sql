create or replace FUNCTION fn_mitkc_exemptform_is_sumtd(
AV_PERSON_EXEMPT_FORM_ID  IRB_EXEMPT_FORM_ACTN_LOG.IRB_PERSON_EXEMPT_FORM_ID%type
)return NUMBER
IS
li_count PLS_INTEGER;
BEGIN
   select count(IRB_PERSON_EXEMPT_FORM_ID) 
   into li_count
   from IRB_EXEMPT_FORM_ACTN_LOG
   where IRB_PERSON_EXEMPT_FORM_ID = AV_PERSON_EXEMPT_FORM_ID
   AND ACTION_TYPE_CODE <> 1; --Created
   
   if li_count > 0 then
    return 1;
   end if;
   
   return 0; 

END;
/
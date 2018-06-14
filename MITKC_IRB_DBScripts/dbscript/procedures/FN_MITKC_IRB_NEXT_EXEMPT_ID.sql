create or replace function FN_MITKC_IRB_NEXT_EXEMPT_ID
return number is
li_irb_person_exempt_form_id NUMBER;
begin

	li_irb_person_exempt_form_id :=  SEQ_IRB_PERSON_EXEMPT_FORM_ID.NEXTVAL;
	
	return li_irb_person_exempt_form_id;

end;
/
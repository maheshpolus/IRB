create or replace FUNCTION FN_MITKC_GET_PER_JOB_TITLE(
AV_PERSON_ID  PERSON_APPOINTMENT.PERSON_ID%type
)return VARCHAR2
IS
ls_job_title  PERSON_APPOINTMENT.job_title%type;
BEGIN
    select t1.job_title 
    into ls_job_title
    from PERSON_APPOINTMENT t1
    where t1.person_id = AV_PERSON_ID
    and t1.APPOINTMENT_ID in (select max(t2.APPOINTMENT_ID) from PERSON_APPOINTMENT t2 
                              where t1.person_id = t2.person_id);
   return ls_job_title;                           
EXCEPTION
WHEN NO_DATA_FOUND THEN
RETURN NULL;
END;
/
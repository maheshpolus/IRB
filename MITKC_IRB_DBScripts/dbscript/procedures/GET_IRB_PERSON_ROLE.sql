create or replace PROCEDURE GET_IRB_PERSON_ROLE(
AV_PERSON_ID IN  KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_USER_ID   IN  KRIM_PRNCPL_T.PRNCPL_NM%TYPE,
CUR_GENERIC OUT SYS_REFCURSOR)
IS
  ls_irb_admin   varchar2(1) := 'N';
  ls_irb_pi      varchar2(1) := 'Y';
  ls_irb_review  varchar2(1) := 'N';  
  ls_irb_chair   varchar2(1) := 'N';
  li_count       pls_integer;
BEGIN
  
  select count(mbr_id)
  into li_count
  from krim_role_t t1
  inner join krim_role_mbr_t t2 on t1.role_id = t2.role_id
  where t2.mbr_id in (select prncpl_id from krim_prncpl_t where prncpl_nm = AV_USER_ID)
  and t1.role_nm =  'IRB Administrator';
  
  if li_count > 0 then
    ls_irb_admin  := 'Y';
    ls_irb_review := 'Y';
    ls_irb_chair  := 'Y';
  end if;
  

    OPEN CUR_GENERIC FOR
    SELECT 
        ls_irb_admin AS IS_IRB_ADMIN,
        ls_irb_pi AS IS_IRB_PI,
        ls_irb_review AS IS_IRB_REVIEWER,
        ls_irb_chair AS IS_IRB_CHAIR
    FROM DUAL;
END;
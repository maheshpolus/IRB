create or replace PROCEDURE get_ab_person_details(
av_prncpl_nm krim_prncpl_t.prncpl_nm%TYPE,
cur_generic  OUT SYS_REFCURSOR)
IS
BEGIN
  OPEN cur_generic FOR
  SELECT 
  kpt.prncpl_id, 
  kent.first_nm,
  kent.middle_nm,
  kent.last_nm,
  decode(kent.middle_nm,NULL,kent.last_nm||', '||kent.first_nm,kent.last_nm||', '||kent.first_nm||' '||kent.middle_nm) full_name,
  keet.email_addr,
  keit.prmry_dept_cd,
  un.unit_name,
  ph.phone_nbr,
  addr.addr_line_1,
  FN_MITKC_GET_PER_JOB_TITLE(kpt.prncpl_id) as job_title 
  FROM krim_prncpl_t kpt 
  LEFT OUTER JOIN krim_entity_nm_t kent ON kpt.entity_id = kent.entity_id
  LEFT OUTER JOIN krim_entity_email_t keet ON kpt.entity_id = keet.entity_id
  LEFT OUTER JOIN krim_entity_emp_info_t keit ON kpt.entity_id = keit.entity_id
  LEFT OUTER JOIN unit un ON keit.prmry_dept_cd = un.unit_number
  LEFT OUTER JOIN krim_entity_phone_t ph ON ph.entity_id = kpt.entity_id and ph.dflt_ind = 'Y'
  LEFT OUTER JOIN krim_entity_addr_t addr ON addr.entity_id = kpt.entity_id and addr.actv_ind = 'Y'                      
  WHERE kpt.prncpl_nm = av_prncpl_nm;
END;
/
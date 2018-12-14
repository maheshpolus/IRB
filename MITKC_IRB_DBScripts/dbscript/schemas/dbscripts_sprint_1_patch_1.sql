ALTER TABLE IRB_PROTOCOL ADD LEAD_UNIT_NUMBER VARCHAR2(8)
/
declare

 cursor c_data is
  SELECT PROTOCOL_NUMBER, UNIT_NUMBER,SEQUENCE_NUMBER
  FROM IRB_PROTOCOL_UNITS t1
  WHERE  t1.LEAD_UNIT_FLAG = 'Y'
  and t1.protocol_units_id  = ( select max(t2.protocol_units_id)
                                FROM IRB_PROTOCOL_UNITS t2
                                WHERE t1.PROTOCOL_NUMBER = t2.PROTOCOL_NUMBER 
                                and t1.SEQUENCE_NUMBER = t2.SEQUENCE_NUMBER
                                and t2.LEAD_UNIT_FLAG = 'Y'                                                            
                                );
  r_data c_data%rowtype;
begin
  open c_data;
  loop
  fetch c_data into r_data;
  exit when c_data%notfound;
  
    UPDATE IRB_PROTOCOL t1
    SET t1.LEAD_UNIT_NUMBER = r_data.UNIT_NUMBER
    WHERE t1.PROTOCOL_NUMBER = r_data.PROTOCOL_NUMBER 
    and t1.SEQUENCE_NUMBER = r_data.SEQUENCE_NUMBER;
  
  end loop;
  close c_data;
end;
/
commit
/
insert into krim_role_t(ROLE_ID,OBJ_ID,VER_NBR,ROLE_NM,NMSPC_CD,DESC_TXT,KIM_TYP_ID,ACTV_IND,LAST_UPDT_DT) 
values (KRIM_ROLE_ID_S.nextval,sys_guid(),1,'Department IRB Administrator','KC-ADM','Department IRB Administrator',69,'Y',sysdate)
/
insert into KRIM_perm_t (PERM_ID,OBJ_ID,VER_NBR,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND)
values(KRIM_PERM_ID_S.nextval,sys_guid(),1,1,'KC-SYS','IRB_Dept_Administrator','Department IRB Administrator','Y')
/
insert into KRIM_ROLE_PERM_T(ROLE_PERM_ID,OBJ_ID,VER_NBR,ROLE_ID,PERM_ID,ACTV_IND)
values(KRIM_ROLE_PERM_ID_S.nextval,sys_guid(),1,(select ROLE_ID from krim_role_t where role_nm = 'Department IRB Administrator')
,(select PERM_ID from KRIM_perm_t where NM = 'IRB_Dept_Administrator'),'Y')
/
commit
/
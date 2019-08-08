ALTER TABLE IRB_PROTOCOL ADD LEAD_UNIT_NUMBER VARCHAR2(8)
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
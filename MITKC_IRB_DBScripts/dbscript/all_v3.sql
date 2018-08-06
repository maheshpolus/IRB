Spool log_all.log;
set heading off;
set define off;
set term on;
set serveroutput on;
select '*-*-*-*-*-*-*- Started building MITKC IRB Schema V2.0 '|| localtimestamp from dual;
-- @schemas/dbscript_mitkc_irb_20180718.sql
select '*-*-*-*-*-*-*- Started compiling MITKC IRB Procedures V2.0 '|| localtimestamp from dual;
@procedures/FN_MITKC_GET_PER_JOB_TITLE.sql;
@procedures/GET_IRB_EXEMPT_FORM_LIST.sql;
@procedures/GET_IRB_EXEMPT_FORM_ACTION_LOG.sql;
@procedures/UPD_IRB_PERSON_EXEMPT_FORM.sql;
@procedures/GET_IRB_PERSON_EXEMPT_PER_FORM.sql;
@procedures/GET_IRB_PERSON_EXEMPT_FORM.sql;
@procedures/get_ab_person_details.sql;
@procedures/GET_IRB_PROTOCOL_HISTORY_DET.sql;
@procedures/GET_IRB_EXEMPT_FORM_CKLST_FILE.sql;
@procedures/UPD_IRB_EXEMPT_FORM_CHECKLST.sql;
@procedures/GET_IRB_EXEMPT_FORM_CHECKLST.sql;
@procedures/FN_IRB_EXEMP_FORM_ACTION_LOG.sql;
@procedures/GET_IRB_PROTOCOL_DETAILS.sql;
@procedures/GET_IRB_NOT_EXEMPT_QSTN_LIST.sql;
@procedures/pkg_mitkc_mail_generic.sql
commit
/
select '*-*-*-*-*-*-*- Completed building MITKC IRB  V2.0'|| localtimestamp from dual;
Spool Off;
Set define On;
Set feedback On;
EXIT
/
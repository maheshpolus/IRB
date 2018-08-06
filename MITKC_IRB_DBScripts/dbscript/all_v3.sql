Spool log_all.log;
set heading off;
set define off;
set term on;
set serveroutput on;
select '*-*-*-*-*-*-*- Started building MITKC IRB Schema V2.0 '|| localtimestamp from dual;
-- @schemas/dbscript_mitkc_irb_20180718.sql
select '*-*-*-*-*-*-*- Started compiling MITKC IRB Procedures V2.0 '|| localtimestamp from dual;
@procedure/FN_MITKC_GET_PER_JOB_TITLE.sql;
@procedure/GET_IRB_EXEMPT_FORM_LIST.sql;
@procedure/GET_IRB_EXEMPT_FORM_ACTION_LOG.sql;
@procedure/UPD_IRB_PERSON_EXEMPT_FORM.sql;
@procedure/GET_IRB_PERSON_EXEMPT_PER_FORM.sql;
@procedure/GET_IRB_PERSON_EXEMPT_FORM.sql;
@procedure/get_ab_person_details.sql;
@procedure/GET_IRB_PROTOCOL_HISTORY_DET.sql;
@procedure/GET_IRB_EXEMPT_FORM_CKLST_FILE.sql;
@procedure/UPD_IRB_EXEMPT_FORM_CHECKLST.sql;
@procedure/GET_IRB_EXEMPT_FORM_CHECKLST.sql;
@procedure/FN_IRB_EXEMP_FORM_ACTION_LOG.sql;
@procedure/GET_IRB_PROTOCOL_DETAILS.sql;
@procedure/GET_IRB_NOT_EXEMPT_QSTN_LIST.sql;
@procedure/pkg_mitkc_mail_generic.sql
commit
/
select '*-*-*-*-*-*-*- Completed building MITKC IRB  V2.0'|| localtimestamp from dual;
Spool Off;
Set define On;
Set feedback On;
EXIT
/
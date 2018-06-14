Spool log_all.log;
set heading off;
set define off;
set term on;
set serveroutput on;
select '*-*-*-*-*-*-*- Started building MITKC IRB Schema V2.0 '|| localtimestamp from dual;
@schemas/dbscript_mitkc_irb_20180608.sql
@schemas/dbscript_mitkc_irb_20180614.sql
select '*-*-*-*-*-*-*- Started compiling MITKC IRB Procedures V2.0 '|| localtimestamp from dual;
@procedures/FN_MITKC_IRB_NEXT_EXEMPT_ID.sql;
@procedures/FN_MITKC_IRB_QUESTIONAIRE_ID.sql;
@procedures/FN_UPD_MITKC_QNR_HEADER.sql;
@procedures/GET_IRB_PERSON_EXEMPT_FORM.sql;
@procedures/GET_IRB_PERSON_EXEMPT_MESSAGE.sql;
@procedures/GET_IRB_PERSON_EXEMPT_PER_FORM.sql;
@procedures/GET_IRB_PROTOCOL_HISTORY_DET.sql;
@procedures/GET_MITKC_QNR_ANSWER.sql;
@procedures/UPD_IRB_PERSON_EXEMPT_FORM.sql;
select '*-*-*-*-*-*-*- Started migrating data to MITKC IRB Procedures V2.0 '|| localtimestamp from dual;
@migration/migration.sql

commit
/
select '*-*-*-*-*-*-*- Completed building MITKC IRB  V2.0'|| localtimestamp from dual;
Spool Off;
Set define On;
Set feedback On;
EXIT
/
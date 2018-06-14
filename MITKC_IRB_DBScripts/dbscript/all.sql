Spool log_all.log;
set heading off;
set define off;
set term on;
set serveroutput on;
select '*-*-*-*-*-*-*- Started building MITKC IRB Schema V1.0 '|| localtimestamp from dual;
@schemas/dbscript_mitkc_irb_20180507.sql
select '*-*-*-*-*-*-*- Started compiling MITKC IRB Procedures V1.0 '|| localtimestamp from dual;
@procedures/GET_IRB_DASHBOARD.sql
@procedures/GET_IRB_PERSON_ROLE.sql
@procedures/GET_IRB_PROTOCOL_DETAILS.sql
@procedures/GET_IRB_PROTOCOL_FUNDING_SRC.sql
@procedures/GET_IRB_PROTOCOL_LOCATION.sql
@procedures/GET_IRB_PROTOCOL_PERSONS.sql
@procedures/GET_IRB_PROTOCOL_SPECIAL_REVW.sql
@procedures/GET_IRB_PROTOCOL_VULNBLE_SUBJT.sql
@procedures/GET_IRB_SUMMARY.sql
@procedures/GET_MITKC_PERSON_INFO.sql
@procedures/GET_MITKC_PERSON_TRAINING_INFO.sql
select '*-*-*-*-*-*-*- Started migrating data to MITKC IRB Procedures V1.0 '|| localtimestamp from dual;
@migration/migration.sql

commit
/
select '*-*-*-*-*-*-*- Completed building MITKC IRB  V1.0'|| localtimestamp from dual;
Spool Off;
Set define On;
Set feedback On;
EXIT
/
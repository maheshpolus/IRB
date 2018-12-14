Spool log_all.log;
set heading off;
set define off;
set term on;
set serveroutput on;
select '*-*-*-*-*-*-*- Started building irb database objects - Phase 1'|| localtimestamp from dual;
@schemas/dbscripts_sprint_1.sql
@schemas/dbscripts_questionnaire.sql
@schemas/dbscripts_sprint_1_patch_1.sql
@procedures/fn_irb_exemp_form_action_log.sql
@procedures/fn_mitkc_exemptform_is_sumtd.sql
@procedures/fn_mitkc_get_per_job_title.sql
@procedures/fn_mitkc_irb_next_exempt_id.sql
@procedures/fn_mitkc_irb_questionaire_id.sql
@procedures/fn_upd_mitkc_qnr_header.sql
@procedures/get_ab_person_details.sql
@procedures/get_irb_dashboard.sql
@procedures/get_irb_exempt_form_action_log.sql
@procedures/get_irb_exempt_form_checklst.sql
@procedures/get_irb_exempt_form_cklst_file.sql
@procedures/get_irb_exempt_form_list.sql
@procedures/get_irb_not_exempt_qstn_list.sql
@procedures/get_irb_person_exempt_form.sql
@procedures/get_irb_person_exempt_message.sql
@procedures/get_irb_person_exempt_per_form.sql
@procedures/get_irb_person_role.sql
@procedures/get_irb_protocol_attachment.sql
@procedures/get_irb_protocol_details.sql
@procedures/get_irb_protocol_funding_src.sql
@procedures/get_irb_protocol_history_det.sql
@procedures/get_irb_protocol_history_group.sql
@procedures/get_irb_protocol_location.sql
@procedures/get_irb_protocol_persons.sql
@procedures/get_irb_protocol_special_revw.sql
@procedures/get_irb_protocol_type.sql
@procedures/get_irb_protocol_vulnble_subjt.sql
@procedures/get_irb_summary.sql
@procedures/get_irb_summary_list.sql
@procedures/get_mitkc_attachment_file.sql
@procedures/get_mitkc_person_info.sql
@procedures/get_mitkc_person_training_info.sql
@procedures/get_mitkc_qnr_answer.sql
@procedures/get_mitkc_qnr_qstn.sql
@procedures/get_mitkc_qnr_qstn_condition.sql
@procedures/pkg_mitkc_mail_generic.sql
@procedures/upd_irb_exempt_form_checklst.sql
@procedures/upd_irb_person_exempt_form.sql
@procedures/fn_upd_mitkc_qnr_ans_header.sql
@procedures/get_mitkc_qnr_qstn_option.sql
@procedures/upd_mitkc_qnr_answer.sql
@procedures/get_mitkc_all_units.sql
@procedures/get_irb_protocol_actn_comments.sql
@procedures/get_irb_proto_corresp_letter.sql
@procedures/fn_irb_pers_has_right_in_proto.sql
@migration/migration.sql
@schemas/materialized_view.sql
commit
/
select '*-*-*-*-*-*-*- Completed building irb database objects - Phase 1'|| localtimestamp from dual;
Spool Off;
Set define On;
Set feedback On;
EXIT
/
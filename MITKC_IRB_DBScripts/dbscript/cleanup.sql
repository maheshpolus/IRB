DROP TABLE IRB_COMM_CORRESP_BATCH_DETAIL
/
DROP TABLE IRB_COMM_CORRESP_BATCH
/
DROP TABLE IRB_COMM_SCHEDULE_MINUTE_DOC
/
DROP TABLE IRB_COMM_SCHEDULE_MINUTES
/
DROP TABLE IRB_MINUTE_ENTRY_TYPE
/
DROP TABLE IRB_COMM_MEMBER_EXPERTISE
/
DROP TABLE IRB_COMM_MEMBER_ROLES
/
DROP TABLE IRB_COMM_MEMBER_STATUS_CHANGE
/
DROP TABLE IRB_COMM_MEMBERSHIPS
/
DROP TABLE IRB_COMM_MEMBERSHIP_STATUS
/
DROP TABLE IRB_COMM_MEMBERSHIP_TYPE
/
DROP TABLE IRB_COMM_SCHEDULE_FREQUENCY
/
DROP TABLE IRB_COMM_SCHEDULE_ATTACHMENTS
/
DROP TABLE IRB_SCHEDULE_AGENDA
/
DROP TABLE IRB_COMM_SCHEDULE_ATTENDANCE
/
DROP TABLE IRB_COMM_SCHEDULE_ACT_ITEMS
/
DROP TABLE IRB_COMM_SCHEDULE_ATTACH_TYPE
/
DROP TABLE IRB_SCHEDULE_ACT_ITEM_TYPE
/
DROP TABLE IRB_PROTOCOL_SCIENTIFIC_DATA
/
DROP TABLE IRB_PROTO_VULNERABLE_SUB
/
DROP TABLE VULNERABLE_SUBJECT_AGE_GROUP
/
DROP TABLE IRB_VULNERABLE_SUBJECT_TYPE
/
DROP TABLE IRB_PROTOCOL_LOCATION_ATTMNT
/
DROP TABLE IRB_PROTOCOL_LOCATION_PERSON
/
DROP SEQUENCE SEQ_EXEMPT_FORM_CHECKLST_ID
/
DROP TABLE IRB_EXEMPT_FORM_CHECKLST
/
DROP SEQUENCE SEQ_EXEMPT_FORM_ACTN_LOG_ID
/
DROP TABLE IRB_EXEMPT_FORM_ACTN_LOG
/
DROP TABLE IRB_EXEMPT_ACTION_TYPE
/
DROP SEQUENCE SEQ_IRB_PERSON_EXEMPT_FORM_ID
/
DROP SEQUENCE SEQ_IRB_PROTOCOL_NUMBER
/
DROP SEQUENCE SEQ_QUESTIONNAIRE_ANSWER_ID
/
DROP SEQUENCE SEQ_QNR_ANS_HEADER_ID
/
DROP SEQUENCE SEQ_QNR_ANSWER_ATT_ID
/
DROP TABLE IRB_PERSON_EXEMPT_FORM
/
DROP TABLE IRB_EXEMPT_FORM_STATUS
/
DROP TABLE IRB_AMEND_RENEW_MODULES
/
DROP TABLE IRB_AMEND_RENEWAL
/
DROP TABLE IRB_PROTOCOL_NOTEPAD
/
DROP TABLE IRB_EXPIDITED_CHKLST
/
DROP TABLE IRB_PROTO_SUBMISSION_DOC
/
DROP TABLE IRB_EXEMPT_CHKLST
/
DROP TABLE IRB_VOTE_ABSTAINEES
/
DROP TABLE IRB_RESEARCH_AREAS
/
DROP TABLE IRB_CORRESPONDENCE
/
DROP TABLE IRB_EXEMPT_NUMBER
/
DROP TABLE IRB_PROTO_SPECIAL_REVIEW
/
DROP TABLE IRB_PROTO_VOTE_RECUSED
/
DROP TABLE IRB_ATTACHMENT_PERSONNEL
/
DROP TABLE IRB_PROTO_FUNDING_SOURCE
/
DROP TABLE IRB_ATTACHMENT_PROTOCOL
/
DROP TABLE IRB_ATTACHMENT
/
DROP TABLE IRB_PROTOCOL_RISK_LEVELS
/
DROP TABLE IRB_PROTOCOL_LOCATION
/
DROP TABLE IRB_PROTOCOL_ONLN_RVWS
/
DROP TABLE IRB_PROTOCOL_REVIEWERS
/
DROP TABLE IRB_PROTOCOL_CORRESPONDENCE
/
DROP TABLE IRB_PROTOCOL_NOTIFICATION
/
DROP TABLE IRB_PROTOCOL_ACTIONS
/
DROP TABLE IRB_ONLN_RVW_STATUS
/
DROP TABLE IRB_ONLN_RVW_DETRM_RECOM
/
DROP TABLE IRB_PROTOCOL_REFERENCES
/
DROP TABLE IRB_PROTOCOL_UNITS
/
DROP TABLE IRB_PROTOCOL_PERSONS
/
DROP TABLE IRB_PROTOCOL_ASSIGNEE_HISTORY
/
DROP TABLE IRB_PROTOCOL_SUBMISSION
/
DROP TABLE IRB_COMM_SCHEDULE
/
DROP TABLE IRB_SCHEDULE_STATUS
/
DROP TABLE IRB_COMM_RESEARCH_AREAS
/
DROP TABLE IRB_COMMITTEE
/
DROP TABLE IRB_COMMITTEE_TYPE
/
DROP TABLE IRB_PROTOCOL
/
DROP TABLE IRB_PROTOCOL_CONTINGENCY
/
DROP TABLE IRB_PROTOCOL_MODULES
/
DROP TABLE IRB_CONTINGENCY
/
DROP TABLE IRB_ATTACHMENT_TYPE_GROUP
/
DROP TABLE IRB_PERSON_ROLE_MAPPING
/
DROP TABLE IRB_REVIEWER_TYPE
/
DROP TABLE IRB_ATTACHMENT_TYPE
/
DROP TABLE IRB_PROTOCOL_ORG_TYPE
/
DROP TABLE IRB_ATTACHMENT_STATUS
/
DROP TABLE IRB_PERSON_ROLES
/
DROP TABLE IRB_PROTOCOL_REFERENCE_TYPE
/
DROP TABLE IRB_PROTOCOL_ATT_GROUP
/
DROP TABLE IRB_PROTOCOL_REVIEW_TYPE
/
DROP TABLE IRB_PROTOCOL_CORRESP_TYPE
/
DROP TABLE IRB_PROTOCOL_TYPE
/
DROP TABLE IRB_STATUS_ACTION_TYP_MAPPING
/
DROP TABLE IRB_PROTOCOL_ACTION_TYPE
/
DROP TABLE IRB_PROTOCOL_STATUS
/
DELETE FROM MITKC_NOTIFICATION_LOG WHERE NOTIFICATION_NUMBER > 700
/
DELETE FROM MITKC_NOTIFICATION_TYPE WHERE NOTIFICATION_NUMBER > 700
/
COMMIT
/
DROP TABLE MITKC_QUESTIONNAIRE_ANSWER_ATT
/
DROP TABLE MITKC_QUESTIONNAIRE_ANSWER
/
DROP TABLE MITKC_QUESTIONNAIRE_ANS_HEADER
/
DROP TABLE MITKC_QNR_QUESTION_CONDITION
/
DROP TABLE MITKC_QNR_QUESTION_OPTION
/
DROP TABLE MITKC_QUESTIONNAIRE_QUESTION
/
DROP TABLE MITKC_QUESTIONNAIRE
/
--DROP TABLE ELASTIC_IRB_RT
--/
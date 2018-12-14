DELETE FROM IRB_PROTOCOL_CORRESPONDENCE
/
DELETE FROM IRB_PROTOCOL_ACTIONS
/
DELETE FROM IRB_PROTOCOL_ACTION_TYPE
/
DELETE FROM IRB_ATTACHMENT_PROTOCOL
/
DELETE FROM IRB_ATTACHMENT_STATUS
/
DELETE FROM IRB_ATTACHMENT_TYPE
/
DELETE FROM IRB_PROTO_SPECIAL_REVIEW
/
DELETE FROM IRB_PROTO_FUNDING_SOURCE
/
DELETE FROM IRB_PROTOCOL_LOCATION
/
DELETE FROM IRB_PROTOCOL_ORG_TYPE
/
DELETE FROM IRB_PROTO_VULNERABLE_SUB
/
DELETE FROM IRB_PROTOCOL_UNITS
/
DELETE FROM IRB_PROTOCOL_PERSONS
/
DELETE FROM IRB_PROTOCOL_SUBMISSION
/
DELETE FROM IRB_PROTOCOL
/
DELETE FROM IRB_PROTOCOL_TYPE
/
DELETE FROM IRB_PROTOCOL_STATUS
/
DELETE FROM IRB_COMM_CORRESP_BATCH_DETAIL
/
DELETE FROM IRB_COMM_CORRESP_BATCH
/
DELETE FROM IRB_COMM_SCHEDULE_MINUTE_DOC
/
DELETE FROM IRB_COMM_SCHEDULE_MINUTES
/
DELETE FROM IRB_COMM_MEMBER_EXPERTISE
/
DELETE FROM IRB_COMM_MEMBER_ROLES
/
DELETE FROM IRB_COMM_MEMBERSHIPS
/
DELETE FROM IRB_SCHEDULE_AGENDA
/
DELETE FROM IRB_COMM_SCHEDULE_ATTENDANCE
/
DELETE FROM IRB_COMM_SCHEDULE_ACT_ITEMS
/
DELETE FROM IRB_COMM_SCHEDULE
/
DELETE FROM IRB_COMM_RESEARCH_AREAS
/
DELETE FROM IRB_COMMITTEE
/
COMMIT
/
SET DEFINE OFF
/
insert into IRB_COMMITTEE
(
ADV_SUBMISSION_DAYS_REQ,
APPLICABLE_REVIEW_TYPE_CODE,
COMMITTEE_ID,
COMMITTEE_NAME,
COMMITTEE_TYPE_CODE,
CREATE_TIMESTAMP,
CREATE_USER,
DEFAULT_REVIEW_TYPE_CODE,
DESCRIPTION,
HOME_UNIT_NAME,
HOME_UNIT_NUMBER,
MAX_PROTOCOLS,
MINIMUM_MEMBERS_REQUIRED,
SCHEDULE_DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select 
t1.ADV_SUBMISSION_DAYS_REQ,
t1.APPLICABLE_REVIEW_TYPE_CODE,
t1.COMMITTEE_ID,
t1.COMMITTEE_NAME,
t1.COMMITTEE_TYPE_CODE,
t1.CREATE_TIMESTAMP,
t1.CREATE_USER,
t1.DEFAULT_REVIEW_TYPE_CODE,
t1.DESCRIPTION,
t2.UNIT_NAME,
t1.HOME_UNIT_NUMBER,
t1.MAX_PROTOCOLS,
t1.MINIMUM_MEMBERS_REQUIRED,
t1.SCHEDULE_DESCRIPTION,
t1.UPDATE_TIMESTAMP,
t1.UPDATE_USER
from COMMITTEE t1
inner join unit t2 on t1.HOME_UNIT_NUMBER = t2.unit_number
where t1.SEQUENCE_NUMBER in (select max(t3.SEQUENCE_NUMBER) from COMMITTEE t3
                             where t1.COMMITTEE_ID = t3.COMMITTEE_ID)
/
INSERT INTO IRB_COMM_RESEARCH_AREAS
(
COMMITTEE_ID,
COMM_RESEARCH_AREAS_ID,
RESEARCH_AREA_CODE,
RESEARCH_AREA_DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT 
DISTINCT
T5.COMMITTEE_ID,
T1.ID,
T1.RESEARCH_AREA_CODE,
T2.DESCRIPTION,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_RESEARCH_AREAS T1
INNER JOIN RESEARCH_AREAS T2 ON T1.RESEARCH_AREA_CODE = T2.RESEARCH_AREA_CODE
INNER JOIN COMMITTEE T5 ON T1.COMMITTEE_ID_FK = T5.ID
WHERE COMMITTEE_ID_FK IN (SELECT DISTINCT ID FROM COMMITTEE t3
								   where t3.SEQUENCE_NUMBER in (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																 where t3.COMMITTEE_ID = t4.COMMITTEE_ID
															   )
						 )
/
INSERT INTO IRB_COMM_SCHEDULE
(
AGENDA_PROD_REV_DATE,
AVAILABLE_TO_REVIEWERS,
COMMENTS,
COMMITTEE_ID,
END_TIME,
MAX_PROTOCOLS,
MEETING_DATE,
PLACE,
PROTOCOL_SUB_DEADLINE,
SCHEDULED_DATE,
SCHEDULE_ID,
SCHEDULE_STATUS_CODE,
START_TIME,
TIME,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT
DISTINCT
T1.AGENDA_PROD_REV_DATE,
T1.AVAILABLE_TO_REVIEWERS,
T1.COMMENTS,
T5.COMMITTEE_ID,
T1.END_TIME,
T1.MAX_PROTOCOLS,
T1.MEETING_DATE,
T1.PLACE,
T1.PROTOCOL_SUB_DEADLINE,
T1.SCHEDULED_DATE,
T1.ID,
T1.SCHEDULE_STATUS_CODE,
T1.START_TIME,
T1.TIME,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_SCHEDULE T1
INNER JOIN COMMITTEE T5 ON T1.COMMITTEE_ID_FK = T5.ID
WHERE T1.COMMITTEE_ID_FK IN (SELECT DISTINCT ID FROM COMMITTEE t3
						     where t3.SEQUENCE_NUMBER in (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
													   where t3.COMMITTEE_ID = t4.COMMITTEE_ID
													  )
						    )
/
INSERT INTO IRB_COMM_SCHEDULE_ACT_ITEMS
(
ACTION_ITEM_NUMBER,
ACT_ITEM_TYPE_DESCRIPTION,
COMM_SCHEDULE_ACT_ITEMS_ID,
ITEM_DESCTIPTION,
SCHEDULE_ACT_ITEM_TYPE_CODE,
SCHEDULE_ID,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT
DISTINCT
T1.ACTION_ITEM_NUMBER,
T2.DESCRIPTION,
T1.COMM_SCHEDULE_ACT_ITEMS_ID,
T1.ITEM_DESCRIPTION,
T1.SCHEDULE_ACT_ITEM_TYPE_CODE,
T1.SCHEDULE_ID_FK,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_SCHEDULE_ACT_ITEMS T1
INNER JOIN SCHEDULE_ACT_ITEM_TYPE T2 ON T1.SCHEDULE_ACT_ITEM_TYPE_CODE=T2.SCHEDULE_ACT_ITEM_TYPE_CODE
WHERE T1.SCHEDULE_ID_FK IN (select ID from COMM_SCHEDULE
							WHERE COMMITTEE_ID_FK IN (SELECT DISTINCT ID FROM COMMITTEE t3
													  where t3.SEQUENCE_NUMBER in (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																							 where t3.COMMITTEE_ID = t4.COMMITTEE_ID
																				  )
													 )
						   )
/
INSERT INTO IRB_COMM_SCHEDULE_ATTENDANCE
(
ALTERNATE_FLAG,
ALTERNATE_FOR,
COMMENTS,
COMM_SCHEDULE_ATTENDANCE_ID,
GUEST_FLAG,
MEMBER_PRESENT,
NON_EMPLOYEE_FLAG,
PERSON_ID,
PERSON_NAME,
SCHEDULE_ID,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT
DISTINCT
ALTERNATE_FLAG,
ALTERNATE_FOR,
COMMENTS,
COMM_SCHEDULE_ATTENDANCE_ID,
GUEST_FLAG,
'N',
NON_EMPLOYEE_FLAG,
PERSON_ID,
PERSON_NAME,
SCHEDULE_ID_FK,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM COMM_SCHEDULE_ATTENDANCE
WHERE SCHEDULE_ID_FK IN (select ID from COMM_SCHEDULE
						 WHERE COMMITTEE_ID_FK IN ( SELECT DISTINCT ID FROM COMMITTEE t3
													where t3.SEQUENCE_NUMBER in ( select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																				  where t3.COMMITTEE_ID = t4.COMMITTEE_ID
																				)
															)
						)
/
INSERT INTO IRB_SCHEDULE_AGENDA
(
AGENDA_NAME,
AGENDA_NUMBER,
CREATE_TIMESTAMP,
CREATE_USER,
PDF_STORE,
SCHEDULE_AGENDA_ID,
SCHEDULE_ID,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT
AGENDA_NAME,
AGENDA_NUMBER,
CREATE_TIMESTAMP,
CREATE_USER,
PDF_STORE,
SCHEDULE_AGENDA_ID,
SCHEDULE_ID_FK,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM
SCHEDULE_AGENDA
WHERE SCHEDULE_ID_FK IN (select ID from COMM_SCHEDULE
						 WHERE COMMITTEE_ID_FK IN ( SELECT DISTINCT ID FROM COMMITTEE t3
													where t3.SEQUENCE_NUMBER in ( select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																				  where t3.COMMITTEE_ID = t4.COMMITTEE_ID
																				)
												 )
						)
/
INSERT INTO IRB_COMM_MEMBERSHIPS
(
COMMENTS,
COMMITTEE_ID,
COMM_MEMBERSHIP_ID,
CONTACT_NOTES,
MEMBERSHIP_ID,
MEMBERSHIP_TYPE_CODE,
NON_EMPLOYEE_FLAG,
PAID_MEMBER_FLAG,
PERSON_ID,
PERSON_NAME,
ROLODEX_ID,
TERM_END_DATE,
TERM_START_DATE,
TRAINING_NOTES,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT
T1.COMMENTS,
T5.COMMITTEE_ID,
T1.COMM_MEMBERSHIP_ID,
TO_CHAR(T1.CONTACT_NOTES),
T1.MEMBERSHIP_ID,
T1.MEMBERSHIP_TYPE_CODE,
'N',
T1.PAID_MEMBER_FLAG,
T1.PERSON_ID,
T1.PERSON_NAME,
T1.ROLODEX_ID,
T1.TERM_END_DATE,
T1.TERM_START_DATE,
T1.TRAINING_NOTES,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_MEMBERSHIPS T1
INNER JOIN COMMITTEE T5 ON T1.COMMITTEE_ID_FK = T5.ID
WHERE COMMITTEE_ID_FK IN ( SELECT DISTINCT ID FROM COMMITTEE t3
						   where t3.SEQUENCE_NUMBER in ( select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
														 where t3.COMMITTEE_ID = t4.COMMITTEE_ID
													   )
						)
/
INSERT INTO IRB_COMM_MEMBER_ROLES
(
COMM_MEMBERSHIP_ID,
COMM_MEMBER_ROLES_ID,
END_DATE,
MEMBERSHIP_ROLE_CODE,
MEMBERSHIP_ROLE_DESCRIPTION,
START_DATE,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT
DISTINCT
T1.COMM_MEMBERSHIP_ID_FK,
T1.COMM_MEMBER_ROLES_ID,
T1.END_DATE,
T1.MEMBERSHIP_ROLE_CODE,
T2.DESCRIPTION,
T1.START_DATE,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_MEMBER_ROLES T1
INNER JOIN MEMBERSHIP_ROLE T2 ON T1.MEMBERSHIP_ROLE_CODE = T2.MEMBERSHIP_ROLE_CODE
WHERE T1.COMM_MEMBERSHIP_ID_FK IN ( SELECT COMM_MEMBERSHIP_ID 
									FROM COMM_MEMBERSHIPS
									WHERE COMMITTEE_ID_FK IN ( SELECT DISTINCT ID FROM COMMITTEE t3
																		where t3.SEQUENCE_NUMBER in ( select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																									  where t3.COMMITTEE_ID = t4.COMMITTEE_ID
																									)
																	  )
								  )
/
INSERT INTO IRB_COMM_MEMBER_EXPERTISE
(
COMM_MEMBERSHIP_ID,
COMM_MEMBER_EXPERTISE_ID,
RESEARCH_AREA_CODE,
RESEARCH_AREA_DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT
T1.COMM_MEMBERSHIP_ID_FK,
T1.COMM_MEMBER_EXPERTISE_ID,
T1.RESEARCH_AREA_CODE,
T2.DESCRIPTION,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_MEMBER_EXPERTISE T1
INNER JOIN RESEARCH_AREAS T2 ON T1.RESEARCH_AREA_CODE = T2.RESEARCH_AREA_CODE
WHERE T1.COMM_MEMBERSHIP_ID_FK IN ( SELECT COMM_MEMBERSHIP_ID 
									FROM COMM_MEMBERSHIPS
									WHERE COMMITTEE_ID_FK IN ( SELECT DISTINCT ID FROM COMMITTEE t3
															   where t3.SEQUENCE_NUMBER in ( select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																							 where t3.COMMITTEE_ID = t4.COMMITTEE_ID
																							)
															 )
								  )
/
INSERT INTO IRB_COMM_SCHEDULE_MINUTES
(
COMM_SCHEDULE_ACT_ITEMS_ID,
COMM_SCHEDULE_MINUTES_ID,
CREATE_TIMESTAMP,
CREATE_USER,
ENTRY_NUMBER,
FINAL_FLAG,
MINUTE_ENTRY,
MINUTE_ENTRY_TYPE_CODE,
PRIVATE_COMMENT_FLAG,
PROTOCOL_CONTINGENCY_CODE,
PROTOCOL_ID,
PROTOCOL_NUMBER,
REVIEWER_ID,
SCHEDULE_ID,
SUBMISSION_ID,
SUBMISSION_NUMBER,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT 
COMM_SCHEDULE_ACT_ITEMS_ID_FK,
COMM_SCHEDULE_MINUTES_ID,
CREATE_TIMESTAMP,
CREATE_USER,
ENTRY_NUMBER,
FINAL_FLAG,
MINUTE_ENTRY,
MINUTE_ENTRY_TYPE_CODE,
PRIVATE_COMMENT_FLAG,
PROTOCOL_CONTINGENCY_CODE,
PROTOCOL_ID_FK,
PROTOCOL_NUMBER,
REVIEWER_ID_FK,
SCHEDULE_ID_FK,
SUBMISSION_ID_FK,
SUBMISSION_NUMBER,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM COMM_SCHEDULE_MINUTES
WHERE SCHEDULE_ID_FK IN (select ID from COMM_SCHEDULE
						 WHERE COMMITTEE_ID_FK IN (SELECT DISTINCT ID FROM COMMITTEE t3
												   where t3.SEQUENCE_NUMBER in (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
																				where t3.COMMITTEE_ID = t4.COMMITTEE_ID
																				)
												  )
						)
/
INSERT INTO IRB_COMM_SCHEDULE_MINUTE_DOC
(
CREATE_TIMESTAMP,
CREATE_USER,
MINUTE_NAME,
MINUTE_NUMBER,
PDF_STORE,
SCHEDULE_ID,
SCHEDULE_MINUTE_DOC_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT 
CREATE_TIMESTAMP,
CREATE_USER,
MINUTE_NAME,
MINUTE_NUMBER,
PDF_STORE,
SCHEDULE_ID_FK,
COMM_SCHEDULE_MINUTE_DOC_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM COMM_SCHEDULE_MINUTE_DOC
WHERE SCHEDULE_ID_FK IN (select ID from COMM_SCHEDULE
						 WHERE COMMITTEE_ID_FK IN (SELECT DISTINCT ID FROM COMMITTEE t3
                                                   where t3.SEQUENCE_NUMBER IN (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
                                                                                where t3.COMMITTEE_ID = t4.COMMITTEE_ID
                                                                                )
                                                  )
                        )
/
INSERT INTO IRB_COMM_CORRESP_BATCH
(
BATCH_CORRESPONDENCE_TYPE_CODE,
BATCH_RUN_DATE,
COMMITTEE_ID,
CORRESP_BATCH_ID,
TIME_WINDOW_END,
TIME_WINDOW_START,
UPDATE_TIMESTAMP,
UPDATE_USER)
SELECT
T1.BATCH_CORRESPONDENCE_TYPE_CODE,
T1.BATCH_RUN_DATE,
T1.COMMITTEE_ID,
T1.COMM_BATCH_CORRESP_ID,
T1.TIME_WINDOW_END,
T1.TIME_WINDOW_START,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER 
FROM COMM_BATCH_CORRESP T1
WHERE COMMITTEE_ID IN (SELECT DISTINCT COMMITTEE_ID FROM COMMITTEE t3
                       where t3.SEQUENCE_NUMBER in (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
                                                     where t3.COMMITTEE_ID = t4.COMMITTEE_ID
                                                    )
                      )
/
INSERT INTO IRB_COMM_CORRESP_BATCH_DETAIL
(
ACTION_ID,
CORRESP_BATCH_DETAIL_ID,
CORRESP_BATCH_ID,
PROTOCOL_NUMBER,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT
T2.ACTION_ID,
T1.COMM_BATCH_CORRESP_DETAIL_ID,
T1.COMM_BATCH_CORRESP_ID,
T2.PROTOCOL_NUMBER,
T1.UPDATE_TIMESTAMP,
T1.UPDATE_USER
FROM COMM_BATCH_CORRESP_DETAIL T1
INNER JOIN PROTOCOL_ACTIONS T2 ON T1.PROTOCOL_ACTION_ID = T2.PROTOCOL_ACTION_ID 
WHERE T1.COMM_BATCH_CORRESP_ID IN (SELECT COMM_BATCH_CORRESP_ID FROM COMM_BATCH_CORRESP
                                   WHERE COMMITTEE_ID IN (SELECT DISTINCT COMMITTEE_ID FROM COMMITTEE t3
                                                           WHERE t3.SEQUENCE_NUMBER in (select max(t4.SEQUENCE_NUMBER) from COMMITTEE t4
                                                                                        where t3.COMMITTEE_ID = t4.COMMITTEE_ID
                                                                                        )
                                                         )
                                  )
/
---------------ENDS COMMITTE MIGRATION-------------------
DELETE FROM IRB_PROTOCOL_REVIEW_TYPE
/
INSERT INTO IRB_PROTOCOL_REVIEW_TYPE
(	
PROTOCOL_REVIEW_TYPE_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER,
GLOBAL_FLAG
)
select PROTOCOL_REVIEW_TYPE_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER,
GLOBAL_FLAG
from PROTOCOL_REVIEW_TYPE
/
INSERT INTO IRB_PROTOCOL_STATUS(
PROTOCOL_STATUS_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT 
PROTOCOL_STATUS_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM PROTOCOL_STATUS
/
INSERT INTO IRB_PROTOCOL_TYPE(
PROTOCOL_TYPE_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT 
PROTOCOL_TYPE_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM PROTOCOL_TYPE
/
insert into IRB_PROTOCOL(
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
PROTOCOL_TYPE_CODE,
PROTOCOL_STATUS_CODE,
TITLE,
DESCRIPTION,
APPROVAL_DATE,
EXPIRATION_DATE,
LAST_APPROVAL_DATE,
FDA_APPLICATION_NUMBER,
REFERENCE_NUMBER_1,
REFERENCE_NUMBER_2,
INITIAL_SUBMISSION_DATE,
ACTIVE,
CREATE_TIMESTAMP,
CREATE_USER,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select distinct
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
PROTOCOL_TYPE_CODE,
PROTOCOL_STATUS_CODE,
TITLE,
DESCRIPTION,
APPROVAL_DATE,
EXPIRATION_DATE,
LAST_APPROVAL_DATE,
FDA_APPLICATION_NUMBER,
REFERENCE_NUMBER_1,
REFERENCE_NUMBER_2,
INITIAL_SUBMISSION_DATE,
ACTIVE,
CREATE_TIMESTAMP,
CREATE_USER,
UPDATE_TIMESTAMP,
UPDATE_USER
from protocol
/
update IRB_PROTOCOL set IS_LATEST = 'N'
/
commit
/
update IRB_PROTOCOL t1 set t1.IS_LATEST = 'Y' 
where t1.protocol_id IN  (
                          select max(protocol_id)
                          from IRB_PROTOCOL
                          group by protocol_number
                          ) 
/
commit
/
insert into IRB_PROTOCOL_SUBMISSION(
SUBMISSION_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
SUBMISSION_NUMBER,
SCHEDULE_ID,
COMMITTEE_ID,
PROTOCOL_ID,
SUBMISSION_TYPE_CODE,
SUBMISSION_TYPE_QUAL_CODE,
SUBMISSION_STATUS_CODE,
PROTOCOL_REVIEW_TYPE_CODE,
SUBMISSION_DATE,
COMMENTS,
YES_VOTE_COUNT,
NO_VOTE_COUNT,
ABSTAINER_COUNT,
VOTING_COMMENTS,
UPDATE_TIMESTAMP,
UPDATE_USER,
RECUSED_COUNT,
IS_BILLABLE,
COMM_DECISION_MOTION_TYPE_CODE
)
select distinct
SUBMISSION_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
SUBMISSION_NUMBER,
SCHEDULE_ID,
COMMITTEE_ID,
PROTOCOL_ID,
SUBMISSION_TYPE_CODE,
SUBMISSION_TYPE_QUAL_CODE,
SUBMISSION_STATUS_CODE,
PROTOCOL_REVIEW_TYPE_CODE,
SUBMISSION_DATE,
COMMENTS,
YES_VOTE_COUNT,
NO_VOTE_COUNT,
ABSTAINER_COUNT,
VOTING_COMMENTS,
UPDATE_TIMESTAMP,
UPDATE_USER,
RECUSED_COUNT,
IS_BILLABLE,
COMM_DECISION_MOTION_TYPE_CODE
from PROTOCOL_SUBMISSION
WHERE SCHEDULE_ID IN (SELECT SCHEDULE_ID FROM IRB_COMM_SCHEDULE)
/
ALTER TABLE IRB_PROTOCOL_PERSONS ADD rolodex_id NUMBER(6)
/
insert into IRB_PROTOCOL_PERSONS(
PROTOCOL_PERSON_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
PERSON_ID,
PERSON_NAME,
PROTOCOL_PERSON_ROLE_ID,
ROLODEX_ID,
AFFILIATION_TYPE_CODE,
UPDATE_TIMESTAMP,
UPDATE_USER,
COMMENTS,
SSN,
LAST_NAME,
FIRST_NAME,
MIDDLE_NAME,
FULL_NAME,
PRIOR_NAME,
USER_NAME,
EMAIL_ADDRESS,
DATE_OF_BIRTH,
AGE,
AGE_BY_FISCAL_YEAR,
GENDER,
RACE,
EDUCATION_LEVEL,
DEGREE,
MAJOR,
IS_HANDICAPPED,
HANDICAP_TYPE,
IS_VETERAN,
VETERAN_TYPE,
VISA_CODE,
VISA_TYPE,
VISA_RENEWAL_DATE,
HAS_VISA,
OFFICE_LOCATION,
OFFICE_PHONE,
SECONDRY_OFFICE_LOCATION,
SECONDRY_OFFICE_PHONE,
SCHOOL,
YEAR_GRADUATED,
DIRECTORY_DEPARTMENT,
SALUTATION,
COUNTRY_OF_CITIZENSHIP,
PRIMARY_TITLE,
DIRECTORY_TITLE,
HOME_UNIT,
IS_FACULTY,
IS_GRADUATE_STUDENT_STAFF,
IS_RESEARCH_STAFF,
IS_SERVICE_STAFF,
IS_SUPPORT_STAFF,
IS_OTHER_ACCADEMIC_GROUP,
IS_MEDICAL_STAFF,
VACATION_ACCURAL,
IS_ON_SABBATICAL,
ID_PROVIDED,
ID_VERIFIED,
ADDRESS_LINE_1,
ADDRESS_LINE_2,
ADDRESS_LINE_3,
CITY,
COUNTY,
STATE,
POSTAL_CODE,
COUNTRY_CODE,
FAX_NUMBER,
PAGER_NUMBER,
MOBILE_PHONE_NUMBER,
ERA_COMMONS_USER_NAME
)
select distinct
PROTOCOL_PERSON_ID,
PROTOCOL_PERSONS.PROTOCOL_ID,
PROTOCOL_PERSONS.PROTOCOL_NUMBER,
PROTOCOL_PERSONS.SEQUENCE_NUMBER,
PROTOCOL_PERSONS.PERSON_ID,
PROTOCOL_PERSONS.PERSON_NAME,
PROTOCOL_PERSON_ROLE_ID,
ROLODEX_ID,
AFFILIATION_TYPE_CODE,
PROTOCOL_PERSONS.UPDATE_TIMESTAMP,
PROTOCOL_PERSONS.UPDATE_USER,
to_char(COMMENTS),
SSN,
LAST_NAME,
FIRST_NAME,
MIDDLE_NAME,
FULL_NAME,
PRIOR_NAME,
USER_NAME,
EMAIL_ADDRESS,
DATE_OF_BIRTH,
AGE,
AGE_BY_FISCAL_YEAR,
GENDER,
RACE,
EDUCATION_LEVEL,
DEGREE,
MAJOR,
IS_HANDICAPPED,
HANDICAP_TYPE,
IS_VETERAN,
VETERAN_TYPE,
VISA_CODE,
VISA_TYPE,
VISA_RENEWAL_DATE,
HAS_VISA,
OFFICE_LOCATION,
OFFICE_PHONE,
SECONDRY_OFFICE_LOCATION,
SECONDRY_OFFICE_PHONE,
SCHOOL,
YEAR_GRADUATED,
DIRECTORY_DEPARTMENT,
SALUTATION,
COUNTRY_OF_CITIZENSHIP,
PRIMARY_TITLE,
DIRECTORY_TITLE,
HOME_UNIT,
IS_FACULTY,
IS_GRADUATE_STUDENT_STAFF,
IS_RESEARCH_STAFF,
IS_SERVICE_STAFF,
IS_SUPPORT_STAFF,
IS_OTHER_ACCADEMIC_GROUP,
IS_MEDICAL_STAFF,
VACATION_ACCURAL,
IS_ON_SABBATICAL,
ID_PROVIDED,
ID_VERIFIED,
ADDRESS_LINE_1,
ADDRESS_LINE_2,
ADDRESS_LINE_3,
CITY,
COUNTY,
PROTOCOL_PERSONS.STATE,
PROTOCOL_PERSONS.POSTAL_CODE,
PROTOCOL_PERSONS.COUNTRY_CODE,
PROTOCOL_PERSONS.FAX_NUMBER,
PROTOCOL_PERSONS.PAGER_NUMBER,
PROTOCOL_PERSONS.MOBILE_PHONE_NUMBER,
PROTOCOL_PERSONS.ERA_COMMONS_USER_NAME
from PROTOCOL_PERSONS inner join IRB_PROTOCOL on PROTOCOL_PERSONS.protocol_id = IRB_PROTOCOL.protocol_id
/
insert into IRB_PROTOCOL_UNITS(
PROTOCOL_PERSON_ID,
PROTOCOL_UNITS_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
UNIT_NUMBER,
LEAD_UNIT_FLAG,
PERSON_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select 
PROTOCOL_PERSON_ID,
PROTOCOL_UNITS_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
UNIT_NUMBER,
LEAD_UNIT_FLAG,
PERSON_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
from protocol_units
WHERE PROTOCOL_PERSON_ID IN (SELECT PROTOCOL_PERSON_ID FROM IRB_PROTOCOL_PERSONS)
/
insert into IRB_PROTO_VULNERABLE_SUB(
PROTOCOL_VULNERABLE_SUB_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
VULNERABLE_SUBJECT_TYPE_CODE,
SUBJECT_COUNT,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select distinct
PROTOCOL_VULNERABLE_SUB_ID,
t1.PROTOCOL_ID,
t1.PROTOCOL_NUMBER,
t1.SEQUENCE_NUMBER,
VULNERABLE_SUBJECT_TYPE_CODE,
SUBJECT_COUNT,
t1.UPDATE_TIMESTAMP,
t1.UPDATE_USER
from PROTOCOL_VULNERABLE_SUB t1 inner join IRB_PROTOCOL t2 on t1.protocol_id = t2.protocol_id
/
INSERT INTO IRB_PROTOCOL_ORG_TYPE(
PROTOCOL_ORG_TYPE_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select 
PROTOCOL_ORG_TYPE_CODE,
DESCRIPTION,
UPDATE_TIMESTAMP,
UPDATE_USER
from
PROTOCOL_ORG_TYPE
/
insert into IRB_PROTOCOL_LOCATION(
PROTOCOL_LOCATION_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
PROTOCOL_ORG_TYPE_CODE,
ORGANIZATION_ID,
ROLODEX_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
)
select distinct
t1.PROTOCOL_LOCATION_ID,
t1.PROTOCOL_ID,
t1.PROTOCOL_NUMBER,
t1.SEQUENCE_NUMBER,
t1.PROTOCOL_ORG_TYPE_CODE,
t1.ORGANIZATION_ID,
t1.ROLODEX_ID,
t1.UPDATE_TIMESTAMP,
t1.UPDATE_USER
from PROTOCOL_LOCATION t1 inner join IRB_PROTOCOL t2 on t1.protocol_id = t2.protocol_id
/
insert into IRB_PROTO_FUNDING_SOURCE(
PROTOCOL_FUNDING_SOURCE_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
FUNDING_SOURCE_TYPE_CODE,
FUNDING_SOURCE,
UPDATE_TIMESTAMP,
UPDATE_USER,
FUNDING_SOURCE_NAME
)
select distinct
t1.PROTOCOL_FUNDING_SOURCE_ID,
t1.PROTOCOL_ID,
t1.PROTOCOL_NUMBER,
t1.SEQUENCE_NUMBER,
t1.FUNDING_SOURCE_TYPE_CODE,
t1.FUNDING_SOURCE,
t1.UPDATE_TIMESTAMP,
t1.UPDATE_USER,
t1.FUNDING_SOURCE_NAME
from PROTOCOL_FUNDING_SOURCE t1 inner join IRB_PROTOCOL t2 on t1.protocol_id = t2.protocol_id
/
insert into IRB_PROTO_SPECIAL_REVIEW(
PROTOCOL_SPECIAL_REVIEW_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
SPECIAL_REVIEW_NUMBER,
SPECIAL_REVIEW_CODE,
APPROVAL_TYPE_CODE,
APPLICATION_DATE,
APPROVAL_DATE,
EXPIRATION_DATE,
COMMENTS,
UPDATE_USER,
UPDATE_TIMESTAMP

)
select distinct
t1.PROTOCOL_SPECIAL_REVIEW_ID,
t1.PROTOCOL_ID,
t2.PROTOCOL_NUMBER,
t2.SEQUENCE_NUMBER,
t1.SPECIAL_REVIEW_NUMBER,
t1.SPECIAL_REVIEW_CODE,
t1.APPROVAL_TYPE_CODE,
t1.APPLICATION_DATE,
t1.APPROVAL_DATE,
t1.EXPIRATION_DATE,
to_char(t1.COMMENTS),
t1.UPDATE_USER,
t1.UPDATE_TIMESTAMP
from PROTOCOL_SPECIAL_REVIEW t1 inner join IRB_PROTOCOL t2 on t1.protocol_id = t2.protocol_id
/
update IRB_PROTOCOL_PERSONS set NON_EMPLOYEE_FLAG = 'N'
/
update IRB_PROTOCOL_PERSONS set NON_EMPLOYEE_FLAG = 'Y'
WHERE rolodex_id is not null
/
update IRB_PROTOCOL_PERSONS set person_id = rolodex_id
WHERE rolodex_id is not null
and person_id is null
/
ALTER TABLE IRB_PROTOCOL_PERSONS DROP COLUMN rolodex_id
/
UPDATE IRB_PROTOCOL_UNITS t1 
	SET t1.PROTOCOL_ID = (SELECT MAX(t2.PROTOCOL_ID)
                         FROM IRB_PROTOCOL t2
                         WHERE t1.PROTOCOL_NUMBER = t2.PROTOCOL_NUMBER
                         AND t1.SEQUENCE_NUMBER = t2.SEQUENCE_NUMBER
						)
/
commit
/       
INSERT INTO IRB_ATTACHMENT_TYPE(
  TYPE_CD,
  DESCRIPTION,
  UPDATE_TIMESTAMP,
  UPDATE_USER
)
SELECT 
  TYPE_CD,
  DESCRIPTION,
  UPDATE_TIMESTAMP,
  UPDATE_USER
FROM PROTOCOL_ATTACHMENT_TYPE
/
INSERT INTO IRB_ATTACHMENT_STATUS(
  STATUS_CD,
  DESCRIPTION,
  UPDATE_TIMESTAMP,
  UPDATE_USER
)
SELECT 
  STATUS_CD,
  DESCRIPTION,
  UPDATE_TIMESTAMP,
  UPDATE_USER
FROM PROTOCOL_ATTACHMENT_STATUS
/
INSERT INTO IRB_ATTACHMENT_PROTOCOL(
  PA_PROTOCOL_ID,
  PROTOCOL_ID,
  PROTOCOL_NUMBER,
  SEQUENCE_NUMBER,
  TYPE_CD,
  DOCUMENT_ID,
  FILE_ID,
  DESCRIPTION,
  STATUS_CD,
  CONTACT_NAME,
  EMAIL_ADDRESS,
  PHONE_NUMBER,
  COMMENTS,
  UPDATE_TIMESTAMP,
  UPDATE_USER,
  DOCUMENT_STATUS_CODE,
  CREATE_TIMESTAMP,
  ATTACHMENT_VERSION
)
SELECT 
  PA_PROTOCOL_ID,
  PROTOCOL_ID_FK,
  PROTOCOL_NUMBER,
  SEQUENCE_NUMBER,
  TYPE_CD,
  DOCUMENT_ID,
  FILE_ID,
  DESCRIPTION,
  STATUS_CD,
  CONTACT_NAME,
  EMAIL_ADDRESS,
  PHONE_NUMBER,
  COMMENTS,
  UPDATE_TIMESTAMP,
  UPDATE_USER,
  DOCUMENT_STATUS_CODE,
  CREATE_TIMESTAMP,
  ATTACHMENT_VERSION
FROM PROTOCOL_ATTACHMENT_PROTOCOL
/
INSERT INTO IRB_PROTOCOL_ACTION_TYPE(
PROTOCOL_ACTION_TYPE_CODE,
DESCRIPTION,
TRIGGER_SUBMISSION,
TRIGGER_CORRESPONDENCE,
FINAL_ACTION_FOR_BATCH_CORRESP,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT 
PROTOCOL_ACTION_TYPE_CODE,
DESCRIPTION,
TRIGGER_SUBMISSION,
TRIGGER_CORRESPONDENCE,
FINAL_ACTION_FOR_BATCH_CORRESP,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM PROTOCOL_ACTION_TYPE
/
INSERT INTO IRB_PROTOCOL_ACTIONS(
PROTOCOL_ACTION_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
SUBMISSION_NUMBER,
ACTION_ID,
PROTOCOL_ACTION_TYPE_CODE,
SUBMISSION_ID,
COMMENTS,
ACTION_DATE,
UPDATE_TIMESTAMP,
UPDATE_USER,
ACTUAL_ACTION_DATE,
PREV_SUBMISSION_STATUS_CODE,
SUBMISSION_TYPE_CODE,
PREV_PROTOCOL_STATUS_CODE,
FOLLOWUP_ACTION_CODE,
CREATE_USER,
CREATE_TIMESTAMP
)
SELECT 
PROTOCOL_ACTION_ID,
PROTOCOL_ID,
PROTOCOL_NUMBER,
SEQUENCE_NUMBER,
SUBMISSION_NUMBER,
ACTION_ID,
PROTOCOL_ACTION_TYPE_CODE,
SUBMISSION_ID_FK,
COMMENTS,
ACTION_DATE,
UPDATE_TIMESTAMP,
UPDATE_USER,
ACTUAL_ACTION_DATE,
PREV_SUBMISSION_STATUS_CODE,
SUBMISSION_TYPE_CODE,
PREV_PROTOCOL_STATUS_CODE,
FOLLOWUP_ACTION_CODE,
CREATE_USER,
CREATE_TIMESTAMP
FROM PROTOCOL_ACTIONS
WHERE SUBMISSION_ID_FK IN (SELECT SUBMISSION_ID FROM IRB_PROTOCOL_SUBMISSION)
/
commit
/
DELETE FROM IRB_PROTOCOL_CORRESPONDENCE
/
DELETE FROM IRB_PROTOCOL_CORRESP_TYPE
/
INSERT INTO IRB_PROTOCOL_CORRESP_TYPE(
PROTO_CORRESP_TYPE_CODE,
DESCRIPTION,
MODULE_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
)
SELECT 
PROTO_CORRESP_TYPE_CODE,
DESCRIPTION,
MODULE_ID,
UPDATE_TIMESTAMP,
UPDATE_USER
FROM PROTO_CORRESP_TYPE
/
INSERT INTO IRB_PROTOCOL_CORRESPONDENCE(
  PROTOCOL_CORRESPONDENCE_ID,
  PROTOCOL_ID,
  PROTOCOL_NUMBER,
  SEQUENCE_NUMBER,
  PROTOCOL_ACTION_ID,
  PROTO_CORRESP_TYPE_CODE,
  FINAL_FLAG,
  CORRESPONDENCE,
  CREATE_TIMESTAMP,
  CREATE_USER,
  UPDATE_TIMESTAMP,
  UPDATE_USER,
  FINAL_FLAG_TIMESTAMP
)
SELECT 
  ID,
  PROTOCOL_ID,
  PROTOCOL_NUMBER,
  SEQUENCE_NUMBER,
  ACTION_ID_FK,
  PROTO_CORRESP_TYPE_CODE,
  FINAL_FLAG,
  CORRESPONDENCE,
  NVL(CREATE_TIMESTAMP,UPDATE_TIMESTAMP),
  NVL(CREATE_USER,UPDATE_USER),
  UPDATE_TIMESTAMP,
  UPDATE_USER,
  FINAL_FLAG_TIMESTAMP
FROM PROTOCOL_CORRESPONDENCE
WHERE ACTION_ID_FK in ( SELECT PROTOCOL_ACTION_ID FROM IRB_PROTOCOL_ACTIONS)
/
commit
/
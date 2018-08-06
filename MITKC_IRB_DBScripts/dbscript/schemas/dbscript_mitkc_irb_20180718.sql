CREATE TABLE MITKC_IRB_EXEMPT_ACTION_TYPE(
"ACTION_TYPE_CODE" VARCHAR2(3 BYTE), 
"DESCRIPTION" VARCHAR2(200 BYTE) NOT NULL ENABLE, 
"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
"UPDATE_USER" VARCHAR2(60 BYTE) NOT NULL ENABLE, 
CONSTRAINT "MITKC_IRB_EXEMPT_ACTION_TYP_PK" PRIMARY KEY ("ACTION_TYPE_CODE")
)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('1', 'Created', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('2', 'Submitted', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('3', 'Approved by Faculty sponsor', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('4', 'Returned by Faculty sponsor', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('5', 'Returned by IRB Office', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('6', 'Approved by IRB Office', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('7', 'Returned by PI', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_ACTION_TYPE" (ACTION_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('8', 'Approved by PI', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_FORM_STATUS" (EXEMPT_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('3', 'Enroute to IRB office', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_FORM_STATUS" (EXEMPT_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('4', 'Submitted', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_FORM_STATUS" (EXEMPT_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('5', 'Enroute to PI', sysdate, user)
/
UPDATE MITKC_IRB_EXEMPT_FORM_STATUS
SET DESCRIPTION = 'Enroute to Faculty sponsor'
WHERE EXEMPT_STATUS_CODE = 2
/
UPDATE MITKC_IRB_PERSON_EXEMPT_FORM
SET EXEMPT_STATUS_CODE = 4
WHERE EXEMPT_STATUS_CODE = 2
/
CREATE TABLE "MITKC_IRB_EXEMPT_FORM_ACTN_LOG" 
(	
"EXEMPT_FORM_ACTN_LOG_ID"		NUMBER(12,0), 
"IRB_PERSON_EXEMPT_FORM_ID"		NUMBER(12,0), 
"ACTION_TYPE_CODE" 				VARCHAR2(3 BYTE),
"COMMENTS"						VARCHAR2(4000),
"EXEMPT_STATUS_CODE" 			VARCHAR2(3 BYTE),
"UPDATE_TIMESTAMP" 				DATE NOT NULL, 
"UPDATE_USER" 					VARCHAR2(60 BYTE) NOT NULL
)
/
CREATE UNIQUE INDEX "MITKC_IRB_EXEMPT_ACTN_LOG_IDX" ON "MITKC_IRB_EXEMPT_FORM_ACTN_LOG" ("EXEMPT_FORM_ACTN_LOG_ID") 
/
ALTER TABLE "MITKC_IRB_EXEMPT_FORM_ACTN_LOG" ADD CONSTRAINT "MITKC_IRB_EXEMPT_ACTN_LOG_PK" PRIMARY KEY ("EXEMPT_FORM_ACTN_LOG_ID")
/
ALTER TABLE "MITKC_IRB_EXEMPT_FORM_ACTN_LOG" ADD CONSTRAINT "MITKC_IRB_EXEMPT_ACTN_LOG_FK1" FOREIGN KEY ("ACTION_TYPE_CODE")
REFERENCES "MITKC_IRB_EXEMPT_ACTION_TYPE" ("ACTION_TYPE_CODE") ENABLE
/
CREATE SEQUENCE  "SEQ_EXEMPT_FORM_ACTN_LOG_ID"  
MINVALUE 1 
MAXVALUE 999999999999 
INCREMENT BY 1
START WITH 1
NOCACHE 
NOORDER 
NOCYCLE
/
CREATE TABLE MITKC_IRB_EXEMPT_FORM_CHECKLST(
EXEMPT_FORM_CHECKLST_ID			NUMBER(12,0), 
IRB_PERSON_EXEMPT_FORM_ID		NUMBER(12,0), 
DESCRIPTION VARCHAR2(200 BYTE) NOT NULL ENABLE, 
FILENAME	VARCHAR2(500 BYTE),
FILE_DATA	BLOB,
CONTENT_TYPE	VARCHAR2(255 BYTE),
UPDATE_TIMESTAMP	DATE,
UPDATE_USER	VARCHAR2(8 BYTE), 
CONSTRAINT "MITKC_IRB_EXEMPT_FRM_CHKLST_PK" PRIMARY KEY ("EXEMPT_FORM_CHECKLST_ID")
)
/
CREATE SEQUENCE  "SEQ_EXEMPT_FORM_CHECKLST_ID"  
MINVALUE 1 
MAXVALUE 999999999999 
INCREMENT BY 1
START WITH 1
NOCACHE 
NOORDER 
NOCYCLE
/
ALTER TABLE "MITKC_IRB_PERSON_EXEMPT_FORM" ADD START_DATE  DATE
/
ALTER TABLE "MITKC_IRB_PERSON_EXEMPT_FORM" ADD END_DATE  DATE
/
SET DEFINE OFF
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (701,'Exempt - When PI Submits (If FS is not required), Send to PI. Inform PI that study is exempt.','Determination - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
A determination has been made regarding your request, {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} - {EXEMPT_CATEGORY}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
Investigators must comply with the requirement outline in the Investigator Responsibility Guidelines outlined on the <a href="https://www.google.co.in/">COUHES website</a>.
<br/><br/>
If you have questions, contact the COUHES to determine further required action.
<br/><br/>
Thank you,
<br/><br/>
<a href="https://www.google.co.in/">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (702,'NonExempt – When PI Submits (If No FS required), send to PI, saying your study is non-exempt','Determination - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
A determination has been made regarding your request, {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} - {EXEMPT_CATEGORY} {COMMENTS}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
A non-exempt determination requires Comprehensive Review. Please visit the <a href="https://www.google.co.in/">COUHES website</a> for further information.
<br/><br/> 
If you have questions, contact the COUHES to determine further required action.
<br/><br/>
Thank you,
<br/><br/>
<a href="https://www.google.co.in/">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (703,'When PI Submits (If No FS required), send to IRB, saying one study is submitted as non-exempt','Determination - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
A determination has been made regarding request, {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} - {EXEMPT_CATEGORY} {COMMENTS}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
Thank you,
<br/><br/>
MIT COUHES Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (704,'Exempt – When FS clicks reject button, send to PI, saying your study is returned by FS','[ACTION REQUIRED] Status ({EXEMPT_FORM_STATUS}) - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
An action is required for {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/> 
&nbsp;&nbsp;&nbsp;&nbsp;<b>Action Required:</b> Response - Returned by Faculty Sponsor
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Note:</b> {COMMENTS}
<br/><br/>
If you have questions, contact the COUHES to determine further required action.
<br/><br/>
Thank you,
<br/><br/>
<a href="https://www.google.co.in/">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (705,'Exempt – When PI submits (FS required) send to FS, saying one study is waiting for your review','[ACTION REQUIRED] Status ({EXEMPT_FORM_STATUS}) - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
An action is required for {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Action Required:</b> Response – Review Request
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} - {EXEMPT_CATEGORY}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
<P>As Faculty Sponsor, you are required to oversee the human subject research conducted under this study and ensure investigators adhere to the Investigator Responsibility Guidelines outlined on the <a href="{IRB_URL}">COUHES website</a></P>
<br/><br/>
If you have questions, contact the COUHES to determine further required action.
<br/><br/>
Thank you,
<br/><br/>
<a href="{IRB_URL}">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (706,'Exempt – When FS approves send to PI & FS, saying study is approved','Determination - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
A determination has been made regarding your request, {EXEMPT_NUMBER} - {STUDY_TITLE}. 
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} – {EXEMPT_CATEGORY}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
Investigators must comply with the requirement outline in the Investigator Responsibility Guidelines outlined on the <a href="{IRB_URL}">COUHES website.</a>
<br/><br/>
If you have questions, contact the COUHES to determine further required action.
<br/><br/>
Thank you,
<br/><br/>
<a href="{IRB_URL}">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (707,'Non Exempt – When FS approves send to PI, FS saying study is approved','Determination - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
A determination has been made regarding your request, {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} – {EXEMPT_CATEGORY} {COMMENTS}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator: {PI_NAME}
</b>
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor: {FACULTY_SPONSOR}
</b>
<br/><br/>
Thank you,
<br/><br/>
<a href="{IRB_URL}">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects |
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (708,'Non Exempt – When FS rejects a study send to PI, saying your study is rejected by FS','[ACTION REQUIRED] Status ({EXEMPT_FORM_STATUS}) - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
An action is required for {EXEMPT_NUMBER} - {STUDY_TITLE}. 
<br/><br/> 
<b>Action Required:</b> Response – Returned by Faculty Sponsor
<br/><br/> 
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/> 
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/> 
&nbsp;&nbsp;&nbsp;&nbsp;<b>Note:</b> {COMMENTS}
<br/><br/> 
If you have questions, contact the COUHES to determine further required action.
<br/><br/> 
Thank you,
<br/><br/>
<a href="{IRB_URL}">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/> 
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/> 
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
Insert into MITKC_NOTIFICATION_TYPE (NOTIFICATION_NUMBER,DESCRIPTION,SUBJECT,IS_ACTIVE,UPDATE_USER,UPDATE_TIMESTAMP,MODULE_CODE,MESSAGE) values (709,'Non Exempt – When PI submits a study (FS required) send to FS, saying one study is waiting for your review','[ACTION REQUIRED] Status ({EXEMPT_FORM_STATUS}) - {EXEMPT_NUMBER} - {STUDY_TITLE}','Y',user,sysdate,null,'Hi,
<br/><br/>
An action is required for {EXEMPT_NUMBER} - {STUDY_TITLE}.
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Action Required:</b> Response – Review Request
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Exempt Determination:</b> {DETERMINATION} – {EXEMPT_CATEGORY} {COMMENTS}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Principal Investigator:</b> {PI_NAME}
<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<b>Faculty Sponsor:</b> {FACULTY_SPONSOR}
<br/><br/>
As Faculty Sponsor, you are required to oversee the human subject research conducted under this study and ensure investigators adhere to theInvestigator Responsibility Guidelines outlined on the <a href="{IRB_URL}">COUHES website</a>
<br/><br/>
If you have questions, contact the COUHES to determine further required action.
<br/><br/>
Thank you,
<br/><br/>
<a href="{IRB_URL}">MIT COUHES</a> Committee on the Use of Humans as Experimental Subjects | 
<br/><br/>
E25-143b, 77 Mass Ave, Cambridge, MA 02139
<br/><br/>
Ph: 617-253-6787 Email: COUHES@mit.edu')
/
commit
/
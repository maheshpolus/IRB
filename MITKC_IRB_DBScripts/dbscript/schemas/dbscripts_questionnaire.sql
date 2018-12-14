SET DEFINE OFF
/
CREATE TABLE "MITKC_QNR_QUESTION_CONDITION" 
(	"QUESTION_CONDITION_ID" NUMBER(12,0), 
"QUESTION_ID" NUMBER(12,0), 
"CONDITION_TYPE" VARCHAR2(90 BYTE), 
"CONDITION_VALUE" VARCHAR2(2000 BYTE), 
"GROUP_NAME" VARCHAR2(60 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE)
) 
/
CREATE TABLE "MITKC_QNR_QUESTION_OPTION" 
(	"QUESTION_OPTION_ID" NUMBER(12,0), 
"QUESTION_ID" NUMBER(12,0), 
"OPTION_NUMBER" NUMBER(3,0), 
"OPTION_LABEL" VARCHAR2(200 BYTE), 
"REQUIRE_EXPLANATION" VARCHAR2(1 BYTE), 
"EXPLANTION_LABEL" VARCHAR2(2000 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE)
)
/
CREATE TABLE "MITKC_QUESTIONNAIRE" 
(	"QUESTIONNAIRE_ID" NUMBER(12,0), 
"QUESTIONNAIRE" VARCHAR2(200 BYTE), 
"DESCRIPTION" VARCHAR2(4000 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE)
) 
/
CREATE TABLE "MITKC_QUESTIONNAIRE_ANS_HEADER" 
(	"QUESTIONNAIRE_ANS_HEADER_ID" NUMBER(12,0), 
"QUESTIONNAIRE_ID" NUMBER(12,0), 
"MODULE_ITEM_CODE" NUMBER(3,0), 
"MODULE_ITEM_ID" NUMBER(12,0), 
"QUESTIONNAIRE_COMPLETED_FLAG" VARCHAR2(1 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE)
) 
/
CREATE TABLE "MITKC_QUESTIONNAIRE_ANSWER" 
(	"QUESTIONNAIRE_ANSWER_ID" NUMBER(12,0), 
"QUESTIONNAIRE_ANS_HEADER_ID" NUMBER(12,0), 
"QUESTION_ID" NUMBER(12,0), 
"ANSWER_NUMBER" NUMBER(3,0), 
"ANSWER" VARCHAR2(2000 BYTE), 
"ANSWER_LOOKUP_CODE" VARCHAR2(3 BYTE), 
"EXPLANATION" VARCHAR2(4000 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE)
)
/
CREATE TABLE "MITKC_QUESTIONNAIRE_ANSWER_ATT" 
(	"QUESTIONNAIRE_ANSWER_ATT_ID" NUMBER(12,0), 
"QUESTIONNAIRE_ANSWER_ID" NUMBER(12,0), 
"ATTACHMENT" BLOB, 
"FILE_NAME" VARCHAR2(150 BYTE), 
"CONTENT_TYPE" VARCHAR2(100 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE)
) 
/
CREATE TABLE "MITKC_QUESTIONNAIRE_QUESTION" 
(	"QUESTION_ID" NUMBER(12,0), 
"QUESTIONNAIRE_ID" NUMBER(12,0), 
"SORT_ORDER" NUMBER(3,0), 
"QUESTION" VARCHAR2(1000 BYTE), 
"DESCRIPTION" VARCHAR2(4000 BYTE), 
"HELP_LINK" VARCHAR2(200 BYTE), 
"ANSWER_TYPE" VARCHAR2(90 BYTE), 
"ANSWER_LENGTH" NUMBER(4,0), 
"NO_OF_ANSWERS" NUMBER(3,0), 
"LOOKUP_TYPE" VARCHAR2(30 BYTE), 
"LOOKUP_NAME" VARCHAR2(100 BYTE), 
"LOOKUP_FIELD" VARCHAR2(30 BYTE), 
"GROUP_NAME" VARCHAR2(60 BYTE), 
"HAS_CONDITION" VARCHAR2(1 BYTE), 
"UPDATE_TIMESTAMP" DATE, 
"UPDATE_USER" VARCHAR2(60 BYTE), 
"GROUP_LABEL" VARCHAR2(300 BYTE)
)
/
CREATE INDEX "MITKC_QNR_QSTN_CONDITION_IDX2" ON "MITKC_QNR_QUESTION_CONDITION" ("QUESTION_ID")
/
CREATE UNIQUE INDEX "PK_MITKC_QNR_QUESTION_CONDTN" ON "MITKC_QNR_QUESTION_CONDITION" ("QUESTION_CONDITION_ID") 
/
CREATE INDEX "MITKC_QNR_QUESTION_OPTION_IDX2" ON "MITKC_QNR_QUESTION_OPTION" ("QUESTION_ID")
/
CREATE UNIQUE INDEX "PK_MITKC_QNR_QUESTION_OPTION" ON "MITKC_QNR_QUESTION_OPTION" ("QUESTION_OPTION_ID")
/
CREATE UNIQUE INDEX "PK_MITKC_QUESTIONNAIRE" ON "MITKC_QUESTIONNAIRE" ("QUESTIONNAIRE_ID")
/
CREATE INDEX "MITKC_QNR_ANS_HEADER_IDX2" ON "MITKC_QUESTIONNAIRE_ANS_HEADER" ("MODULE_ITEM_ID", "MODULE_ITEM_CODE") 
/
CREATE UNIQUE INDEX "PK_QUESTIONNAIRE_ANS_HDR" ON "MITKC_QUESTIONNAIRE_ANS_HEADER" ("QUESTIONNAIRE_ANS_HEADER_ID") 
/
CREATE INDEX "MITKC_QNR_ANSWER_IDX2" ON "MITKC_QUESTIONNAIRE_ANSWER" ("QUESTIONNAIRE_ANS_HEADER_ID")
/
CREATE UNIQUE INDEX "PK_QUESTIONNAIRE_ANS" ON "MITKC_QUESTIONNAIRE_ANSWER" ("QUESTIONNAIRE_ANSWER_ID") 
/
CREATE INDEX "MITKC_QNR_ANSWER_ATT_IDX2" ON "MITKC_QUESTIONNAIRE_ANSWER_ATT" ("QUESTIONNAIRE_ANSWER_ID") 
/
CREATE UNIQUE INDEX "PK_QUESTIONNAIRE_ANS_ATT" ON "MITKC_QUESTIONNAIRE_ANSWER_ATT" ("QUESTIONNAIRE_ANSWER_ATT_ID") 
/
CREATE UNIQUE INDEX "PK_MITKC_QUESTIONNAIRE_QSTN" ON "MITKC_QUESTIONNAIRE_QUESTION" ("QUESTION_ID") 
/
CREATE INDEX "MITKC_QNR_QSTN_INDX2" ON "MITKC_QUESTIONNAIRE_QUESTION" ("QUESTIONNAIRE_ID")
/
ALTER TABLE "MITKC_QNR_QUESTION_CONDITION" ADD CONSTRAINT "PK_MITKC_QNR_QUESTION_CONDTN" PRIMARY KEY ("QUESTION_CONDITION_ID")
/
ALTER TABLE "MITKC_QNR_QUESTION_OPTION" ADD CONSTRAINT "PK_MITKC_QNR_QUESTION_OPTION" PRIMARY KEY ("QUESTION_OPTION_ID")
/



ALTER TABLE "MITKC_QUESTIONNAIRE" ADD CONSTRAINT "PK_MITKC_QUESTIONNAIRE" PRIMARY KEY ("QUESTIONNAIRE_ID")
/
ALTER TABLE "MITKC_QUESTIONNAIRE_ANS_HEADER" ADD CONSTRAINT "PK_QUESTIONNAIRE_ANS_HDR" PRIMARY KEY ("QUESTIONNAIRE_ANS_HEADER_ID")
/
ALTER TABLE "MITKC_QUESTIONNAIRE_ANSWER" ADD CONSTRAINT "PK_QUESTIONNAIRE_ANS" PRIMARY KEY ("QUESTIONNAIRE_ANSWER_ID")
/
ALTER TABLE "MITKC_QUESTIONNAIRE_ANSWER_ATT" ADD CONSTRAINT "PK_QUESTIONNAIRE_ANS_ATT" PRIMARY KEY ("QUESTIONNAIRE_ANSWER_ATT_ID")
/
ALTER TABLE "MITKC_QUESTIONNAIRE_QUESTION" ADD CONSTRAINT "PK_MITKC_QUESTIONNAIRE_QSTN" PRIMARY KEY ("QUESTION_ID")
/
ALTER TABLE "MITKC_QNR_QUESTION_CONDITION" ADD CONSTRAINT "FK1_MITKC_QNR_QUESTION_CONDTN" FOREIGN KEY ("QUESTION_ID")
REFERENCES "MITKC_QUESTIONNAIRE_QUESTION" ("QUESTION_ID") ENABLE
/
ALTER TABLE "MITKC_QNR_QUESTION_OPTION" ADD CONSTRAINT "FK1_MITKC_QNR_QUESTION_OPTION" FOREIGN KEY ("QUESTION_ID")
REFERENCES "MITKC_QUESTIONNAIRE_QUESTION" ("QUESTION_ID") ENABLE
/


ALTER TABLE "MITKC_QUESTIONNAIRE_ANS_HEADER" ADD CONSTRAINT "FK1_QUESTIONNAIRE_ANS_HDR" FOREIGN KEY ("QUESTIONNAIRE_ID")
REFERENCES "MITKC_QUESTIONNAIRE" ("QUESTIONNAIRE_ID") ENABLE
/
ALTER TABLE "MITKC_QUESTIONNAIRE_ANSWER" ADD CONSTRAINT "FK1_QUESTIONNAIRE_ANS" FOREIGN KEY ("QUESTIONNAIRE_ANS_HEADER_ID")
REFERENCES "MITKC_QUESTIONNAIRE_ANS_HEADER" ("QUESTIONNAIRE_ANS_HEADER_ID") ENABLE
/
ALTER TABLE "MITKC_QUESTIONNAIRE_ANSWER" ADD CONSTRAINT "FK2_QUESTIONNAIRE_ANS" FOREIGN KEY ("QUESTION_ID")
REFERENCES "MITKC_QUESTIONNAIRE_QUESTION" ("QUESTION_ID") ENABLE
/
ALTER TABLE "MITKC_QUESTIONNAIRE_ANSWER_ATT" ADD CONSTRAINT "FK1_QUESTIONNAIRE_ANS_ATT" FOREIGN KEY ("QUESTIONNAIRE_ANSWER_ID")
REFERENCES "MITKC_QUESTIONNAIRE_ANSWER" ("QUESTIONNAIRE_ANSWER_ID") ENABLE
/
ALTER TABLE "MITKC_QUESTIONNAIRE_QUESTION" ADD CONSTRAINT "FK1_MITKC_QUESTIONNAIRE_QSTN" FOREIGN KEY ("QUESTIONNAIRE_ID")
REFERENCES "MITKC_QUESTIONNAIRE" ("QUESTIONNAIRE_ID") ENABLE
/
CREATE SEQUENCE "SEQ_QUESTIONNAIRE_ANSWER_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 10001 NOCACHE  NOORDER  NOCYCLE
/
CREATE SEQUENCE "SEQ_QNR_ANS_HEADER_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 101 NOCACHE  NOORDER  NOCYCLE
/
CREATE SEQUENCE  "SEQ_QNR_ANSWER_ATT_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE
/
Insert into MITKC_QUESTIONNAIRE (QUESTIONNAIRE_ID,QUESTIONNAIRE,DESCRIPTION,UPDATE_TIMESTAMP,UPDATE_USER) values (1003,'Exempt Questionnaire',null,sysdate,user)
/
Insert into MITKC_QUESTIONNAIRE (QUESTIONNAIRE_ID,QUESTIONNAIRE,DESCRIPTION,UPDATE_TIMESTAMP,UPDATE_USER) values (1001,'First Questionnaire',null,sysdate,user)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (75,1001,75,'COI Questions (3 at most)',null,null,'Text',null,1,null,null,null,'G5',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (76,1001,76,'Has the outside organization provided you with an DUA for MIT to review?',null,null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (77,1001,77,'upload attachment',null,null,'Attachment',null,1,null,null,null,'G41',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (78,1001,78,'By submitting this request, you are agreeing to MIT''s strict marking requirements for confidential information',null,null,'Checkbox',null,1,null,null,null,'G5',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (79,1001,79,'Description of Data set / Description of Research Project / Funded Account?',null,null,'Text',null,1,null,null,null,'G6',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (80,1001,80,'Does this request relate to a funded account? ',null,null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (81,1001,81,'Account Name and #',null,null,'Text',null,1,null,null,null,'G42',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (82,1001,82,'Does the purpose of this NDA conflict with obligations MIT has to Sponsors already funding your research or to providers of materials or equipment you are using?',null,null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (83,1001,83,'Please describe the conflict',null,null,'Textarea',null,1,null,null,null,'G43',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (84,1001,84,'Do you intend to disclose the OO''s data set to third parties? ',null,null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (85,1001,85,'To Whom? ',null,null,'Text',null,1,null,null,null,'G44',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (86,1001,86,'PII/HIPPAA/Human subjects Q',null,null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (87,1001,87,'Do you have IRB Approval? ',null,null,'Radio',null,1,null,null,null,'G45','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (88,1001,88,'Please attach',null,null,'Attachment',null,1,null,null,null,'G46',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (89,1001,89,'Export Control Q',null,null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (90,1001,90,'Describe',null,null,'Textarea',null,1,null,null,null,'G47',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (91,1001,91,'COI Questions (3 at most)',null,null,'Text',null,1,null,null,null,'G6',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (92,1001,92,'Has the outside organization provided you with an DUA for MIT to review?',null,null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (93,1001,93,'upload attachment',null,null,'Attachment',null,1,null,null,null,'G48',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (94,1001,94,'By submitting this request, you are agreeing to MIT''s strict marking requirements for confidential information',null,null,'Checkbox',null,1,null,null,null,'G6',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (169,1003,1,'Does your study meet the Federal definition of research?','Research is defined as a systematic investigation, including research development, testing and evaluation, designed to develop or contribute to generalizable knowledge.<br/>
<br /><a href="https://www.hhs.gov/ohrp/regulations-and-policy/regulations/45-cfr-46/index.html" target="_blank">https://www.hhs.gov/ohrp/regulations-and-policy/regulations/45-cfr-46/index.html</a>',null,'Radio',null,1,null,null,null,'G0','Y',sysdate,user,'General')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (170,1003,2,'Does your research involve the use of human subjects?','Human subjects is defined as a living individual about whom an investigator conducting research obtains data through 1.) Intervention or interaction with the individual, or 2.) Identifiable private information.<br /><br /><a href="https://www.hhs.gov/ohrp/regulations-and-policy/regulations/45-cfr-46/index.html" target="_blank">https://www.hhs.gov/ohrp/regulations-and-policy/regulations/45-cfr-46/index.html</a> <br /><a href="https://www.hhs.gov/ohrp/regulations-and-policy/guidance/guidance-on-engagement-of-institutions/index.html" target="_blank">https://www.hhs.gov/ohrp/regulations-and-policy/guidance/guidance-on-engagement-of-institutions/index.html</a><br /><a href="https://www.hhs.gov/ohrp/regulations-and-policy/guidance/research-involving-coded-private-information/index.html" target="_blank">https://www.hhs.gov/ohrp/regulations-and-policy/guidance/research-involving-coded-private-information/index.html</a>',null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (171,1003,3,'Does your research include prisoners as subjects?','Prisoners are individuals involuntarily confined or detained in a penal institution including incarceration, commitment procedures that provide alternatives to criminal incarceration, and individuals detained pending arraignment, trial, or sentencing.',null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,'Prisoners')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (172,1003,4,' Is the inclusion of prisoners as subjects incidental?','Research activities are not intended to study prisoners specifically but will include the population more generally as part of the general population.',null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (173,1003,5,'Is your study conducted in a traditional educational setting (classroom, seminar, lecture, afterschool program, etc.) that involves normal educational practices?','See the Exempt Guidance document on our website for additional information.<br/>
<br/><a href="https://www.gpo.gov/fdsys/pkg/CFR-2017-title45-vol1/xml/CFR-2017-title45-vol1-part46.xml" target="_blank">https://www.gpo.gov/fdsys/pkg/CFR-2017-title45-vol1/xml/CFR-2017-title45-vol1-part46.xml</a>',null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,'Exempt 1')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (174,1003,6,'Will the research likely adversely impact students'' opportunity to learn?','Research should not adversely impact students'' opportunity to learn required educational content or interfere with assessments conducted by educators.',null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (175,1003,7,'Does your study include a benign behavioral intervention that includes data collection through verbal or written responses, or audiovisual recording?','Benign behavioral interventions are brief in duration, harmless, painless, not physically invasive, not likely to have a significant adverse and lasting impact on the subjects, and the investigator has no reason to believe the subject will find the interventions offensive or embarrassing.<br/>
<br/><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm" target="_blank">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G6','Y',sysdate,user,'Exempt 3')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (176,1003,8,'Does your subject population include only adults? ','Subject population is 18 years of age or older.',null,'Radio',null,1,null,null,null,'G7','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (177,1003,9,'Does your study involve deception?','Research activities involve deception if investigators provide false, incomplete or ambiguous information deliberately misleading participants.',null,'Radio',null,1,null,null,null,'G8','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (178,1003,10,'Is the data identifiable?','The identity of the subject is or may readily be ascertained by the investigator or through associated research data.',null,'Radio',null,1,null,null,null,'G9','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (179,1003,11,'Does disclosure of this data introduce risk or harm to the participant?','Any disclosure of the human subjects'' responses outside the research would not reasonably place the subjects at risk of criminal or civil liability or be damaging to the subjects'' financial standing, employability, educational advancement, or reputation.
<br/><br/><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm" target="_blank" rel="noopener">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G10','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL)
values (180,1003,12,'Will the subjects be aware that deception is involved in your study? ','Subjects prospectively agree to participate in research activities in which the subject is informed that he or she will be unaware of or misled regarding the purpose of the research. &nbsp'||';'||'
<br/><a href="https://www.gpo.gov/fdsys/pkg/CFR-2017-title45-vol1/xml/CFR-2017-title45-vol1-part46.xml#seqnum46.402" target="_blank" rel="noopener">https://www.gpo.gov/fdsys/pkg/CFR-2017-title45-vol1/xml/CFR-2017-title45-vol1-part46.xml#seqnum46.402</a>',null,'Radio',null,1,null,null,null,'G11','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (181,1003,13,'Is the data identifiable?','The identity of the subject is or may readily be ascertained by the investigator or through associated research data.',null,'Radio',null,1,null,null,null,'G12','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (182,1003,14,'Does disclosure of this data introduce risk or harm to the participant?','Any disclosure of the human subjects'' responses outside the research would not reasonably place the subjects at risk of criminal or civil liability or be damaging to the subjects'' financial standing, employability, educational advancement, or reputation.
<br/><br/><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm" target="_blank" rel="noopener">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G13','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (183,1003,15,'Does your study involve the use of surveys, interviews, educational tests, or observation of public behavior?','Research activities only include interactions involving educational testing, survey procedures, interview procedures or observation of public behavior.
<br /><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm"  target="_blank">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G14','Y',sysdate,user,'Exempt 2')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (184,1003,16,'Does your study population include children?','Study population is 17 years of age or younger.  ',null,'Radio',null,1,null,null,null,'G15','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (185,1003,17,'Is the data identifiable?','The identity of the subject is or may readily be ascertained by the investigator or through associated research data.',null,'Radio',null,1,null,null,null,'G16','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (186,1003,18,'Does disclosure of this data introduce risk or harm to the participant?','Any disclosure of the human subjects'' responses outside the research would not reasonably place the subjects at risk of criminal or civil liability or be damaging to the subjects'' financial standing, employability, educational advancement, or reputation.
<br/><br/><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm" target="_blank" rel="noopener">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G17','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (187,1003,19,'Does your study involve educational testing which is publically observable with no intervention?','Educational testing includes cognitive, diagnostic, aptitude, or achievement; and research does not involve interaction by an investigator.
<br/><a href="https://www.hhs.gov/ohrp/regulations-and-policy/guidance/research-involving-coded-private-information/index.html" target="_blank" rel="noopener">https://www.hhs.gov/ohrp/regulations-and-policy/guidance/research-involving-coded-private-information/index.html</a>',null,'Radio',null,1,null,null,null,'G18','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (188,1003,20,'Is the data identifiable?','The identity of the subject is or may readily be ascertained by the investigator or through associated research data.',null,'Radio',null,1,null,null,null,'G19','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (189,1003,21,'Does disclosure of this data introduce risk or harm to the participant?','Any disclosure of the human subjects'' responses outside the research would not reasonably place the subjects at risk of criminal or civil liability or be damaging to the subjects'' financial standing, employability, educational advancement, or reputation.
<br/><br/><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm" target="_blank" rel="noopener">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G20','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (190,1003,22,'Does your study involve the use of biospecimen or private information? This data must exist as of today.','Data must exist prior to the start of the research project and no new data collection can occur during the research.
<br/><a href="https://www.hhs.gov/ohrp/sachrp-committee/recommendations/attachment-b-december-12-2017/index.html" target="_blank" rel="noopener">https://www.hhs.gov/ohrp/sachrp-committee/recommendations/attachment-b-december-12-2017/index.html</a>',null,'Radio',null,1,null,null,null,'G21','Y',sysdate,user,'Exempt 4')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (191,1003,23,'Are the biospecimen and/or pre-existing dataset(s) publically available?','Publically available means data is readily accessible by the public, without prior authorization, login, or agreement and subjects do not have a reasonable expectation of privacy.',null,'Radio',null,1,null,null,null,'G22','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (192,1003,24,'Is the data recorded in such a way that the identity of the human subject cannot be readily ascertained directly or through identifiers linked to the subjects and the investigator will not contact or re-identify subjects?','Data received by investigators must be de-identified and investigators cannot have access to any information that may link subjects to the data.',null,'Radio',null,1,null,null,null,'G23','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (193,1003,25,'Is the data collected on behalf of a government agency, or is the research involving the use of this secondary data being conducted by a Federal agency?','See the Exempt Guidance document on our website for additional information.',null,'Radio',null,1,null,null,null,'G24','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (194,1003,26,'Is your study an evaluation of taste, food quality, or consumer acceptance?','Study activities are limited to taste testing foods and designed to evaluate subjects&rsquo; preference and not to measure health outcomes. This excludes research involving supplements or vitamins.<br/>
<br/><a href="https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm" target="_blank">https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/html/2017-01058.htm</a>',null,'Radio',null,1,null,null,null,'G25','Y',sysdate,user,'Exempt 6')
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (195,1003,27,'Does the food being evaluated contain any additives?','Additives are substances added to&nbsp;food&nbsp;to preserve flavor or enhance its taste.',null,'Radio',null,1,null,null,null,'G26','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (196,1003,28,'Does the food contain additives that are above the level found to be safe by the FDA, EPA, or the Food Safety and Inspection Service of the U.S. Department of Agriculture? ','See the Exempt Guidance document on our website for additional information.',null,'Radio',null,1,null,null,null,'G27',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (1,1001,1,'Please select the description that best describes your request from the following',null,null,'Radio',null,1,null,null,null,'G0','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (2,1001,2,'What is the Substantive Nature of the Confidential Information the Outside Organization Intends to Disclose?',null,null,'Text',null,1,null,null,null,'G1',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (3,1001,3,'Does this request relate to a funded account? ',null,null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (4,1001,4,'Account Name and #',null,null,'Text',null,1,null,null,null,'G7',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (5,1001,5,'Does the purpose of this NDA conflict with obligations MIT has to Sponsors already funding your research or to providers of materials or equipment you are using?',null,null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (6,1001,6,'Please describe the conflict',null,null,'Textarea',null,1,null,null,null,'G8',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (7,1001,7,'Do you intend to disclose the OO''s confidential information to third parties? ',null,null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (8,1001,8,'To Whom? ',null,null,'Text',null,1,null,null,null,'G9',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (9,1001,9,'PII/HIPPAA/Human subjects Q',null,null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (10,1001,10,'Do you have IRB Approval? ',null,null,'Radio',null,1,null,null,null,'G10','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (11,1001,11,'Please attach',null,null,'Attachment',null,1,null,null,null,'G11',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (12,1001,12,'Export Control Q',null,null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (13,1001,13,'Describe',null,null,'Textarea',null,1,null,null,null,'G12',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (14,1001,14,'Has the outside organization provided you with an NDA for MIT to review?',null,null,'Radio',null,1,null,null,null,'G1','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (15,1001,15,'upload attachment',null,null,'Attachment',null,1,null,null,null,'G13',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (16,1001,16,'By submitting this request, you are agreeing to MIT''s strict marking requirements for confidential information',null,null,'Checkbox',null,1,null,null,null,'G1',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (17,1001,17,'What is the Substantive Nature of the Confidential Information the Outside Organization Intends to Disclose?',null,null,'Text',null,1,null,null,null,'G2',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (18,1001,18,'Does this request relate to a funded account? ',null,null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (19,1001,19,'Account Name and #',null,null,'Text',null,1,null,null,null,'G14',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (20,1001,20,'Does the purpose of this NDA conflict with obligations MIT has to Sponsors already funding your research or to providers of materials or equipment you are using?',null,null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (21,1001,21,'Please describe the conflict',null,null,'Textarea',null,1,null,null,null,'G15',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (22,1001,22,'Do you intend to allow the OO''s to disclose your MIT confidential information to third parties? ',null,null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (23,1001,23,'To Whom? ',null,null,'Text',null,1,null,null,null,'G16',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (24,1001,24,'PII/HIPPAA/Human subjects Q',null,null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (25,1001,25,'Do you have IRB Approval? ',null,null,'Radio',null,1,null,null,null,'G17','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (26,1001,26,'Please attach',null,null,'Attachment',null,1,null,null,null,'G18',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (27,1001,27,'Export Control Q',null,null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (28,1001,28,'Describe',null,null,'Textarea',null,1,null,null,null,'G19',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (29,1001,29,'Has the outside organization provided you with an NDA for MIT to review?',null,null,'Radio',null,1,null,null,null,'G2','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (30,1001,30,'upload attachment',null,null,'Attachment',null,1,null,null,null,'G20',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (31,1001,31,'By submitting this request, you are agreeing to MIT''s strict marking requirements for confidential information',null,null,'Checkbox',null,1,null,null,null,'G2',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (32,1001,32,'What is the Substantive Nature of the Confidential Information the Outside Organization Intends to Disclose?',null,null,'Text',null,1,null,null,null,'G3',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (33,1001,33,'Does this request relate to a funded account? ',null,null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (34,1001,34,'Account Name and #',null,null,'Text',null,1,null,null,null,'G21',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (35,1001,35,'Does the purpose of this NDA conflict with obligations MIT has to Sponsors already funding your research or to providers of materials or equipment you are using?',null,null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (36,1001,36,'Please describe the conflict',null,null,'Textarea',null,1,null,null,null,'G22',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (37,1001,37,'Do you intend to disclose the OO''s confidential information to third parties? ',null,null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (38,1001,38,'To Whom? ',null,null,'Text',null,1,null,null,null,'G23',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (39,1001,39,'PII/HIPPAA/Human subjects Q',null,null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (40,1001,40,'Do you have IRB Approval? ',null,null,'Radio',null,1,null,null,null,'G24','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (41,1001,41,'Please attach',null,null,'Attachment',null,1,null,null,null,'G25',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (42,1001,42,'Export Control Q',null,null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (43,1001,43,'Describe',null,null,'Textarea',null,1,null,null,null,'G26',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (44,1001,44,'Has the outside organization provided you with an NDA for MIT to review?',null,null,'Radio',null,1,null,null,null,'G3','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (45,1001,45,'upload attachment',null,null,'Attachment',null,1,null,null,null,'G27',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (46,1001,46,'By submitting this request, you are agreeing to MIT''s strict marking requirements for confidential information',null,null,'Checkbox',null,1,null,null,null,'G3',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (47,1001,47,'Description of Data set / Description of Research Project / Funded Account?',null,null,'Text',null,1,null,null,null,'G4',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (48,1001,48,'Does this request relate to a funded account? ',null,null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (49,1001,49,'Account Name and #',null,null,'Text',null,1,null,null,null,'G28',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (50,1001,50,'Does the purpose of this NDA conflict with obligations MIT has to Sponsors already funding your research or to providers of materials or equipment you are using?',null,null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (51,1001,51,'Please describe the conflict',null,null,'Textarea',null,1,null,null,null,'G29',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (52,1001,52,'Do you intend to disclose the OO''s data set to third parties? ',null,null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (53,1001,53,'To Whom? ',null,null,'Text',null,1,null,null,null,'G30',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (54,1001,54,'PII/HIPPAA/Human subjects Q',null,null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (55,1001,55,'Do you have IRB Approval? ',null,null,'Radio',null,1,null,null,null,'G31','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (56,1001,56,'Please attach',null,null,'Attachment',null,1,null,null,null,'G32',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (57,1001,57,'Export Control Q',null,null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (58,1001,58,'Describe',null,null,'Textarea',null,1,null,null,null,'G33',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (59,1001,59,'COI Questions (3 at most)',null,null,'Text',null,1,null,null,null,'G4',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (60,1001,60,'Has the outside organization provided you with an DUA for MIT to review?',null,null,'Radio',null,1,null,null,null,'G4','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (61,1001,61,'upload attachment',null,null,'Attachment',null,1,null,null,null,'G34',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (62,1001,62,'By submitting this request, you are agreeing to MIT''s strict marking requirements for confidential information',null,null,'Checkbox',null,1,null,null,null,'G4',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (63,1001,63,'Description of Data set / Description of Research Project / Funded Account?',null,null,'Text',null,1,null,null,null,'G5',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (64,1001,64,'Does this request relate to a funded account? ',null,null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (65,1001,65,'Account Name and #',null,null,'Text',null,1,null,null,null,'G35',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (66,1001,66,'Does the purpose of this NDA conflict with obligations MIT has to Sponsors already funding your research or to providers of materials or equipment you are using?',null,null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (67,1001,67,'Please describe the conflict',null,null,'Textarea',null,1,null,null,null,'G36',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (68,1001,68,'Do you intend to allow the OO''s to disclose your data set to third parties? ',null,null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (69,1001,69,'To Whom? ',null,null,'Text',null,1,null,null,null,'G37',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (70,1001,70,'PII/HIPPAA/Human subjects Q',null,null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (71,1001,71,'Do you have IRB Approval? ',null,null,'Radio',null,1,null,null,null,'G38','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (72,1001,72,'Please attach',null,null,'Attachment',null,1,null,null,null,'G39',null,sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (73,1001,73,'Export Control Q',null,null,'Radio',null,1,null,null,null,'G5','Y',sysdate,user,null)
/
Insert into MITKC_QUESTIONNAIRE_QUESTION (QUESTION_ID,QUESTIONNAIRE_ID,SORT_ORDER,QUESTION,DESCRIPTION,HELP_LINK,ANSWER_TYPE,ANSWER_LENGTH,NO_OF_ANSWERS,LOOKUP_TYPE,LOOKUP_NAME,LOOKUP_FIELD,GROUP_NAME,HAS_CONDITION,UPDATE_TIMESTAMP,UPDATE_USER,GROUP_LABEL) values (74,1001,74,'Describe',null,null,'Textarea',null,1,null,null,null,'G40',null,sysdate,user,null)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (238,169,'EQUALS','Yes','G1',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (239,170,'EQUALS','Yes','G2',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (240,171,'EQUALS','Yes','G3',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (241,171,'EQUALS','No','G4',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (242,172,'EQUALS','Yes','G4',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (243,172,'EQUALS','No','G4',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (244,173,'EQUALS','Yes','G5',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (245,173,'EQUALS','No','G6',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (246,174,'EQUALS','Yes','G6',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (247,174,'EQUALS','No','G6',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (248,175,'EQUALS','Yes','G7',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (249,175,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (250,176,'EQUALS','Yes','G8',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (251,176,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (252,177,'EQUALS','Yes','G11',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (253,177,'EQUALS','No','G9',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (254,178,'EQUALS','Yes','G10',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (255,178,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (256,179,'EQUALS','Yes','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (257,179,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (258,180,'EQUALS','Yes','G12',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (259,180,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (260,181,'EQUALS','Yes','G13',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (261,181,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (262,182,'EQUALS','Yes','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (263,182,'EQUALS','No','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (264,183,'EQUALS','Yes','G15',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (265,183,'EQUALS','No','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (266,184,'EQUALS','Yes','G18',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (267,184,'EQUALS','No','G16',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (268,185,'EQUALS','Yes','G17',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (269,185,'EQUALS','No','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (270,186,'EQUALS','Yes','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (271,186,'EQUALS','No','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (272,187,'EQUALS','Yes','G19',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (273,187,'EQUALS','No','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (274,188,'EQUALS','Yes','G20',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (275,188,'EQUALS','No','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (276,189,'EQUALS','Yes','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (277,189,'EQUALS','No','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (278,190,'EQUALS','Yes','G22',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (279,190,'EQUALS','No','G25',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (280,191,'EQUALS','Yes','G25',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (281,191,'EQUALS','No','G23',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (282,192,'EQUALS','Yes','G25',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (283,192,'EQUALS','No','G24',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (284,193,'EQUALS','Yes','G25',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (285,193,'EQUALS','No','G25',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (286,194,'EQUALS','Yes','G26',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (287,195,'EQUALS','Yes','G27',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (1,1,'EQUALS','Only to receive or have access to confidential information','G1',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (2,1,'EQUALS','Only to provide confidential information to an outside organization','G2',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (3,1,'EQUALS','Both to receive and provide or have access to confidential information','G3',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (4,1,'EQUALS','Only to receive a discreet confidential data set for use in my research','G4',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (5,1,'EQUALS','Only to provide a discreet confidential data set from my research to and outside organization','G5',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (6,1,'EQUALS','Both to receive and provide a discreet confidential data set for use in my research','G6',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (7,3,'EQUALS','Yes','G7',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (8,5,'EQUALS','Yes','G8',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (9,7,'EQUALS','Yes','G9',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (10,9,'EQUALS','Yes','G10',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (11,10,'EQUALS','Yes','G11',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (12,12,'EQUALS','Yes','G12',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (13,14,'EQUALS','Yes','G13',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (14,18,'EQUALS','Yes','G14',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (15,20,'EQUALS','Yes','G15',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (16,22,'EQUALS','Yes','G16',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (17,24,'EQUALS','Yes','G17',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (18,25,'EQUALS','Yes','G18',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (19,27,'EQUALS','Yes','G19',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (20,29,'EQUALS','Yes','G20',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (21,33,'EQUALS','Yes','G21',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (22,35,'EQUALS','Yes','G22',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (23,37,'EQUALS','Yes','G23',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (24,39,'EQUALS','Yes','G24',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (25,40,'EQUALS','Yes','G25',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (26,42,'EQUALS','Yes','G26',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (27,44,'EQUALS','Yes','G27',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (28,48,'EQUALS','Yes','G28',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (29,50,'EQUALS','Yes','G29',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (30,52,'EQUALS','Yes','G30',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (31,54,'EQUALS','Yes','G31',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (32,55,'EQUALS','Yes','G32',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (33,57,'EQUALS','Yes','G33',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (34,60,'EQUALS','Yes','G34',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (35,64,'EQUALS','Yes','G35',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (36,66,'EQUALS','Yes','G36',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (37,68,'EQUALS','Yes','G37',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (38,70,'EQUALS','Yes','G38',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (39,71,'EQUALS','Yes','G39',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (40,73,'EQUALS','Yes','G40',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (41,76,'EQUALS','Yes','G41',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (42,80,'EQUALS','Yes','G42',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (43,82,'EQUALS','Yes','G43',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (44,84,'EQUALS','Yes','G44',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (45,86,'EQUALS','Yes','G45',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (46,87,'EQUALS','Yes','G46',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (47,89,'EQUALS','Yes','G47',sysdate,user)
/
Insert into MITKC_QNR_QUESTION_CONDITION (QUESTION_CONDITION_ID,QUESTION_ID,CONDITION_TYPE,CONDITION_VALUE,GROUP_NAME,UPDATE_TIMESTAMP,UPDATE_USER) values (48,92,'EQUALS','Yes','G48',sysdate,user)
/

Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (337,169,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (338,169,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (339,170,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (340,170,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (341,171,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (342,171,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (343,172,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (344,172,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (345,173,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (346,173,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (347,174,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (348,174,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (349,175,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (350,175,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (351,176,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (352,176,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (353,177,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (354,177,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (355,178,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (356,178,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (357,179,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (358,179,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (359,180,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (360,180,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (361,181,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (362,181,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (363,182,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (364,182,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (365,183,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (366,183,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (367,184,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (368,184,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (369,185,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (370,185,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (371,186,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (372,186,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (373,187,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (374,187,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (375,188,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (376,188,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (377,189,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (378,189,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (379,190,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (380,190,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (381,191,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (382,191,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (383,192,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (384,192,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (385,193,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (386,193,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (387,194,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (388,194,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (389,195,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (390,195,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (391,196,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (392,196,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (1,1,1,'Only to receive or have access to confidential information',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (2,1,2,'Only to provide confidential information to an outside organization',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (3,1,3,'Both to receive and provide or have access to confidential information',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (4,1,4,'Only to receive a discreet confidential data set for use in my research',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (5,1,5,'Only to provide a discreet confidential data set from my research to and outside organization',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (6,1,6,'Both to receive and provide a discreet confidential data set for use in my research',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (7,3,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (8,3,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (9,5,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (10,5,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (11,7,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (12,7,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (13,9,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (14,9,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (15,10,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (16,10,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (17,12,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (18,12,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (19,14,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (20,14,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (21,16,1,'Yes, I Agree',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (23,18,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (24,18,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (25,20,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (26,20,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (27,22,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (28,22,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (29,24,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (30,24,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (31,25,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (32,25,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (33,27,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (34,27,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (35,29,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (36,29,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (37,31,1,'Yes, I Agree',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (39,33,2,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (40,33,1,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (41,35,2,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (42,35,1,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (43,37,2,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (44,37,1,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (45,39,2,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (46,39,1,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (47,40,2,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (48,40,1,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (49,42,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (50,42,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (51,44,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (52,44,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (53,46,1,'Yes, I Agree',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (55,48,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (56,48,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (57,50,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (58,50,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (59,52,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (60,52,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (61,54,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (62,54,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (63,55,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (64,55,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (65,57,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (66,57,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (67,60,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (68,60,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (69,62,1,'Yes, I Agree',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (71,64,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (72,64,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (73,66,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (74,66,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (75,68,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (76,68,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (77,70,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (78,70,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (79,71,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (80,71,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (81,73,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (82,73,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (83,76,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (84,76,1,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (85,78,2,'Yes, I Agree',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (87,80,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (88,80,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (89,82,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (90,82,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (91,84,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (92,84,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (93,86,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (94,86,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (95,87,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (96,87,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (97,89,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (98,89,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (99,92,1,'Yes',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (100,92,2,'No',null,null,sysdate,user)
/
Insert into MITKC_QNR_QUESTION_OPTION (QUESTION_OPTION_ID,QUESTION_ID,OPTION_NUMBER,OPTION_LABEL,REQUIRE_EXPLANATION,EXPLANTION_LABEL,UPDATE_TIMESTAMP,UPDATE_USER) values (101,94,1,'Yes, I Agree',null,null,sysdate,user)
/
commit
/
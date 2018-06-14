CREATE TABLE MITKC_IRB_EXEMPT_FORM_STATUS(
"EXEMPT_STATUS_CODE" VARCHAR2(3 BYTE), 
"DESCRIPTION" VARCHAR2(200 BYTE) NOT NULL ENABLE, 
"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
"UPDATE_USER" VARCHAR2(60 BYTE) NOT NULL ENABLE, 
CONSTRAINT "MITKC_IRB_EXEMPT_FORM_STAT_PK" PRIMARY KEY ("EXEMPT_STATUS_CODE")
)
/
INSERT INTO "MITKC_IRB_EXEMPT_FORM_STATUS" (EXEMPT_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('1', 'In Progress', sysdate, user)
/
INSERT INTO "MITKC_IRB_EXEMPT_FORM_STATUS" (EXEMPT_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) VALUES ('2', 'Submitted', sysdate, user)
/
CREATE TABLE "MITKC_IRB_PERSON_EXEMPT_FORM" 
(	
"IRB_PERSON_EXEMPT_FORM_ID"		NUMBER(12,0), 
"PERSON_ID"						VARCHAR2(40 BYTE), 
"PERSON_NAME" 					VARCHAR2(90 BYTE), 
"EXEMPT_STATUS_CODE"			VARCHAR2(3 BYTE),
"IS_EXEMPT_GRANTED"				VARCHAR2(1),
"EXEMPT_FORM_NUMBER" 			NUMBER(4,0), 
"EXEMPT_TITLE"					VARCHAR2(200), 
"QUESTIONNAIRE_ANS_HEADER_ID" 	NUMBER(12,0),
"UPDATE_TIMESTAMP" 				DATE NOT NULL, 
"UPDATE_USER" 					VARCHAR2(60 BYTE) NOT NULL
)
/
CREATE UNIQUE INDEX "MITKC_IRB_PERSON_EXEMPT_PK" ON "MITKC_IRB_PERSON_EXEMPT_FORM" ("IRB_PERSON_EXEMPT_FORM_ID") 
/
ALTER TABLE "MITKC_IRB_PERSON_EXEMPT_FORM" ADD CONSTRAINT "MITKC_IRB_PERSON_EXEMPT_PK" PRIMARY KEY ("IRB_PERSON_EXEMPT_FORM_ID")
/
ALTER TABLE "MITKC_IRB_PERSON_EXEMPT_FORM" ADD CONSTRAINT "MITKC_IRB_PERSON_EXEMPT_FK2" FOREIGN KEY ("EXEMPT_STATUS_CODE")
REFERENCES "MITKC_IRB_EXEMPT_FORM_STATUS" ("EXEMPT_STATUS_CODE") ENABLE
/
CREATE SEQUENCE  "SEQ_IRB_PERSON_EXEMPT_FORM_ID"  
MINVALUE 1 
MAXVALUE 999999999999 
INCREMENT BY 1
START WITH 1
NOCACHE 
NOORDER 
NOCYCLE
/
create or replace PACKAGE pkg_mitkc_mail_generic as

  FUNCTION fn_send_notification (
        av_award_budget_header_id IN mitkc_ab_header.award_budget_header_id%TYPE,
        av_comments IN CLOB,
        av_notification_type_id IN mitkc_notification_type.notification_number%TYPE
         )RETURN NUMBER;

  PROCEDURE	send_mail(
        av_recipients IN CLOB,
        av_copy_to   IN  VARCHAR2,
        av_reply_to  IN  VARCHAR2,
        av_subject IN CLOB,
        av_message IN CLOB,
        av_attach_blob IN BLOB, 
        av_attach_name IN VARCHAR2);

  PROCEDURE  insert_notification_log(
        av_notification_type_id IN mitkc_notification_log.notification_number%TYPE,
        av_request_id IN mitkc_notification_log.module_item_key%TYPE,
        av_to_user_email_id IN mitkc_notification_log.to_user_email_id%TYPE,
        av_cc_email_id IN mitkc_notification_log.cc_email_id%TYPE,
        av_subject IN mitkc_notification_log.subject%TYPE,
        av_message IN mitkc_notification_log.message%TYPE,
        av_notification_send_date IN DATE,
        ls_exception IN VARCHAR2
        );

 FUNCTION fn_send_nda_notification (
		   av_nda_request_id IN mitkc_nda_header.NDA_REQUEST_ID%TYPE,
		   av_notification_type_id IN mitkc_notification_type.notification_number%TYPE,
		   av_action_log_id IN MITKC_NDA_ACTION_LOG.NDA_ACTION_LOG_ID%TYPE,
           av_attach_blob IN BLOB, 
           av_attach_name IN VARCHAR2
		   )RETURN NUMBER;

  PROCEDURE get_nda_replace_custom_field (
         av_nda_request_id IN mitkc_nda_header.NDA_REQUEST_ID%TYPE, 
         av_subject IN OUT mitkc_notification_type.subject%TYPE,
         av_message IN OUT mitkc_notification_type.message%TYPE,
		 av_action_log_id IN MITKC_NDA_ACTION_LOG.NDA_ACTION_LOG_ID%TYPE
         );
  FUNCTION fn_send_notification (
   av_module_item_id IN NUMBER,
   av_comments IN CLOB,
   av_person_id IN VARCHAR2,
   av_notification_number IN mitkc_notification_type.notification_number%TYPE
   )RETURN NUMBER;

  FUNCTION fn_send_irb_notification (
    av_module_item_id 		  IN NUMBER,
	av_comments 	  		  IN CLOB,
	av_sender_person_id 	  IN VARCHAR2,
	av_notification_type	  IN VARCHAR2,
	av_notification_number 	  IN mitkc_notification_type.notification_number%TYPE
   )RETURN NUMBER;  
END;
/
create or replace PACKAGE BODY pkg_mitkc_mail_generic  AS

  FUNCTION fn_send_notification (
         av_award_budget_header_id IN mitkc_ab_header.award_budget_header_id%TYPE,
         av_comments IN CLOB,
         av_notification_type_id IN mitkc_notification_type.notification_number%TYPE
         )RETURN NUMBER
  IS

  ls_mitkc_ab_app_url krcr_parm_t.val%TYPE;
  ll_subject mitkc_notification_type.subject%TYPE;
  ll_message mitkc_notification_type.message%TYPE;
  ls_award_number award.award_number%TYPE;
  li_version_number mitkc_ab_header.version_number%TYPE;
  ls_status mitkc_ab_status.description%TYPE;
  ls_account_number award.account_number%TYPE;
  ls_title award.title%TYPE;
  ls_budget_start_date mitkc_ab_header.start_date%TYPE;
  ls_budget_end_date mitkc_ab_header.end_date%TYPE;
  li_total_cost mitkc_ab_header.total_cost%TYPE;
  ls_recipients CLOB;
  ls_notification_send_date DATE;
  ls_exception VARCHAR2(1000) := NULL;
  ls_rejected_user_name VARCHAR2(160);
  ls_create_user_name VARCHAR2(160);
  ls_approver_user_name VARCHAR2(160);


  BEGIN

      BEGIN

        SELECT val
        INTO ls_mitkc_ab_app_url
        FROM krcr_parm_t
        WHERE UPPER(parm_nm) = 'MITKC_AB_APP_URL'
        AND nmspc_cd = 'KC-GEN';


        EXCEPTION
        WHEN OTHERS THEN
        ls_exception := 'Error Generating Email. Parameter MITKC_AB_APP_URL not found in parameter table';

      END;

      IF av_notification_type_id = 101 THEN    -- To next approver in the workflow.

          BEGIN

            SELECT subject, message
            INTO ll_subject, ll_message
            FROM mitkc_notification_type
            WHERE notification_number = 101;

            EXCEPTION
            WHEN OTHERS THEN
            ls_exception := 'Error in fetching subject and message from mitkc_notification_type';

          END;

          BEGIN

            SELECT abh.award_number,abh.version_number,abh.start_date,abh.end_date,
            abh.total_cost,abs.description,aw.account_number,aw.title
            INTO ls_award_number, li_version_number, ls_budget_start_date, ls_budget_end_date,
            li_total_cost, ls_status, ls_account_number, ls_title
            FROM mitkc_ab_header abh
            INNER JOIN award aw ON abh.award_number = aw.award_number
            INNER JOIN mitkc_ab_status abs ON abh.budget_status_code = abs.award_budget_status_code
            WHERE abh.award_budget_header_id = av_award_budget_header_id
            AND aw.award_sequence_status = 'ACTIVE';

          EXCEPTION
          WHEN OTHERS THEN
          ls_exception := 'Error in fetching message contents';

          END;


          BEGIN

            FOR rec in(SELECT ROWNUM rw, email_addr FROM(SELECT distinct ket.email_addr
            FROM mitkc_workflow_detail mwd
            INNER JOIN mitkc_workflow mw ON mwd.workflow_id = mw.workflow_id
            INNER JOIN krim_prncpl_t kpt ON mwd.approver_person_id = kpt.prncpl_id
            INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND UPPER(mwd.approval_status) = 'W'
            AND mw.module_item_id = av_award_budget_header_id))

            LOOP

              IF rec.rw = 1  THEN

                ls_recipients := rec.email_addr;

              ELSE

                ls_recipients := ls_recipients||','|| rec.email_addr;

              END IF;

            END LOOP;

            EXCEPTION WHEN OTHERS THEN
            ls_exception := 'Error in fetching recipient email address';

          END;

      ELSIF av_notification_type_id = 102 THEN -- To submitter when budget is rejected

          BEGIN

            SELECT subject, message
            INTO ll_subject, ll_message
            FROM mitkc_notification_type
            WHERE notification_number = 102;

            EXCEPTION WHEN OTHERS THEN
            ls_exception := 'Error in fetching subject and message from mitkc_notification_type';

          END;

          BEGIN

            SELECT abh.award_number,abh.version_number,abh.start_date,abh.end_date,
            abh.total_cost,abs.description,aw.account_number,aw.title
            INTO ls_award_number, li_version_number, ls_budget_start_date, ls_budget_end_date,
            li_total_cost, ls_status, ls_account_number, ls_title
            FROM mitkc_ab_header abh
            INNER JOIN award aw ON abh.award_number = aw.award_number
            INNER JOIN mitkc_ab_status abs ON abh.budget_status_code = abs.award_budget_status_code
            WHERE abh.award_budget_header_id = av_award_budget_header_id
            AND aw.award_sequence_status = 'ACTIVE';

            EXCEPTION WHEN OTHERS THEN
            ls_exception :=  'Error in fetching message contents';

          END;

          BEGIN

            SELECT full_name INTO ls_rejected_user_name FROM(SELECT decode(kent.middle_nm,NULL,kent.last_nm||', '||kent.first_nm,kent.last_nm||', '||kent.first_nm||' '||kent.middle_nm) full_name
            FROM mitkc_workflow_detail mwd
            INNER JOIN mitkc_workflow mw ON mwd.workflow_id = mw.workflow_id
            INNER JOIN krim_prncpl_t kpt ON mwd.approver_person_id = kpt.prncpl_id
            INNER JOIN krim_entity_nm_t kent ON kpt.entity_id = kent.entity_id
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND UPPER(mwd.approval_status) = 'R'
            AND mw.module_item_id = av_award_budget_header_id);

            EXCEPTION WHEN OTHERS THEN
            ls_exception :=  'Error in fetching message contents';

          END;

          BEGIN

            SELECT ket.email_addr
            INTO ls_recipients
            FROM krim_entity_email_t ket
            INNER JOIN krim_prncpl_t kpt ON kpt.entity_id = ket.entity_id
            INNER JOIN mitkc_workflow mw ON kpt.prncpl_id = mw.workflow_start_person
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND mw.module_item_id = av_award_budget_header_id;

            EXCEPTION WHEN OTHERS THEN
            ls_exception := 'Error in fetching recipient email address';

          END;

        ELSIF av_notification_type_id = 103 THEN    -- To approvers when award budget is rejected by another person.

          BEGIN

            SELECT subject, message
            INTO ll_subject, ll_message
            FROM mitkc_notification_type
            WHERE notification_number = 103;

            EXCEPTION
            WHEN OTHERS THEN
            ls_exception := 'Error in fetching subject and message from mitkc_notification_type';

          END;

          BEGIN

            SELECT abh.award_number,abh.version_number,abh.start_date,abh.end_date,
            abh.total_cost,abh.create_user_name,abs.description,aw.account_number,aw.title
            INTO ls_award_number, li_version_number, ls_budget_start_date, ls_budget_end_date,
            li_total_cost, ls_create_user_name, ls_status, ls_account_number, ls_title
            FROM mitkc_ab_header abh
            INNER JOIN award aw ON abh.award_number = aw.award_number
            INNER JOIN mitkc_ab_status abs ON abh.budget_status_code = abs.award_budget_status_code
            WHERE abh.award_budget_header_id = av_award_budget_header_id
            AND aw.award_sequence_status = 'ACTIVE';

          EXCEPTION
          WHEN OTHERS THEN
          ls_exception := 'Error in fetching message contents';

          END;

		      ls_create_user_name := ls_create_user_name||'''s';

           BEGIN

            SELECT full_name INTO ls_approver_user_name FROM(SELECT decode(kent.middle_nm,NULL,kent.last_nm||', '||kent.first_nm,kent.last_nm||', '||kent.first_nm||' '||kent.middle_nm) full_name
            FROM mitkc_workflow_detail mwd
            INNER JOIN mitkc_workflow mw ON mwd.workflow_id = mw.workflow_id
            INNER JOIN krim_prncpl_t kpt ON mwd.approver_person_id = kpt.prncpl_id
            INNER JOIN krim_entity_nm_t kent ON kpt.entity_id = kent.entity_id
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND UPPER(mwd.approval_status) = 'R'
            AND mw.module_item_id = av_award_budget_header_id);

            EXCEPTION WHEN OTHERS THEN
            ls_exception :=  'Error in fetching message contents';

          END;

          BEGIN

            FOR rec in(SELECT ROWNUM rw, email_addr FROM(SELECT distinct ket.email_addr
            FROM mitkc_workflow_detail mwd
            INNER JOIN mitkc_workflow mw ON mwd.workflow_id = mw.workflow_id
            INNER JOIN krim_prncpl_t kpt ON mwd.approver_person_id = kpt.prncpl_id
            INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND UPPER(mwd.approval_status) = 'J'
            AND mw.module_item_id = av_award_budget_header_id))

            LOOP

              IF rec.rw = 1  THEN

                ls_recipients := rec.email_addr;

              ELSE

                ls_recipients := ls_recipients||','|| rec.email_addr;

              END IF;

            END LOOP;

            EXCEPTION WHEN OTHERS THEN
            ls_exception := 'Error in fetching recipient email address';

          END;

       ELSIF av_notification_type_id = 104 THEN    -- To approvers when award budget is approved by another person.

          BEGIN

            SELECT subject, message
            INTO ll_subject, ll_message
            FROM mitkc_notification_type
            WHERE notification_number = 104;

            EXCEPTION
            WHEN OTHERS THEN
            ls_exception := 'Error in fetching subject and message from mitkc_notification_type';

          END;

          BEGIN

            SELECT abh.award_number,abh.version_number,abh.start_date,abh.end_date,
            abh.total_cost,abh.create_user_name,abs.description,aw.account_number,aw.title
            INTO ls_award_number, li_version_number, ls_budget_start_date, ls_budget_end_date,
            li_total_cost, ls_create_user_name, ls_status, ls_account_number, ls_title
            FROM mitkc_ab_header abh
            INNER JOIN award aw ON abh.award_number = aw.award_number
            INNER JOIN mitkc_ab_status abs ON abh.budget_status_code = abs.award_budget_status_code
            WHERE abh.award_budget_header_id = av_award_budget_header_id
            AND aw.award_sequence_status = 'ACTIVE';

          EXCEPTION
          WHEN OTHERS THEN
          ls_exception := 'Error in fetching message contents';

          END;

		      ls_create_user_name := ls_create_user_name||'''s';

           BEGIN

            SELECT full_name INTO ls_approver_user_name FROM(SELECT decode(kent.middle_nm,NULL,kent.last_nm||', '||kent.first_nm,kent.last_nm||', '||kent.first_nm||' '||kent.middle_nm) full_name
            FROM mitkc_workflow_detail mwd
            INNER JOIN mitkc_workflow mw ON mwd.workflow_id = mw.workflow_id
            INNER JOIN krim_prncpl_t kpt ON mwd.approver_person_id = kpt.prncpl_id
            INNER JOIN krim_entity_nm_t kent ON kpt.entity_id = kent.entity_id
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND UPPER(mwd.approval_status) = 'A'
            AND mw.module_item_id = av_award_budget_header_id);

            EXCEPTION WHEN OTHERS THEN
            ls_exception :=  'Error in fetching message contents';

          END;

          BEGIN

            FOR rec in(SELECT ROWNUM rw, email_addr FROM(SELECT distinct ket.email_addr
            FROM mitkc_workflow_detail mwd
            INNER JOIN mitkc_workflow mw ON mwd.workflow_id = mw.workflow_id
            INNER JOIN krim_prncpl_t kpt ON mwd.approver_person_id = kpt.prncpl_id
            INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
            WHERE UPPER(mw.is_workflow_active) = 'Y'
            AND UPPER(mwd.approval_status) = 'O'
            AND mw.module_item_id = av_award_budget_header_id))

            LOOP

              IF rec.rw = 1  THEN

                ls_recipients := rec.email_addr;

              ELSE

                ls_recipients := ls_recipients||','|| rec.email_addr;

              END IF;

            END LOOP;

            EXCEPTION WHEN OTHERS THEN
            ls_exception := 'Error in fetching recipient email address';

          END;

      END IF;


      ll_message := REPLACE(ll_message,'{AWARD_NUMBER}', ls_award_number );
      ll_message := REPLACE(ll_message,'{ACCOUNT_NUMBER}', ls_account_number );
      ll_message := REPLACE(ll_message,'{TITLE}', ls_title );
      ll_message := REPLACE(ll_message,'{BUDGET_START}', ls_budget_start_date );
      ll_message := REPLACE(ll_message,'{BUDGET_END}', ls_budget_end_date );
      ll_message := REPLACE(ll_message,'{TOTAL_COST}', li_total_cost );
      ll_message := REPLACE(ll_message,'{VERSION_NUMBER}', li_version_number);
      ll_message := REPLACE(ll_message,'{COMMENTS}', av_comments);
      ll_message := REPLACE(ll_message,'{MITKC_AB_APP_URL}', ls_mitkc_ab_app_url);
      ll_message := REPLACE(ll_message,'{REJECTED_USER_NAME}', ls_rejected_user_name);
      ll_message := REPLACE(ll_message,'{AB_CREATOR_NAME}', ls_create_user_name);
      ll_message := REPLACE(ll_message,'{APPROVER_NAME}', ls_approver_user_name);


      ll_subject := REPLACE(ll_subject,'{AWARD_NUMBER}', ls_award_number );
      ll_subject := REPLACE(ll_subject,'{VERSION_NUMBER}', li_version_number);
      ll_subject := REPLACE(ll_subject,'{STATUS}', ls_status);

      IF ls_exception IS NULL THEN

        BEGIN

          SEND_MAIL(ls_recipients,NULL,NULL, ll_subject, ll_message,null,null);
          EXCEPTION
          WHEN OTHERS THEN
          ls_exception := SUBSTR(SQLERRM, 1, 1000);

        END;

      END IF;

      ls_notification_send_date := SYSDATE;

      insert_notification_log(
      av_notification_type_id,
      av_award_budget_header_id,
      ls_recipients,
      NULL,
      ll_subject,
      ll_message,
      ls_notification_send_date,
      ls_exception
      );

      RETURN 0;
  END;

 FUNCTION fn_send_notification (
   av_module_item_id      IN NUMBER,
   av_comments            IN CLOB,
   av_person_id           IN VARCHAR2,
   av_notification_number IN mitkc_notification_type.notification_number%TYPE
   )RETURN NUMBER
 IS
	ls_recipients             CLOB;
	ls_pi_email               varchar2(200);
	ls_exception              VARCHAR2(1000) := NULL;
	ls_notification_send_date DATE;
    ll_subject                mitkc_notification_type.subject%TYPE;
    ll_message                mitkc_notification_type.message%TYPE;


    ls_award_number      mitkc_cost_share_header.award_number%TYPE;
    ls_create_user       mitkc_cost_share_header.create_user%TYPE;
    ls_create_user_email varchar2(200);
 BEGIN

    BEGIN

            SELECT subject, message
            INTO ll_subject, ll_message
            FROM mitkc_notification_type
            WHERE notification_number = av_notification_number;

            EXCEPTION
            WHEN OTHERS THEN
            ls_exception := 'Error in fetching subject and message from mitkc_notification_type';

    END;

    BEGIN
			select t2.email_addr into ls_recipients
			from krim_prncpl_t t1 
			inner join krim_entity_email_t t2 on t1.entity_id = t2.entity_id
			where t1.prncpl_id = av_person_id;
		EXCEPTION 
		WHEN OTHERS THEN
			ls_recipients := null;
    END;

    BEGIN

           	SELECT t4.email_addr,t6.email_addr 
            INTO   ls_pi_email,ls_create_user_email
            FROM   mitkc_cost_share_header t1 
                   INNER JOIN award_persons t2 
                           ON t1.award_id = t2.award_id 
                              AND t2.contact_role_code = 'PI' 
                   INNER JOIN krim_prncpl_t t3 
                           ON t2.person_id = t3.prncpl_id 
                   INNER JOIN krim_entity_email_t t4 
                           ON t3.entity_id = t4.entity_id 

                   INNER JOIN krim_prncpl_t t5 
                           ON lower(t1.create_user) = lower(t5.prncpl_nm)
                   INNER JOIN krim_entity_email_t t6 
                           ON t5.entity_id = t6.entity_id                                   
            WHERE  t1.cost_share_header_id = av_module_item_id; 


    EXCEPTION 
    WHEN OTHERS THEN
        ls_pi_email := null;
        ls_create_user_email := null;
    END;

     BEGIN
            select award_number
            into ls_award_number
            from MITKC_COST_SHARE_HEADER 
            where COST_SHARE_HEADER_ID = av_module_item_id;

    EXCEPTION 
    WHEN OTHERS THEN
        NULL;
    END;

    if ls_recipients = ls_create_user_email then
        ls_create_user_email:=null;
    end if;

	if av_notification_number in (601,602,603,604,605) then --submit,approve,disapprove,migrated to kc,created new request 

        ls_recipients := ls_recipients||','||ls_pi_email||','||ls_create_user_email;

	end if;	


    BEGIN

        select ltrim(rtrim(ls_recipients,','),',') into ls_recipients from dual; 

        ll_subject := REPLACE(ll_subject,'{cost_share_header_id}', av_module_item_id );
        ll_message := REPLACE(ll_message,'{award_number}', ls_award_number);
        ll_message := REPLACE(ll_message,'{comments}', av_comments);
        ll_message := REPLACE(ll_message,'{cost_share_header_id}', av_module_item_id );


		if ls_recipients is not null then

          SEND_MAIL(ls_recipients,NULL,NULL, ll_subject, ll_message,null,null);

		end if;

    EXCEPTION
    WHEN OTHERS THEN
        ls_exception := SUBSTR(SQLERRM, 1, 1000);
    END;

	  ls_notification_send_date := SYSDATE;

      insert_notification_log(
      av_notification_number,
      av_module_item_id,
      ls_recipients,
      NULL,
      ll_subject,
      ll_message,
      ls_notification_send_date,
      ls_exception
      );

      RETURN 0;


 END; 

FUNCTION fn_send_irb_notification (
av_module_item_id 		  IN NUMBER,
av_comments 	  		  IN CLOB,
av_sender_person_id 	  IN VARCHAR2,
av_notification_type	  IN VARCHAR2,
av_notification_number 	  IN mitkc_notification_type.notification_number%TYPE
)RETURN NUMBER
IS
ls_recipients             CLOB;
ls_pi_email               varchar2(200);
ls_exception              VARCHAR2(1000) := NULL;
ls_notification_send_date DATE;
ll_subject                mitkc_notification_type.subject%TYPE;
ll_message                mitkc_notification_type.message%TYPE;


ls_award_number      	  mitkc_cost_share_header.award_number%TYPE;
ls_create_user       	  mitkc_cost_share_header.create_user%TYPE;
ls_create_user_email 	  VARCHAR2(200);
ls_exempt_number 	 	  VARCHAR2(20);
ls_exempt_title		 	  MITKC_IRB_PERSON_EXEMPT_FORM.EXEMPT_TITLE%TYPE;
li_pi_person_id			  MITKC_IRB_PERSON_EXEMPT_FORM.PERSON_ID%TYPE;
ls_pi_name				  MITKC_IRB_PERSON_EXEMPT_FORM.PERSON_NAME%TYPE;	 
ls_faculty_sponsor		  OSP$PERSON.FULL_NAME%TYPE;
ls_determination		  VARCHAR2(20);
li_pi_email				  OSP$PERSON.EMAIL_ADDRESS%TYPE;
ls_exempt_form_status     MITKC_IRB_EXEMPT_FORM_STATUS.DESCRIPTION%TYPE;
BEGIN

	BEGIN

		SELECT subject, message
		INTO ll_subject, ll_message
		FROM mitkc_notification_type
		WHERE notification_number = av_notification_number;

	EXCEPTION
	WHEN OTHERS THEN
		ls_exception := 'Error in fetching subject and message from mitkc_notification_type';
	END;


    IF av_notification_type = 'EXEMPT' THEN
        BEGIN
    
            SELECT 
                'E'||T1.IRB_PERSON_EXEMPT_FORM_ID,
                T1.EXEMPT_TITLE,
                T1.PERSON_ID,
                T1.PERSON_NAME,
                T2.FULL_NAME,
                DECODE(T1.IS_EXEMPT_GRANTED,'Y','Exempted','N','Non-Exempted'),
                T3.DESCRIPTION
            INTO 
                ls_exempt_number,
                ls_exempt_title,
                li_pi_person_id,
                ls_pi_name,
                ls_faculty_sponsor,
                ls_determination,
                ls_exempt_form_status
            FROM
            MITKC_IRB_PERSON_EXEMPT_FORM T1
            LEFT OUTER JOIN OSP$PERSON T2 ON T1.FACULTY_SPONSOR_PERSON_ID = T2.PERSON_ID
            LEFT OUTER JOIN MITKC_IRB_EXEMPT_FORM_STATUS T3 ON T1.EXEMPT_STATUS_CODE = T3.EXEMPT_STATUS_CODE
            WHERE T1.IRB_PERSON_EXEMPT_FORM_ID = av_module_item_id;
    
        EXCEPTION
        WHEN OTHERS THEN
            ls_exempt_number        := NULL;
            ls_exempt_title         := NULL;
            li_pi_person_id         := NULL;
            ls_pi_name              := NULL;
            ls_faculty_sponsor      := NULL;
            ls_determination        := NULL;
            ls_exempt_form_status   := NULL;
        END;
    END IF;

	IF av_notification_number IN (701,702,704) THEN

        BEGIN
            SELECT 
                T1.EMAIL_ADDRESS 
            INTO 
                li_pi_email
            FROM OSP$PERSON T1
            WHERE T1.PERSON_ID = li_pi_person_id;
        EXCEPTION 
        WHEN OTHERS THEN
            li_pi_email := NULL;
        END;

		ls_recipients := li_pi_email;
        
    ELSIF av_notification_number = 703 THEN
        
        ls_recipients := 'COUHES@mit.edu';
        
	END IF;--end of notification number


	BEGIN

		select ltrim(rtrim(ls_recipients,','),',') into ls_recipients from dual; 

		ll_subject := REPLACE(ll_subject,'{DETERMINATION}', ls_determination);
		ll_subject := REPLACE(ll_subject,'{EXEMPT_NUMBER}', ls_exempt_number);
		ll_subject := REPLACE(ll_subject,'{EXEMPT_CATEGORY}', null);
		ll_subject := REPLACE(ll_subject,'{STUDY_TITLE}', ls_exempt_title);
		ll_subject := REPLACE(ll_subject,'{PI_NAME}', ls_pi_name);
		ll_subject := REPLACE(ll_subject,'{FACULTY_SPONSOR}', ls_faculty_sponsor);
        ll_subject := REPLACE(ll_subject,'{EXEMPT_FORM_STATUS}', ls_exempt_form_status);

		ll_message := REPLACE(ll_message,'{DETERMINATION}', ls_determination);
		ll_message := REPLACE(ll_message,'{EXEMPT_NUMBER}', ls_exempt_number);
		ll_message := REPLACE(ll_message,'{EXEMPT_CATEGORY}', null);
		ll_message := REPLACE(ll_message,'{STUDY_TITLE}', ls_exempt_title);
		ll_message := REPLACE(ll_message,'{PI_NAME}', ls_pi_name );
		ll_message := REPLACE(ll_message,'{EXEMPT_FORM_STATUS}', ls_exempt_form_status);
        ll_message := REPLACE(ll_message,'{COMMENTS}', av_comments);
        ll_message := REPLACE(ll_message,'{FACULTY_SPONSOR}', ls_faculty_sponsor);

		if ls_recipients is not null then

		  SEND_MAIL(ls_recipients,NULL,NULL, ll_subject, ll_message,null,null);

		end if;

	EXCEPTION
	WHEN OTHERS THEN
		ls_exception := SUBSTR(SQLERRM, 1, 1000);
	END;

	ls_notification_send_date := SYSDATE;

	insert_notification_log(
	av_notification_number,
	av_module_item_id,
	ls_recipients,
	NULL,
	ll_subject,
	ll_message,
	ls_notification_send_date,
	ls_exception
	);

	RETURN 0;



END;   


  PROCEDURE send_mail(av_recipients IN CLOB,
  av_copy_to    IN VARCHAR2,
  av_reply_to   IN VARCHAR2,
  av_subject    IN CLOB,
  av_message    IN CLOB,
  av_attach_blob IN BLOB, 
  av_attach_name IN VARCHAR2)

  IS

  gs_mail_host        VARCHAR2(250); 
  gs_mail_port        VARCHAR2(250); 
  gs_mail_sender      VARCHAR2(250); 
  gs_mail_reply       VARCHAR2(250); 
  mail_conn           utl_smtp.connection; 
  mesg                CLOB; 
  mesg1               CLOB; 
  crlf                VARCHAR2( 2 ) := Chr(13) || Chr(10); 
  tab                 VARCHAR2(1) := Chr(9); 
  ls_mailmode         VARCHAR2(2); 
  ls_testmailreceiver VARCHAR2(256); 
  ls_recipients       CLOB; 
  ls_allrecipients    CLOB; 
  li_pos              NUMBER; 
  ls_emailid          VARCHAR2(200); 
  v_len               INTEGER; 
  v_cur_pos           NUMBER := 1; 
  l_step              PLS_INTEGER := 12000; 
  l_boundary          VARCHAR2(50) := '----=*#abc1234321cba#*='; 
  p_attach_mime       VARCHAR2(50) := 'text/html'; 

BEGIN 


    BEGIN 
        SELECT val 
        INTO   gs_mail_host 
        FROM   krcr_parm_t 
        WHERE  parm_nm = 'KC_DB_MAIL_HOST' 
               AND nmspc_cd = 'KC-GEN'; 
    EXCEPTION 
        WHEN OTHERS THEN 
          Raise_application_error(-20101,'Error Generating Email. Parameter KC_DB_MAIL_HOST not found in parameter table'); 

		RETURN; 
	END; 

	gs_mail_port := 25; 

	BEGIN 
		SELECT val 
		INTO   gs_mail_sender 
		FROM   krcr_parm_t 
		WHERE  parm_nm = 'EMAIL_NOTIFICATION_FROM_ADDRESS' 
			   AND nmspc_cd = 'KC-GEN'; 
	EXCEPTION 
		WHEN OTHERS THEN 
		  Raise_application_error(-20101,'Error Generating Email. Parameter EMAIL_NOTIFICATION_FROM_ADDRESS not found in parameter table'); 

		RETURN; 
	END; 

	BEGIN 
		SELECT val 
		INTO   ls_mailmode 
		FROM   krcr_parm_t 
		WHERE  parm_nm = 'EMAIL_NOTIFICATION_TEST_ENABLED' 
			   AND nmspc_cd = 'KC-GEN'; 
	EXCEPTION 
		WHEN OTHERS THEN 
		  Raise_application_error(-20101, 'Error Generating Email. Parameter EMAIL_NOTIFICATION_TEST_ENABLED not found in parameter table'); 

		RETURN; 
	END; 

	BEGIN 
		SELECT val 
		INTO   ls_testmailreceiver 
		FROM   krcr_parm_t 
		WHERE  parm_nm = 'EMAIL_NOTIFICATION_TEST_ADDRESS' 
			   AND nmspc_cd = 'KC-GEN'; 
	EXCEPTION 
		WHEN OTHERS THEN 
		  Raise_application_error(-20101,'Error Generating Email. Parameter EMAIL_NOTIFICATION_TEST_ADDRESS not found in parameter table'); 

		RETURN; 
	END; 

	BEGIN 
		SELECT val 
		INTO   gs_mail_reply 
		FROM   krcr_parm_t 
		WHERE  parm_nm = 'KC_DB_REPLY_ID' 
			   AND nmspc_cd = 'KC-GEN'; 
	EXCEPTION 
		WHEN OTHERS THEN 
		  Raise_application_error(-20101, 'Error Generating Email. Parameter KC_DB_REPLY_ID not found in parameter table') ; 

		RETURN; 
	END; 

	ls_recipients := av_recipients; 

	ls_allrecipients := av_recipients; 

	IF av_reply_to IS NOT NULL THEN

	  gs_mail_reply := av_reply_to;

	END IF; 

	mail_conn := utl_smtp.Open_connection(gs_mail_host, gs_mail_port); 

	utl_smtp.Helo(mail_conn, gs_mail_host); 

	utl_smtp.Mail(mail_conn, gs_mail_sender); 

	IF ls_mailmode = 'Y' THEN 
	  utl_smtp.Rcpt(mail_conn, ls_testmailreceiver); 
	ELSE 
		LOOP 
			li_pos := Instr(ls_recipients, ','); 

			IF li_pos = 0 THEN 
				utl_smtp.Rcpt(mail_conn, ls_recipients); 

				EXIT; 
			ELSE 
				ls_emailid := Substr(ls_recipients, 1, li_pos - 1); 

				utl_smtp.Rcpt(mail_conn, ls_emailid); 

				ls_recipients := Substr(ls_recipients, li_pos + 1); 

				IF ( Length(ls_recipients) < 1 ) OR ( ls_recipients IS NULL ) THEN 

					EXIT; 

				END IF; 
            END IF; 
		END LOOP; 
	END IF; 


	utl_smtp.Open_data(mail_conn); 

	mesg := 'From:'|| 'KC_Companion AwardBudgetTool'|| crlf || 
			'Reply-To:'|| gs_mail_reply || crlf; 

	IF ls_mailmode = 'N' THEN 

		mesg := mesg || 'CC:' || av_copy_to || crlf;

	END IF; 

	--mesg :=  mesg || 'BCC:'||ls_TestMailReceiver  || crlf || 
	--'Content-Type: text/html' || crlf; 
	mesg := mesg || 'BCC:' || ls_testmailreceiver || crlf; 

	mesg := mesg || 'Subject:'|| av_subject || crlf; 

	IF ls_mailmode = 'N' THEN 

		mesg := mesg || 'To: '|| ls_allrecipients || crlf; 

	ELSE 

	    mesg := mesg || 'To: '|| ls_testmailreceiver || crlf;

	END IF; 

	--mesg := mesg || '' || crlf || crlf ; 
	utl_smtp.Write_data(mail_conn, mesg); 

	utl_smtp.Write_data(mail_conn, 'MIME-Version: 1.0'|| utl_tcp.crlf); 

	utl_smtp.Write_data(mail_conn, 'Content-Type: multipart/mixed; boundary="'|| l_boundary || '"'||utl_tcp.crlf|| utl_tcp.crlf); 

	IF av_message IS NOT NULL THEN 

		utl_smtp.Write_data(mail_conn, '--'|| l_boundary || utl_tcp.crlf); 

		utl_smtp.Write_data(mail_conn, 'Content-Type: text/html; charset="iso-8859-1"' || utl_tcp.crlf || utl_tcp.crlf); 

		IF ls_mailmode = 'Y' THEN 

			mesg := 
					'<p>-----------------------------------------------------</p>' ||crlf 
					|| 
					'In Production mode this mail will be sent to ' 
					|| ls_allrecipients || crlf; 

			IF av_copy_to IS NOT NULL THEN 

				mesg := mesg || ' And copied to ' || av_copy_to || crlf; 

			END IF; 

			mesg := mesg 
					|| '<p>-----------------------------------------------------</p>' 
					|| crlf; 
		END IF; 
        utl_smtp.write_data(mail_conn, mesg);--ADDED
		v_len := dbms_lob.Getlength(av_message); 

		WHILE v_cur_pos <= v_len LOOP

			  utl_smtp.Write_data(mail_conn, 
			  dbms_lob.Substr(av_message, 32000, v_cur_pos)); 

			  v_cur_pos := v_cur_pos + 32000;

		END LOOP; 

		--UTL_SMTP.write_data(mail_conn, av_message); 
		utl_smtp.Write_data(mail_conn, utl_tcp.crlf || utl_tcp.crlf); 
	END IF; 

	IF av_attach_name IS NOT NULL THEN 

		utl_smtp.Write_data(mail_conn, '--' || l_boundary || utl_tcp.crlf); 

		utl_smtp.Write_data(mail_conn, 'Content-Type: ' || p_attach_mime || '; name="' 
                                 || av_attach_name 
                                 || '"' 
                                 || utl_tcp.crlf); 

		utl_smtp.Write_data(mail_conn, 'Content-Transfer-Encoding: base64' || utl_tcp.crlf); 

		utl_smtp.Write_data(mail_conn, 'Content-Disposition: attachment; filename="' || av_attach_name 
                                 || '"' 
                                 || utl_tcp.crlf 
                                 || utl_tcp.crlf); 

		FOR i IN 0 .. Trunc((dbms_lob.Getlength(av_attach_blob) - 1 )/l_step) 
		LOOP 
			utl_smtp.Write_data(mail_conn, utl_raw.Cast_to_varchar2( 
                                     utl_encode.Base64_encode( 
                                     dbms_lob.Substr(av_attach_blob,l_step, i * l_step + 1)))); 
		END LOOP; 

		utl_smtp.Write_data(mail_conn, utl_tcp.crlf || utl_tcp.crlf); 

	END IF; 

	utl_smtp.Write_data(mail_conn, '--' || l_boundary || '--' || utl_tcp.crlf); 

	utl_smtp.Close_data(mail_conn); 

	utl_smtp.Quit(mail_conn); 

  END SEND_MAIL;

  PROCEDURE  insert_notification_log(
  av_notification_type_id IN mitkc_notification_log.notification_number%TYPE,
  av_request_id IN mitkc_notification_log.module_item_key%TYPE,
  av_to_user_email_id IN mitkc_notification_log.to_user_email_id%TYPE,
  av_cc_email_id IN mitkc_notification_log.cc_email_id%TYPE,
  av_subject IN mitkc_notification_log.subject%TYPE,
  av_message IN mitkc_notification_log.message%TYPE,
  av_notification_send_date IN DATE,
  ls_exception IN VARCHAR2
  )
  IS

  ls_success mitkc_notification_log.is_success%TYPE;
  ls_from_email krim_entity_email_t.email_addr%TYPE;
  ls_has_exception VARCHAR2(1000) := NULL;

  BEGIN

       ls_has_exception := ls_exception;

        BEGIN

          SELECT val INTO ls_from_email
          FROM krcr_parm_t
          WHERE parm_nm = 'EMAIL_NOTIFICATION_FROM_ADDRESS'
          AND nmspc_cd = 'KC-GEN';
          EXCEPTION
          WHEN OTHERS THEN
          ls_has_exception := 'Parameter KC_DB_SENDER_ID not found in parameter table';

        END;

        IF ls_has_exception IS NULL THEN

          ls_success := 'Y';

        ELSE

          ls_success := 'N';

        END IF;

        INSERT INTO mitkc_notification_log(
        notification_log_id,
        notification_number,
        module_item_key,
        from_user_email_id,
        to_user_email_id,
        cc_email_id,
        subject,
        message,
        notification_send_date,
        is_success,
        error_message
        )
        VALUES(
        SEQ_MITKC_NOTIFICATION_LOG.nextval,
        av_notification_type_id,
        av_request_id,
        ls_from_email,
        nvl(av_to_user_email_id,'No recipients'),
        av_cc_email_id,
        av_subject,
        av_message,
        av_notification_send_date,
        ls_success,
        ls_has_exception
        );

  END;
  -------------------------Starts Function fn_send_nda_notification on 26072017---------------------------
  FUNCTION fn_send_nda_notification (
		   av_nda_request_id IN mitkc_nda_header.NDA_REQUEST_ID%TYPE,
		   av_notification_type_id IN mitkc_notification_type.notification_number%TYPE,
		   av_action_log_id IN MITKC_NDA_ACTION_LOG.NDA_ACTION_LOG_ID%TYPE,
           av_attach_blob IN BLOB, 
           av_attach_name IN VARCHAR2
		   )RETURN NUMBER
  is
	ll_subject mitkc_notification_type.subject%TYPE;
	ll_message mitkc_notification_type.message%TYPE;
	ls_title   mitkc_nda_header.TITLE%TYPE;
	ls_sponsor mitkc_nda_sponsor.sponsor_name%TYPE;
	ls_recipients CLOB;
	ls_pi_mail_id varchar2(200);
	ls_requestor_mail_id varchar2(200);
	ls_new_negotiator_mail_id varchar2(200);
	ls_old_negotiator_mail_id varchar2(200);
	ls_pi_personid varchar2(40);
	ls_requestor_personid varchar2(40);
	li_notification_no number(3);
	ls_exception varchar2(1000):=null;
	ls_notification_send_date DATE;
	ls_new_negotiator_personid varchar2(40);
	ls_old_negotiator_personid varchar2(40);

    ll_attach_blob blob:=null;
    ls_attach_name varchar2(1000):=null;

  BEGIN

	-------------------------------Starts getting PI & Requestor mail ids------------------------------------
	BEGIN
		select PI_PERSON_ID,REQUESTOR_PERSON_ID 
		into ls_pi_personid,ls_requestor_personid 
		from mitkc_nda_header 
		where nda_request_id = av_nda_request_id;
    EXCEPTION 
	WHEN OTHERS THEN
		return 0;
	END;

	BEGIN

			SELECT distinct ket.email_addr
			INTO ls_pi_mail_id
			FROM krim_prncpl_t kpt 
			INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
			WHERE kpt.prncpl_id = ls_pi_personid and ket.ACTV_IND='Y';


	EXCEPTION 
	WHEN OTHERS THEN
		ls_pi_mail_id:=null;
	END;

	BEGIN

		SELECT distinct ket.email_addr
		INTO ls_requestor_mail_id
	    FROM krim_prncpl_t kpt 
		INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
		WHERE kpt.prncpl_id = ls_requestor_personid and ket.ACTV_IND='Y';

	EXCEPTION 
	WHEN OTHERS THEN
		ls_requestor_mail_id:=null;
	END;

    IF ls_pi_mail_id=ls_requestor_mail_id then
        ls_requestor_mail_id:=NULL;
    END IF;

	---------------------------Ends getting PI & Requestor mail ids-------------------------------------

	BEGIN

			SELECT T1.Subject,t1.MESSAGE 
			INTO ll_subject,ll_message 
			FROM mitkc_notification_type t1 
			WHERE t1.NOTIFICATION_NUMBER=av_notification_type_id;
	EXCEPTION 
	WHEN OTHERS THEN
			ll_subject:=null;
			ll_message:=null;
			return 0;
	END;


	IF av_notification_type_id = 401 THEN -- Request Submitted Notification

		BEGIN

			IF ls_pi_mail_id IS NOT NULL THEN		

				ls_recipients:=ls_pi_mail_id||',';

			END IF;
			ls_recipients:=ls_recipients;--||'nda-request@mit.edu';--should uncomment
		EXCEPTION 
		WHEN OTHERS THEN

			ls_recipients:=null;

		END;

	END IF;

	IF av_notification_type_id = 403 THEN -- Request Assigned - Re-assigned


		BEGIN

			SELECT new_negotiator_person_id,old_negotiator_person_id
			INTO ls_new_negotiator_personid,ls_old_negotiator_personid		
			FROM mitkc_nda_negotiatior_history 
			WHERE nda_action_log_id=av_action_log_id;

			SELECT distinct ket.email_addr
			INTO ls_old_negotiator_mail_id
			FROM krim_prncpl_t kpt 
			INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
			WHERE kpt.prncpl_id = ls_old_negotiator_personid and ket.ACTV_IND='Y';
      EXCEPTION 
      WHEN OTHERS THEN
      ls_old_negotiator_mail_id:=null;
      END;	

      BEGIN
			SELECT distinct ket.email_addr
			INTO ls_new_negotiator_mail_id
			FROM krim_prncpl_t kpt 
			INNER JOIN krim_entity_email_t ket ON kpt.entity_id = ket.entity_id
			WHERE kpt.prncpl_id = ls_new_negotiator_personid and ket.ACTV_IND='Y';


     EXCEPTION 
     WHEN OTHERS THEN

			ls_new_negotiator_mail_id:=null;
    END;

		IF ls_pi_mail_id IS NOT NULL THEN
			ls_recipients:=ls_pi_mail_id||',';
		END IF;
		IF ls_requestor_mail_id IS NOT NULL THEN
			ls_recipients:=ls_recipients||ls_requestor_mail_id||',';
		END IF;
		IF ls_old_negotiator_mail_id IS NOT NULL THEN
			ls_recipients:=ls_recipients||ls_old_negotiator_mail_id||',';
		END IF;
		IF ls_new_negotiator_mail_id IS NOT NULL THEN
			ls_recipients:=ls_recipients||ls_new_negotiator_mail_id;
		END IF;

	END IF;

	IF av_notification_type_id in(402,404,405,406,407,412) THEN 
		-- Request Assigned- Initial,Awaiting Sponsor Signature,Executed,Abandoned,Terminated

		ls_recipients:=ls_pi_mail_id||','||ls_requestor_mail_id;

	END IF;

	IF av_notification_type_id = 408 THEN
		--Transferred to OGC

		BEGIN

			for i in(SELECT t1.subject,t1.message,t1.notification_number 
					 FROM mitkc_notification_type t1 
					 WHERE t1.NOTIFICATION_NUMBER in(408,409))
			loop
				BEGIN
					ll_subject:=i.subject;
					ll_message:=i.message;
					get_nda_replace_custom_field(av_nda_request_id,ll_subject,ll_message,av_action_log_id);
				END;

				-------------------Starts sending mail------------------- 
				BEGIN
				  SELECT Decode(i.notification_number, 408, ls_pi_mail_id 
                                          || ',' 
                                          || ls_requestor_mail_id, 
                                     409,NULL)-- 'tlo@mit.edu') --should uncomment
				  INTO   ls_recipients 
				  FROM   dual;

				  SELECT LTRIM(RTRIM(ls_recipients,','),',') INTO ls_recipients FROM DUAL;

				  SEND_MAIL(ls_recipients,NULL,NULL, ll_subject, ll_message,null,null);

				EXCEPTION
				WHEN OTHERS THEN				
					ls_exception := SUBSTR(SQLERRM, 1, 1000);
				END;

				ls_notification_send_date := SYSDATE;

					insert_notification_log(
					i.notification_number,
					av_nda_request_id,
					ls_recipients,
					NULL,
					ll_subject,
					ll_message,
					ls_notification_send_date,
					ls_exception
					);
				------------------Ends sending mail----------------------				

			end loop;
		END;


	END IF;

	IF av_notification_type_id = 410 THEN
		--Transferred to OGC

		BEGIN

			for i in(SELECT T1.Subject,t1.MESSAGE,T1.NOTIFICATION_NUMBER 
					 FROM mitkc_notification_type t1 
					 WHERE t1.NOTIFICATION_NUMBER in(410,411))
			loop
				BEGIN
					ll_subject:=i.subject;
					ll_message:=i.message;
					get_nda_replace_custom_field(av_nda_request_id,ll_subject,ll_message,av_action_log_id);
				END;

				-------------------Starts sending mail------------------- 
				BEGIN

				  SELECT Decode(i.notification_number, 410, ls_pi_mail_id 
                                          || ',' 
                                          || ls_requestor_mail_id, 
                                     411, null)--'mitogc@mit.edu') --should uncomment
				  INTO   ls_recipients 
				  FROM   dual;

				  SELECT LTRIM(RTRIM(ls_recipients,','),',') INTO ls_recipients FROM DUAL;

                  if i.NOTIFICATION_NUMBER=411 then

                        ll_attach_blob :=av_attach_blob;
                        ls_attach_name :=av_attach_name||'.pdf';

                  end if;

				  SEND_MAIL(ls_recipients,NULL,NULL, ll_subject, ll_message,ll_attach_blob,ls_attach_name);


				EXCEPTION
				WHEN OTHERS THEN				
					ls_exception := SUBSTR(SQLERRM, 1, 1000);
				END;

					ls_notification_send_date := SYSDATE;

					insert_notification_log(
					i.notification_number,
					av_nda_request_id,
					ls_recipients,
					NULL,
					ll_subject,
					ll_message,
					ls_notification_send_date,
					ls_exception
					);
				-------------------Ends sending mail------------------- 	
			end loop;
		END;


	END IF;

	IF av_notification_type_id not in(408,410) THEN
		BEGIN
			get_nda_replace_custom_field(av_nda_request_id,ll_subject,ll_message,av_action_log_id);
		END;

		-------------------Starts sending mail------------------- 
		BEGIN

			SELECT LTRIM(RTRIM(ls_recipients,','),',') INTO ls_recipients FROM DUAL;
			SEND_MAIL(ls_recipients,NULL,NULL, ll_subject, ll_message,null,null);

		EXCEPTION
		WHEN OTHERS THEN

			ls_exception := SUBSTR(SQLERRM, 1, 1000);

		END;


		ls_notification_send_date := SYSDATE;

					insert_notification_log(
					av_notification_type_id,
					av_nda_request_id,
					ls_recipients,
					NULL,
					ll_subject,
					ll_message,
					ls_notification_send_date,
					ls_exception
					);

        -------------------Ends sending mail------------------- 	
	END IF;
    RETURN 0;
  END;
  ------------------------Ends Function fn_send_nda_notification---------------------------------------------
  ------------------------Starts Procedure get_nda_replace_custom_field on 26072017--------------------------
  PROCEDURE get_nda_replace_custom_field (
         av_nda_request_id IN mitkc_nda_header.NDA_REQUEST_ID%TYPE, 
         av_subject IN OUT mitkc_notification_type.subject%TYPE,
         av_message IN OUT mitkc_notification_type.message%TYPE,
		 av_action_log_id IN MITKC_NDA_ACTION_LOG.NDA_ACTION_LOG_ID%TYPE
         ) 
  IS
	cursor c_data is

	SELECT T1.nda_request_id, 
		   T1.title, 
           T2.sponsor_name, 
           T1.requestor_name,
		   T1.NEGOTIATOR_NAME,
           T1.END_DATE
	FROM   mitkc_nda_header T1 
	LEFT OUTER JOIN mitkc_nda_sponsor T2 
    ON T1.nda_request_id = T2.nda_request_id 
	WHERE  T1.nda_request_id = av_nda_request_id 
    AND NVL(T2.SPONSOR_NUMBER,0)=(nvl((select min(SPONSOR_NUMBER) from mitkc_nda_sponsor where NDA_REQUEST_ID=av_nda_request_id),0));

	r_data	c_data%rowtype;
	av_note mitkc_nda_note.note%TYPE;
	av_New_Negotiator_name varchar2(160);
	av_negotiator_name_last_name varchar2(160);
	av_requestor_name_last_name varchar2(160);
	ls_nda_app_url varchar2(1000);
    ls_sponsor_name varchar2(200);
  BEGIN

	BEGIN

        select note into av_note 
		from mitkc_nda_note 
		where NDA_ACTION_LOG_ID=av_action_log_id;


	EXCEPTION 
	WHEN OTHERS THEN
		av_note:=null;
	END;

	BEGIN
		select T3.LAST_NM ||', '||T3.First_Nm||' '||T3.Middle_Nm 
		into av_New_Negotiator_name
		from MITKC_NDA_NEGOTIATIOR_HISTORY t1 
		inner join krim_prncpl_t t2 on t1.NEW_NEGOTIATOR_PERSON_ID=T2.Prncpl_Id 
		inner join KRIM_ENTITY_NM_T t3 on t2.entity_id=T3.Entity_Id 
		where T1.Nda_Action_Log_Id=av_action_log_id and T3.Actv_Ind='Y';

	EXCEPTION 
	WHEN OTHERS THEN
		av_New_Negotiator_name:=null;
	END;

	BEGIN
		select T3.LAST_NM ||', '||T3.First_Nm||' '||T3.Middle_Nm 
	    into av_negotiator_name_last_name
		from mitkc_nda_header t1 
		inner join krim_prncpl_t t2 on T1.Negotiator_Person_Id=T2.Prncpl_Id 
		inner join KRIM_ENTITY_NM_T t3 on t2.entity_id=T3.Entity_Id 
		where T1.Nda_Request_Id=av_nda_request_id and T3.Actv_Ind='Y';
	EXCEPTION 
	WHEN OTHERS THEN
		av_negotiator_name_last_name:=null;
	END;

	BEGIN
		select T3.LAST_NM ||', '||T3.First_Nm||' '||T3.Middle_Nm 
	    into av_requestor_name_last_name
		from mitkc_nda_header t1 
		inner join krim_prncpl_t t2 on T1.requestor_person_id=T2.Prncpl_Id 
		inner join KRIM_ENTITY_NM_T t3 on t2.entity_id=T3.Entity_Id 
		where T1.Nda_Request_Id=av_nda_request_id and T3.Actv_Ind='Y';
	EXCEPTION 
	WHEN OTHERS THEN
		av_requestor_name_last_name:=null;
	END;


	BEGIN
		  SELECT VAL 
		  INTO ls_nda_app_url
		  FROM KRCR_PARM_T
		  WHERE PARM_NM = 'NDA_APP_URL'
		  AND NMSPC_CD = 'KC-GEN';
	EXCEPTION 
	WHEN OTHERS THEN
		ls_nda_app_url:=null;
	END;

	open c_data;
	loop
	fetch c_data into r_data;
	exit when c_data%notfound;	

     if r_data.sponsor_name is null then
        ls_sponsor_name := '(Sponsor is not added)';
     else
        ls_sponsor_name := r_data.sponsor_name;
     end if;

		av_subject := REPLACE(av_subject,'{LOG_ID}', av_nda_request_id );
		av_subject := REPLACE(av_subject,'{TITLE}',  r_data.title);
		av_subject := REPLACE(av_subject,'{SPONSOR}',ls_sponsor_name);
		av_subject := REPLACE(av_subject,'{NEGOTIATOR_NAME}',r_data.NEGOTIATOR_NAME);
		av_subject := REPLACE(av_subject,'{NEGOTIATOR_NAME_LAST_NAME}',av_negotiator_name_last_name);
		av_subject := REPLACE(av_subject,'{REQUESTOR_NAME_LAST_NAME}',av_requestor_name_last_name);
        av_subject := REPLACE(av_subject,'{END_DATE}',r_data.END_DATE);

		av_message := REPLACE(av_message,'{LOG_ID}', av_nda_request_id );
		av_message := REPLACE(av_message,'{TITLE}',  r_data.title);
		av_message := REPLACE(av_message,'{SPONSOR}',ls_sponsor_name);
		av_message := REPLACE(av_message,'{REQUESTOR_NAME}',r_data.requestor_name);
		av_message := REPLACE(av_message,'{COMMENT}',av_note);
		av_message := REPLACE(av_message,'{NEW_NEGOTIATOR_NAME}',av_New_Negotiator_name);
		av_message := REPLACE(av_message,'{APP_URL}',ls_nda_app_url);
		av_message := REPLACE(av_message,'{NEGOTIATOR_NAME}',r_data.NEGOTIATOR_NAME);
		av_message := REPLACE(av_message,'{NEGOTIATOR_NAME_LAST_NAME}',av_negotiator_name_last_name);
		av_message := REPLACE(av_message,'{REQUESTOR_NAME_LAST_NAME}',av_requestor_name_last_name);
		av_message := REPLACE(av_message,'{END_DATE}',r_data.END_DATE);

	end loop;
	close c_data;

  END;

END;
/
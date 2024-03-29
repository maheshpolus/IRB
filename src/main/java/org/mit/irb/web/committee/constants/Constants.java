package org.mit.irb.web.committee.constants;

public interface Constants {

	String ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI = "ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI";
	String KC_GENERIC_PARAMETER_NAMESPACE = "KC-GEN";
	String KC = "KC";
	String KC_ALL_PARAMETER_DETAIL_TYPE_CODE = "All";
	String SPONSOR_HIERARCHIES_PARM = "PERSON_ROLE_SPONSOR_HIERARCHIES";
	String NIH_MULTIPLE_PI_HIERARCHY = "NIH Multiple PI";
	String DEFAULT_SPONSOR_HIERARCHY_NAME = "DEFAULT";
	String INVALID_TIME = "Invalid Time";
	String ALTERNATE_ROLE = "12";
	String INACTIVE_ROLE = "14";
	String DEFAULTTIME = "12:00";
	String NAME = "t";
	String GROUP = "g";
	String JOBNAME = "j";
	String JOBGROUP = "g";
	String COLON = ":";
	String ZERO = "0";
	String PRINCIPAL_INVESTIGATOR = "PI";
	String MULTI_PI = "MPI";
	String CO_INVESTIGATOR = "COI";
	String KEY_PERSON = "KP";
	String DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION = "Y";
	String DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION = "N";
	String ATTENDANCE = "2";
	String PROTOCOL = "3";
	String ACTION_ITEM = "4";
	String PROTOCOL_REVIEWER_COMMENT = "6";
	String DESCRIPTION = "description";
	String HASH_ALGORITHM = "SHA";
	String CHARSET = "UTF-8";
	String DECIMAL_FORMAT = "00000000";
	String DECIMAL_FORMAT_FOR_NEW_IP = "0000";
	String INSTITUTIONAL_PROPSAL_PROPSAL_NUMBER_SEQUENCE = "SEQ_PROPOSAL_PROPOSAL_ID";
	String DEFAULT_HOME_UNIT_NUMBER = "000001";
	String DEFAULT_HOME_UNIT_NAME = "University";

	// Security constants
	String SECRET = "SecretKeyToGenJWTs";
    long EXPIRATION_TIME = 864_000_000; // 10 days
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/fibi-comp/login";

    // Grant Call
    Integer GRANT_CALL_STATUS_CODE_DRAFT = 1;
    Integer GRANT_CALL_STATUS_CODE_OPEN = 2;
    Integer GRANT_CALL_TYPE_INTERNAL = 1;
    Integer GRANT_CALL_TYPE_EXTERNAL = 2;
    Integer GRANT_CALL_TYPE_OTHERS = 3;


    // Proposal
    Integer PROPOSAL_STATUS_CODE_IN_PROGRESS = 1;
    Integer PROPOSAL_STATUS_CODE_SUBMITTED = 5;
    Integer PROPOSAL_STATUS_CODE_APPROVED = 4;
    Integer PROPOSAL_STATUS_CODE_REJECTED = 3;
    Integer PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS = 2;
    Integer PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS = 8;
    Integer PROPOSAL_STATUS_CODE_REVISION_REQUESTED = 9;
    Integer PROPOSAL_STATUS_CODE_ENDORSEMENT = 10;
    Integer PROPOSAL_STATUS_CODE_AWARDED = 11;

    // Route Log Status Code
    String WORKFLOW_STATUS_CODE_WAITING = "W";
    String WORKFLOW_STATUS_CODE_APPROVED = "A";
    String WORKFLOW_STATUS_CODE_REJECTED = "R";
    String WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED = "T";
    String WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW = "WR";
    String WORKFLOW_STATUS_CODE_REVIEW_COMPLETED = "RC";

    // Workflow first stop number
    Integer WORKFLOW_FIRST_STOP_NUMBER = 1;
    Integer REVIEWER_ROLE_TYPE_CODE = 3;
    Integer ADMIN_ROLE_TYPE_CODE = 2;
    String SMU_GRANT_MANAGER_CODE = "10";
    String SMU_GRANT_PROVOST_CODE = "11";

    // Protocol
    String PROTOCOL_SATUS_CODE_ACTIVE_OPEN_TO_ENTROLLMENT = "200";

    // Parameter Constants
    String MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT = "KC-PD";
    String DOCUMENT_COMPONENT = "Document";
    String FISCAL_YEAR_BASED_IP = "GENERATE_IP_BASED_ON_FY";

}

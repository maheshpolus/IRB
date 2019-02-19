package org.mit.irb.web.correspondence.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.correspondence.dto.Exempt;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.utils.DBEngine;
import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

@Service(value = "correspondenceDao")
public class CorrespondenceDaoImpl implements CorrespondenceDao {
	
	protected static Logger logger = Logger.getLogger(CorrespondenceDaoImpl.class.getName());
	private DBEngine dbEngine;
	
	public CorrespondenceDaoImpl(){
		dbEngine = new DBEngine();
	}

	@Override
	public byte[] getTemplateData(CommonVO commonVO) {
		byte[] attachmentData =null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_LETTER_TEMPLATE_TYPE_CODE", DBEngineConstants.TYPE_STRING, "1"));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_LETTER_TEMPLATE_TYPE", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("CORRESPONDENCE_TEMPLATE");
				attachmentData = byteArrayOutputStream.toByteArray();
			}
		} catch (Exception e) {
			logger.info("Exception in getTemplateData method:" + e);
		}
		return attachmentData;
	}
	
	@Override
	public byte[] getActionTemplateData(CommonVO commonVO) {
		byte[] attachmentData =null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_LETTER_TEMPLATE_TYPE_CODE", DBEngineConstants.TYPE_STRING, "2"));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_LETTER_TEMPLATE_TYPE", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("CORRESPONDENCE_TEMPLATE");
				attachmentData = byteArrayOutputStream.toByteArray();
			}
		} catch (Exception e) {
			logger.info("Exception in getTemplateData method:" + e);
		}
		return attachmentData;
	}

	@Override
	public byte[] mergePlaceHolders(byte[] data, CommonVO commonVO){
		byte[] mergedOutput = null;
		try{
			InputStream myInputStream = new ByteArrayInputStream(data); 
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(myInputStream,TemplateEngineKind.Velocity);
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
			fieldsMetadata.load("exempt", Exempt.class, true);
			IContext context = report.createContext();
			List<Exempt> exemptList = setExemptQuestionList(commonVO.getIrbExemptForm().getExemptQuestionList());
			context.put("exemptList", exemptList);
			if(commonVO.getIrbExemptForm().getIsExempt().equalsIgnoreCase("O")){
				context.put("exemptList", "");
			}
			context = setPlaceHolderData(context,commonVO.getIrbExemptForm());
			DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
			mergedOutput = docxDocumentMergerAndConverter.mergeAndGeneratePDFOutput(myInputStream,report,null, TemplateEngineKind.Velocity,context);
		}catch (Exception e) {
			logger.info("Exception in mergePlaceHolders method:" + e);
		}
		return mergedOutput;
	}

	@Override
	public byte[] mergeActionPlaceHolders(byte[] data, CommonVO commonVO){
		byte[] mergedOutput = null;
		try{
			InputStream myInputStream = new ByteArrayInputStream(data); 
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(myInputStream,TemplateEngineKind.Velocity);
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
			fieldsMetadata.load("exempt", Exempt.class, true);
			IContext context = report.createContext();
			context = setActionPlaceHolderData(context,commonVO.getIrbExemptForm());
			DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
			mergedOutput = docxDocumentMergerAndConverter.mergeAndGeneratePDFOutput(myInputStream,report,null, TemplateEngineKind.Velocity,context);
		}catch (Exception e) {
			logger.info("Exception in mergePlaceHolders method:" + e);
		}
		return mergedOutput;
	}
	
	private IContext setPlaceHolderData(IContext context, IRBExemptForm irbExemptForm) {
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd-yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");  
			Date date1 = null;
			String exemptStartDate = null;
			String exemptEndDateStr = null;
			String exemptSubmissionDate = null;
			String exemptDeterminationText = null;
			String isExempt =null;
			String footerDialogue =null; 
			String determinationVar = "Determination :";
			if (irbExemptForm.getExemptProtocolStartDate() != null) {
			 date1=formatter.parse(irbExemptForm.getExemptProtocolStartDate());
			 exemptStartDate = simpleDateFormat.format(date1);
			} if (irbExemptForm.getExemptProtocolEndDate() != null) {
				date1=formatter.parse(irbExemptForm.getExemptProtocolStartDate());
				exemptEndDateStr = simpleDateFormat.format(date1);
			}
			if (irbExemptForm.getSubmissionDate() != null) {
				date1=formatter.parse(irbExemptForm.getExemptProtocolStartDate());
				exemptSubmissionDate = simpleDateFormat.format(date1);
			}
			if(irbExemptForm.getIsExempt().equalsIgnoreCase("Y")){
				isExempt= "Exempt";
				exemptDeterminationText = "Your research activities meet the criteria for exemption as defined by Federal regulation 45 CFR 46 under the following: ";
				footerDialogue = "All members of the research team must adhere to the policies as outlined"
						+ " in the Investigator responsibilities for Exempt Research. If the facts surrounding"
						+ " your evaluation change, you are required to submit a new Exempt Evaluation. "
						+ "Research records may be audited at any time during the conduct of the study.";
			} else if(irbExemptForm.getIsExempt().equalsIgnoreCase("O")){
				isExempt= "";
				determinationVar= "";
				exemptDeterminationText = "Based on your responses, COUHES review is not required. Your study activities do not meet the federal definition"
						+ " of research or do not involve human subject as defined in 45 CFR 46. Should the facts surrounding your submission change, you are required to"
						+ "submit a new Exempt Evaluation to reassess your exempt status";
				footerDialogue = "For Assistance please contact the COUHES office";
			} else if(irbExemptForm.getIsExempt().equalsIgnoreCase("N")){
				isExempt= "Non-Exempt";
				exemptDeterminationText = "The proposed research activities do not meet the criteria for exemption as defined by Federal regulation 45 CFR 46."
						+ "Comprehensive Review is required. Research cannot commence untill COUHES review has been completed. The comprehensive review apllication"
						+ "and supporting documentation are available at \n http://couhes.mit.edu/forms-templates";
				footerDialogue = "For Assistance please contact the COUHES office";
			}
			context.put("DETERMINATION_TEXT", exemptDeterminationText);
			context.put("DETERMINATION_VAR", determinationVar);
			context.put("FOOTER_DIALOGUE", footerDialogue);
			context.put("START_DATE", exemptStartDate);
			context.put("END_DATE", exemptEndDateStr);
			context.put("PI_NAME", irbExemptForm.getPersonName());
			context.put("EXEMPT_ID", irbExemptForm.getExemptFormID());
			context.put("SUBMISSION_DATE", irbExemptForm.getSubmissionDate() == null?"":exemptSubmissionDate);
			context.put("TITLE", irbExemptForm.getExemptTitle());
			context.put("UNIT_NAME", irbExemptForm.getUnitName());
			context.put("IS_EXEMPT", isExempt);	
			context.put("FACULTY_SPONSOR_NAME", irbExemptForm.getFacultySponsorPerson() == null?"":irbExemptForm.getFacultySponsorPerson());	

		}catch (Exception e) {
			logger.info("Exception in setPlaceHolderData method:" + e);
		}
		return context;
	}

	private IContext setActionPlaceHolderData(IContext context, IRBExemptForm irbExemptForm) {
		try{
			context.put("START_DATE", "12-02-2019");
			context.put("END_DATE", "12-02-2019");
			context.put("PI_NAME", "saxe");
			context.put("EXEMPT_ID", "99999999999");
			context.put("SUBMISSION_DATE", "12-02-2019");
			context.put("TITLE", "TITLE");
			context.put("UNIT_NAME", "UNIT_NAME");		
		}catch (Exception e) {
			logger.info("Exception in setPlaceHolderData method:" + e);
		}
		return context;
	}
	
	private List<Exempt> setExemptQuestionList(ArrayList<HashMap<String, Object>> exemptQuestionList) {
		List<Exempt> exemptList = new ArrayList<Exempt>();
		try{
			for(HashMap<String, Object> exemptQuestion: exemptQuestionList){
				exemptList.add(new Exempt(exemptQuestion.get("CATEGORY").toString(), exemptQuestion.get("CATEGORY_DESC").toString()));
			}
		}catch (Exception e) {
			logger.info("Exception in setExemptQuestionList method:" + e);
		}
		return exemptList;
	}
}

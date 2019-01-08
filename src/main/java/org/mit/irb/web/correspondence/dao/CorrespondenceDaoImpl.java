package org.mit.irb.web.correspondence.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
			context = setPlaceHolderData(context,commonVO.getIrbExemptForm());
			DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
			mergedOutput = docxDocumentMergerAndConverter.mergeAndGeneratePDFOutput(myInputStream,report,null, TemplateEngineKind.Velocity,context);
		}catch (Exception e) {
			logger.info("Exception in mergePlaceHolders method:" + e);
		}
		return mergedOutput;
	}

	private IContext setPlaceHolderData(IContext context, IRBExemptForm irbExemptForm) {
		try{
			context.put("START_DATE", irbExemptForm.getExemptProtocolStartDate());
			context.put("END_DATE", irbExemptForm.getExemptProtocolEndDate());
			context.put("PI_NAME", irbExemptForm.getPersonName());
			context.put("EXEMPT_ID", irbExemptForm.getExemptFormID());
			context.put("SUBMISSION_DATE", irbExemptForm.getSubmissionDate() == null?"":irbExemptForm.getSubmissionDate());
			context.put("TITLE", irbExemptForm.getExemptTitle());
			context.put("UNIT_NAME", irbExemptForm.getUnitName());
			context.put("IS_EXEMPT", irbExemptForm.getIsExempt().equalsIgnoreCase("Y")?"Exempt":"Non-Exempt");	
			context.put("FACULTY_SPONSOR_NAME", irbExemptForm.getFacultySponsorPerson() == null?"":irbExemptForm.getFacultySponsorPerson());		
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

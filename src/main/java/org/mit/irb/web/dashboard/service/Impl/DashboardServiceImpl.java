package org.mit.irb.web.dashboard.service.Impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.view.IRBViews;
import org.mit.irb.web.dashboard.dao.DashboardDao;
import org.mit.irb.web.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service(value = "dashboardService")
public class DashboardServiceImpl implements DashboardService {

	protected static Logger logger = Logger.getLogger(DashboardServiceImpl.class.getName());
	
	@Autowired
	DashboardDao dashboardDao; 

	@Override
	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception {
		DashboardProfile profile = dashboardDao.getSnapshotData(personId, personRoleType);
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboardType,
			String piName, String protocolNumber,String protocolTypeCode, String title, String prtocolStatusCode,String approvalDate,String  expirationDate,
			String isAdvanceSearch,String fundingSource,String protocolSubmissionStatus,String adminPersonId) throws ParseException {
		DashboardProfile profile = dashboardDao.getDashboardProtocolList(personId, personRoleType, dashboardType,
				piName, protocolNumber, protocolTypeCode, title, prtocolStatusCode,
				approvalDate,expirationDate,isAdvanceSearch,fundingSource,protocolSubmissionStatus,adminPersonId);
		return profile;
	}

	@Override
	public DashboardProfile getExpandedSnapShotView(String personId, String personRoleType, String avSummaryType) {
		DashboardProfile profile = dashboardDao.getExpandedSnapShotView(personId, personRoleType, avSummaryType);
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolType() {
		DashboardProfile profile = dashboardDao.getDashboardProtocolType();
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolStatus() {
		DashboardProfile profile = dashboardDao.getDashboardProtocolStatus();
		return profile;
	}

	@Override
	public DashboardProfile getprotocolSubmissionStatus() {
		DashboardProfile profile = dashboardDao.getProtocolSubmissionStatus();
		return profile;
	}

	@Override
	public DashboardProfile getAdminDashBoardPermissions(String personId ,String leadUnitNumber) {
		DashboardProfile profile = dashboardDao.getAdminDashBoardPermissions(personId, leadUnitNumber);
		return profile;
	}

	@Override
	public XSSFWorkbook getXSSFWorkbookDashboardProtocolList(CommonVO vo) throws Exception {
		
		logger.info("---------getXSSFWorkbookDashboardProtocolList---------");
		XSSFWorkbook workbook = new XSSFWorkbook();
		String personId = vo.getPersonId();
		String personRoleType = vo.getPersonRoleType();
		String dashboardType = vo.getDashboardType();
		String piName = vo.getPiName();
		String protocolNumber = vo.getProtocolNumber();
		String protocolTypeCode = vo.getProtocolTypeCode();
		String title = vo.getTitle();
		String protocolStatusCode = vo.getProtocolStatusCode();
		String approvalDate = vo.getApprovalDate();
		String expirationDate = vo.getExpirationDate();
		String isAdvanceSearch = vo.getIsAdvancedSearch();
		String fundingSource = vo.getFundingSource();
		String protocolSubmissionStatus = vo.getProtocolSubmissionStatus();
		String adminPersonId = vo.getAdminPersonId();
			
		DashboardProfile profile = dashboardDao.getDashboardProtocolList(personId, personRoleType, dashboardType,
				piName, protocolNumber, protocolTypeCode, title, protocolStatusCode,
				approvalDate,expirationDate,isAdvanceSearch,fundingSource,protocolSubmissionStatus,adminPersonId);
				
		XSSFSheet sheet = workbook.createSheet("Dashboard Protocol List");
		Object[] tableHeadingRow = {"PROTOCOL_NUMBER","PI_NAME","PROTOCOL_STATUS","PROTOCOL_TYPE","LAST_APPROVAL_DATE",
				"APPROVAL_DATE","EXPIRATION_DATE","TITLE",
				"ASSIGNEE_PERSON","UPDATE_TIMESTAMP","SUBMISSION_TYPE"};		
		prepareExcelSheet(profile, sheet, tableHeadingRow, workbook, vo);	
		return workbook;
	}
	
	private void prepareExcelSheet(DashboardProfile profile, XSSFSheet sheet, Object[] tableHeadingRow,
			XSSFWorkbook workbook, CommonVO vo) {

		int headingCellNumber = 0;
		String documentHeading = vo.getDocumentHeading();
		// Excel sheet heading style and font creation code.
		Row headerRow = sheet.createRow(0);
		Cell headingCell = headerRow.createCell(0);
		headingCell.setCellValue((String) documentHeading);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, tableHeadingRow.length - 1));
		Row headerRow1 = sheet.createRow(2);
		XSSFFont headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 15);
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_LEFT);
		headerStyle.setFont(headerFont);
		headingCell.setCellStyle(headerStyle);
		XSSFFont headerFontCriteria = workbook.createFont();
		headerFontCriteria.setBold(true);
		headerFontCriteria.setFontHeightInPoints((short) 13);
		XSSFCellStyle headerStyleCriteria = workbook.createCellStyle();
		headerStyleCriteria.setBorderTop(BorderStyle.HAIR);
		headerStyleCriteria.setBorderBottom(BorderStyle.HAIR);
		headerStyleCriteria.setBorderLeft(BorderStyle.HAIR);
		headerStyleCriteria.setBorderRight(BorderStyle.HAIR);
		headerStyleCriteria.setFont(headerFontCriteria);

		IRBViews iRBViewsnew = vo.getSearchCriteria();
		int rowNumberCount = 1;
		try {
			Row row1 = sheet.createRow(rowNumberCount++);
			int cellNumberValue = 0;
			Cell cell2 = row1.createCell(cellNumberValue);
			cell2.setCellValue("Search Criteria");
			cell2.setCellStyle(headerStyleCriteria);
			cell2 = row1.createCell(cellNumberValue + 1);
			cell2.setCellValue("Search Value");
			row1 = sheet.createRow(rowNumberCount++);
			cell2.setCellStyle(headerStyleCriteria);
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getProtocolNumberNew() != null && !iRBViewsnew.getProtocolNumberNew().equals("")) {
				cell2.setCellValue("Protocol Number");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getProtocolNumberNew());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (!iRBViewsnew.getProtocolStatus().equals("")) {
				cell2.setCellValue("protocol Status");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getProtocolStatus());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getExpirationDateNew() != null) {
				cell2.setCellValue("Expiration Date");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getExpirationDateNew());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getApprovalDateNew() != null) {
				cell2.setCellValue("Approval Date");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getApprovalDateNew());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getTitle() != null && !iRBViewsnew.getTitle().equals("")) {
				cell2.setCellValue("Title");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getTitle());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getProtocolPersonName() != null && !iRBViewsnew.getProtocolPersonName().equals("")) {
				cell2.setCellValue("protocol Person Name");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getProtocolPersonName());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getProtocolType() != null) {
				cell2.setCellValue("Protocol Type");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getProtocolType());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (!iRBViewsnew.getSubmissionStatus().equals("")) {
				cell2.setCellValue("Submission Status");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getSubmissionStatus());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getFundingSource() != null && !iRBViewsnew.getFundingSource().equals("")) {
				cell2.setCellValue("Funding Source");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getFundingSource());
				row1 = sheet.createRow(rowNumberCount++);
			}
			cell2 = row1.createCell(cellNumberValue);
			if (iRBViewsnew.getIrbAdmin() != null && !iRBViewsnew.getIrbAdmin().equals("")) {
				cell2.setCellValue("Irb Admin");
				cell2 = row1.createCell(cellNumberValue + 1);
				cell2.setCellValue(iRBViewsnew.getIrbAdmin());
				row1 = sheet.createRow(rowNumberCount++);
			}
		
		} catch (Exception e) {
			logger.error("Error in Excel Search criteria", e);
		}
		Row tableHeadRow = sheet.createRow(rowNumberCount);
		XSSFCellStyle tableHeadStyle = workbook.createCellStyle();
		tableHeadStyle.setBorderTop(BorderStyle.HAIR);
		tableHeadStyle.setBorderBottom(BorderStyle.HAIR);
		tableHeadStyle.setBorderLeft(BorderStyle.HAIR);
		tableHeadStyle.setBorderRight(BorderStyle.HAIR);
		XSSFFont tableHeadFont = workbook.createFont();
		tableHeadFont.setBold(true);
		tableHeadFont.setFontHeightInPoints((short) 12);
		tableHeadStyle.setFont(tableHeadFont);
		// Table body style and font creation code.
		XSSFCellStyle tableBodyStyle = workbook.createCellStyle();
		tableBodyStyle.setBorderTop(BorderStyle.HAIR);
		tableBodyStyle.setBorderBottom(BorderStyle.HAIR);
		tableBodyStyle.setBorderLeft(BorderStyle.HAIR);
		tableBodyStyle.setBorderRight(BorderStyle.HAIR);
		XSSFFont tableBodyFont = workbook.createFont();
		tableBodyFont.setFontHeightInPoints((short) 12);
		tableBodyStyle.setFont(tableBodyFont);
		// Set table head data to each column.
		for (Object heading : tableHeadingRow) {
			Cell cell = tableHeadRow.createCell(headingCellNumber++);
			cell.setCellValue((String) heading);
			cell.setCellStyle(tableHeadStyle);
		}
		// Set table body data to each column.
		try {
			int rowNumber = rowNumberCount + 1;
			for (HashMap<String, Object> hashMap : profile.getDashBoardDetailMap()) {
				Row row = sheet.createRow(rowNumber++);
				int cellNumber = 0;
				Cell cell1 = row.createCell(cellNumber++);
				cell1.setCellStyle(tableBodyStyle);

				if (hashMap.get("PROTOCOL_NUMBER") != null) {
					cell1.setCellValue(hashMap.get("PROTOCOL_NUMBER").toString());
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("PI_NAME") != null) {
					cell1.setCellValue(hashMap.get("PI_NAME").toString());
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("PROTOCOL_STATUS") != null) {
					cell1.setCellValue(hashMap.get("PROTOCOL_STATUS").toString());
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("PROTOCOL_TYPE") != null) {
					cell1.setCellValue(hashMap.get("PROTOCOL_TYPE").toString());
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("LAST_APPROVAL_DATE") != null) {
					String input = hashMap.get("LAST_APPROVAL_DATE").toString();
					if (input != null && !input.equals(" ")) {
						DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM-dd-yyyy");
						LocalDate datetime = LocalDate.parse(input, oldPattern);
						String parsedDate = datetime.format(newPattern);
						cell1.setCellValue(parsedDate);
					} else {
						cell1.setCellValue(" ");
					}
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("APPROVAL_DATE") != null) {
					String input = hashMap.get("APPROVAL_DATE").toString();
					if (input != null && !input.equals(" ")) {
						DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM-dd-yyyy");
						LocalDate datetime = LocalDate.parse(input, oldPattern);
						String parsedDate = datetime.format(newPattern);
						cell1.setCellValue(parsedDate);
					} else {
						cell1.setCellValue(" ");
					}
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("EXPIRATION_DATE") != null) {
					String input = hashMap.get("EXPIRATION_DATE").toString();
					if (input != null && !input.equals(" ")) {
						DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM-dd-yyyy");
						LocalDate datetime = LocalDate.parse(input, oldPattern);
						String parsedDate = datetime.format(newPattern);
						cell1.setCellValue(parsedDate);
					} else {
						cell1.setCellValue(" ");
					}
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("TITLE") != null) {
					cell1.setCellValue(hashMap.get("TITLE").toString());
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("ASSIGNEE_PERSON") != null) {
					cell1.setCellValue(hashMap.get("ASSIGNEE_PERSON").toString());
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("UPDATE_TIMESTAMP") != null) {
					String input = hashMap.get("UPDATE_TIMESTAMP").toString();
					if (input != null && !input.equals(" ")) {
						DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM-dd-yyyy");
						LocalDate datetime = LocalDate.parse(input, oldPattern);
						String parsedDate = datetime.format(newPattern);
						cell1.setCellValue(parsedDate);
					} else {
						cell1.setCellValue(" ");
					}
				} else {
					cell1.setCellValue(" ");
				}
				cell1 = row.createCell(cellNumber++);

				if (hashMap.get("SUBMISSION_TYPE") != null) {
					cell1.setCellValue(hashMap.get("SUBMISSION_TYPE").toString());
				} else {
					cell1.setCellValue(" ");
				}
			}
		} catch (Exception e) {
			logger.error("Error in Excel Generation", e);
		}
		XSSFSheet sheet1 = workbook.getSheetAt(0);
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;
		int rowCountValue = sheet.getPhysicalNumberOfRows() - rowNumberCount - 2;
		Row lastHeaderRow = sheet.createRow(rowCount);
		Cell lastHeadingCell = lastHeaderRow.createCell(0);
		lastHeadingCell.setCellValue((String) "Count Value : " + rowCountValue);
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, tableHeadingRow.length - 1));
		lastHeadingCell.setCellStyle(headerStyleCriteria);

		// Adjust size of table columns according to length of table data.
		autoSizeColumns(workbook, rowNumberCount);
	}
	
	private void autoSizeColumns(XSSFWorkbook workbook, int rowNumberCount) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(rowNumberCount);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}

	@Override
	public ResponseEntity<byte[]> getResponseEntityForDownload(CommonVO vo, XSSFWorkbook workbook) throws Exception {
		logger.info("--------- getResponseEntityForExcelOrPDFDownload ---------");
		byte[] byteArray = null;
		String exportType = vo.getExportType();
		String documentHeading = vo.getDocumentHeading();
		logger.info("exportType : " + exportType);
		logger.info("documentHeading : " + documentHeading);
		
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			byteArray = bos.toByteArray();
		
		return getResponseEntity(byteArray);
	}
	
	private ResponseEntity<byte[]> getResponseEntity(byte[] bytes) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentLength(bytes.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in method getResponseEntity", e);
		}
		return attachmentData;
	}
}

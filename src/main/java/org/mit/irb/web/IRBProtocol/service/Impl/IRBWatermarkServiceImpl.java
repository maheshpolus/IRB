package org.mit.irb.web.IRBProtocol.service.Impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.mit.irb.web.IRBProtocol.dao.Impl.IRBProtocolDaoImpl;
import org.mit.irb.web.IRBProtocol.service.IRBWatermarkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Transactional
@Service(value = "watermarkService")
public class IRBWatermarkServiceImpl implements IRBWatermarkService{
	Logger logger = Logger.getLogger(IRBProtocolDaoImpl.class.getName());

	@Override
	public byte[] generateTimestampAndUsernameForPdf(byte[] data, Date updatedDate, String updateUser) {
		byte[] byteArray = null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PdfReader reader = new PdfReader(data);
			PdfStamper watermark = new PdfStamper(reader, outputStream);
			Font FONT = new Font(Font.FontFamily.COURIER, 10, Font.BOLD, new GrayColor(0.5f));
			/*DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String startDate = df.format(updatedDate);*/
			Phrase watermarkText = new Phrase("Uploaded on " + "" + " by " + updateUser, FONT);
			PdfContentByte over;
			Rectangle pageSize;
			float x;
			int totalpages = reader.getNumberOfPages();
			for (int i = 1; i <= totalpages; i++) {
				pageSize = reader.getPageSizeWithRotation(i);
				x = (pageSize.getLeft() + pageSize.getRight());
				over = watermark.getOverContent(i);
				over.saveState();
				PdfGState state = new PdfGState();
				state.setFillOpacity(10);
				over.setGState(state);
				ColumnText.showTextAligned(over, Element.ALIGN_CENTER, watermarkText, x - 150, 15, 0);
				over.restoreState();
			}
			watermark.close();
			reader.close();
			byteArray = outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArray;
	}

	@Override
	public byte[] generateTimestampAndUsernameForImages(byte[] data, Date updatedDate, String updateUser,
			String contentType) {
		byte[] byteArray = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			/*DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String startDate = df.format(updatedDate);*/ 
			BufferedImage sourceImage = ImageIO.read(inputStream);
			Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
			AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(alphaChannel);
			g2d.setColor(Color.DARK_GRAY);
			String watermarkText = " Uploaded on " + "" + " by ";
			int countOfChar = watermarkText.length();
			float centerX = sourceImage.getWidth();
			float centerY = sourceImage.getHeight() - 10;
			int size = ((sourceImage.getWidth() + sourceImage.getHeight()) / 2) / countOfChar;
			g2d.setFont(new java.awt.Font("Courier", Font.BOLD, size));
			FontMetrics fontMetrics = g2d.getFontMetrics();
			g2d.drawString(watermarkText, centerX - fontMetrics.stringWidth(watermarkText), centerY);
			String[] parts = contentType.split("/");
			String format = parts[1];
			ImageIO.write(sourceImage, format, outputStream);
			g2d.dispose();
			byteArray = outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArray;
	}

	@Override
	public byte[] generateTimestampAndUsernameForDocuments(byte[] data, Date updatedDate, String updateUser) {
		byte[] byteArray = null;
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String startDate = df.format(updatedDate);
			InputStream is = new ByteArrayInputStream(data);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XWPFDocument doc = new XWPFDocument(is);
			XWPFParagraph para = doc.getLastParagraph();
			String text = " Uploaded on " + startDate + " by " + updateUser;
			XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
			if (headerFooterPolicy == null)
				headerFooterPolicy = doc.createHeaderFooterPolicy();
			headerFooterPolicy.createWatermark(text);
			XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
			para = header.getParagraphArray(0);
			org.apache.xmlbeans.XmlObject[] xmlobjects = para.getCTP().getRArray(0).getPictArray(0)
					.selectChildren(new javax.xml.namespace.QName("urn:schemas-microsoft-com:vml", "shape"));
			if (xmlobjects.length > 0) {
				com.microsoft.schemas.vml.CTShape ctshape = (com.microsoft.schemas.vml.CTShape) xmlobjects[0];
				ctshape.setFillcolor("#808080");
				ctshape.setStyle(
						"opacity:.5;position:absolute;margin-left:500;margin-top:0;width:150pt;height:15pt;mso-wrap-edited:f;mso-position-horizontal:bottom;mso-position-horizontal-relative:bottom;mso-position-vertical:bottom;mso-position-vertical-relative:margin;");
				doc.write(out);
				out.close();
				byteArray = out.toByteArray();
				doc.close();
			}
		} catch (Exception e) {
			logger.error("Exception in Document" + e.getMessage());
		}
		return byteArray;
	}

}

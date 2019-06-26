package org.mit.irb.web.IRBProtocol.service;
import java.sql.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Service(value = "watermarkService")
public interface IRBWatermarkService {
	/**
	 * This method is used to Generate WaterMark on proposal PDF document
	 * attachments.
	 * 
	 * @param data       - Byte array of Attachment.
	 * @param attachment - Object of ProposalAttachment class.
	 * @return byte array of attachment pdf.
	 */
	public byte[] generateTimestampAndUsernameForPdf(byte[] data, Date updatedDate, String updateUser);

	/**
	 * This method is used to Generate WaterMark on proposal Image attachments.
	 * 
	 * @param data       - Byte array of Attachment.
	 * @param attachment - Object of ProposalAttachment class.
	 * @return byte array of attachment Image.
	 */
	public byte[] generateTimestampAndUsernameForImages(byte[] data, Date updatedDate, String updateUser, String contentType);
	
	/**
	 * @param data       - Byte array of Attachment.
	 * @param attachment - Object of ProposalAttachment class.
	 * @return byte array of attachment docx.
	 * @return
	 */
	public byte[] generateTimestampAndUsernameForDocuments(byte[] data, Date updatedDate, String updateUser); 
}

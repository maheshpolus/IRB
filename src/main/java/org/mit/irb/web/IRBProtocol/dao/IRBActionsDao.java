package org.mit.irb.web.IRBProtocol.dao;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;

public interface IRBActionsDao {

	IRBActionsVO getPersonRight(IRBActionsVO vo);

	IRBActionsVO submitForReviewProtocolActions(IRBActionsVO vo);

	IRBActionsVO withdrawProtocolActions(IRBActionsVO vo);

	IRBActionsVO adandonProtocolActions(IRBActionsVO vo);

	IRBActionsVO createAmendmentProtocolActions(IRBActionsVO vo);

	IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo);

	IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo);

	IRBActionsVO notifyIRBProtocolActions(IRBActionsVO vo);

	IRBActionsVO requestForDataAnalysisProtocolActions(IRBActionsVO vo);

	IRBActionsVO requestForSuspensionProtocolActions(IRBActionsVO vo);

	IRBActionsVO requestForTerminationProtocolActions(IRBActionsVO vo);

	IRBActionsVO requestForCloseProtocolActions(IRBActionsVO vo);

	IRBActionsVO requestForCloseEnrollmentProtocolActions(IRBActionsVO vo);

	IRBActionsVO requestForReopenEnrollmentProtocolActions(IRBActionsVO vo);

	IRBActionsVO modifyAmendmentSectionProtocolActions(IRBActionsVO vo);

}

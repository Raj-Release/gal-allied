/**
 * 
 */
package com.shaic.claim.reimbursement.createandsearchlot;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.ClaimPayment;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.vijayar
 *
 */
public class CreateAndSearchLotMapper {
	
	private static MapperFacade tableMapper;
	
	static CreateAndSearchLotMapper myObj;
	
	//private static BoundMapperFacade<ClaimPayment, CreateAndSearchLotTableDTO> createAndSearchLotMapper;
	
	public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ClaimPayment, CreateAndSearchLotTableDTO> lotMapper = mapperFactory.classMap(ClaimPayment.class, CreateAndSearchLotTableDTO.class);
		
		// Mapping of DO to DTO. Yet to be implemented.
		lotMapper.field("intimationNumber", "intimationNo");
		lotMapper.field("claimNumber","claimNo");
		lotMapper.field("policyNumber","policyNo");
		lotMapper.field("rodNumber","rodNo");
		lotMapper.field("paymentStatus.value","paymentStatus");
		lotMapper.field("productCode","product");
		lotMapper.field("cpuCode","cpuCode");
		lotMapper.field("paymentCpuCode","paymentCpuString");
		lotMapper.field("paymentType","paymentTypeValue");
		lotMapper.field("paymentType","tempPaymentType");
		lotMapper.field("ifscCode","ifscCode");
		lotMapper.field("accountNumber","beneficiaryAcntNo");
		lotMapper.field("branchName","branchName");
		lotMapper.field("vnrFlag","verifyAccntValue");
		lotMapper.field("claimType","typeOfClaim");
		lotMapper.field("approvedAmount","approvedAmt");
		lotMapper.field("totalApprovedAmount","approvedAmt");
		lotMapper.field("payeeName","payeeNameStr");
		lotMapper.field("payeeName","tempPayeeName");
		//bancs payment verification lvl1 - src risk id,nominee code from db
		lotMapper.field("payeeRelationship","payeeRelationship");
		lotMapper.field("nomineeName", "nominee");
		lotMapper.field("nomineeRelationship", "nomineeRelationship");
		lotMapper.field("legalHeirRelationship", "legalHeirRelationship");
		lotMapper.field("accountPreference", "accountPreference");
		lotMapper.field("accountType", "accountType");
		lotMapper.field("payeeName", "nameAsPerBankAccount");
		lotMapper.field("micrCode", "micrCode");
		lotMapper.field("virtualPaymentAddr", "virtualPaymentProcess");
		lotMapper.field("payableAt","payableAt");//GALAXYMAIN-12716
		lotMapper.field("panNumber","panNo");
		//lotMapper.field("pioCode","providerCode"); // --> This needs to be checked.
		lotMapper.field("hospitalCode","providerCode"); // As per sathish sir comments, hospital code is populated in provider code column. 
		lotMapper.field("zonalMailId","emailID");
		lotMapper.field("zonalMailId","dbEmailId");
		lotMapper.field("batchNumber","accountBatchNo"); //--> This needs to be checked.
		lotMapper.field("lotNumber","lotNo");
		//lotMapper.field("","serviceTax"); --> This needs to be checked.
		//lotMapper.field("","totalAmnt"); --> This needs to be checked.
		lotMapper.field("tdsAmount","tdsAmt");
		lotMapper.field("netAmount","netAmnt");
		lotMapper.field("tdsPercentage","tdsPercentage");
		lotMapper.field("bankName","bankName");
		lotMapper.field("key","claimPaymentKey");
		lotMapper.field("hospital","hospitals");
		lotMapper.field("hospital.name","hospitalName");
		//lotMapper.field("hospitalName","hospitalName");
		lotMapper.field("hospitalCode","hospitalCode");
		lotMapper.field("reasonForChange", "reasonForChange");
		lotMapper.field("legalHeirName", "legalFirstName");
		lotMapper.field("pioCode", "pioCode");
		lotMapper.field("modifiedBy","userId");
		lotMapper.field("batchCreatedDate","paymentReqDt");

		lotMapper.field("delayDays","numberofdays");
		lotMapper.field("interestRate","intrestRate");		
		lotMapper.field("interestAmount","intrestAmount");
		lotMapper.field("interestAmount","interestAmntForCalculation");
		lotMapper.field("intrestRemarks","remarks");
		lotMapper.field("lastAckDate","lastAckDate");
		lotMapper.field("faApprovalDate", "faApprovedDate");
		lotMapper.field("approvedAmount", "faApprovedAmnt");
		lotMapper.field("allowedDelayDays","noofdaysexceeding");
		lotMapper.field("allowedDelayDays","noOfDaysExceedingforCalculation");
		lotMapper.field("totalAmount", "penalTotalAmnt");
		lotMapper.field("paymentStatus.key","paymentStatusKey");
		lotMapper.field("documentReceivedFrom", "docReceivedFrom");
		lotMapper.field("reconisderFlag","reconsiderationFlag");

		lotMapper.field("documentReceivedFrom","documentReceivedFrom");
		lotMapper.field("remarks","dbSideRemark");

		lotMapper.field("batchProcessFlag","batchProcessFlag");
		lotMapper.field("recordStatusFlag","recStatusFlag");
		lotMapper.field("zonalMailId", "zonalMailId");
		
		lotMapper.field("payModeChangeReason", "payModeChangeReason");
		lotMapper.field("gmcEmployeeName", "gmcEmployeeName");
		lotMapper.field("proposerName", "gmcProposerName");
		lotMapper.field("productName","productName");
		lotMapper.field("claimType","claimType");
		lotMapper.field("paymentCpuCode","paymentCpucodeTextValue");
		lotMapper.field("saveFlag","saveFlag");
		
		lotMapper.field("chequeDDDate", "chequeDate");
		lotMapper.field("chequeDDNumber", "chequeNo");
		lotMapper.field("bankCode","bankCode");

		lotMapper.field("sourceRiskId", "sourceRskID");
		lotMapper.field("bancsPriorityFlag", "bancsPriorityFlag");
//		lotMapper.field("", "receiptOfDocumentDTO.")

		lotMapper.field("verifiedStatusFlag","verifiedStatusFlag");
		lotMapper.field("selectCount", "recordCount");

		//lotMapper.field("rodkey.updatePaymentDtlsFlag","docVerifiedValue");
		//lotMapper.field("rodkey.docAcknowLedgement.documentReceivedFromId","documentReceivedFromId");
		//lotMapper.field("","refNo"); --> This needs to be checked.
		//lotMapper.field("","paymentReqDt"); --> This needs to be checked.
		//lotMapper.field("","paymentReqUID"); --> This needs to be checked.
		
		lotMapper.register();
		tableMapper = mapperFactory.getMapperFacade();
		//createAndSearchLotMapper = mapperFactory.getMapperFacade(ClaimPayment.class, CreateAndSearchLotTableDTO.class);
	}

	public static List<CreateAndSearchLotTableDTO> getListOfCreateAndSearchLotTableDTO(List<ClaimPayment> claimPaymentList)
	{
		List<CreateAndSearchLotTableDTO> mapAsList = tableMapper.mapAsList(claimPaymentList, CreateAndSearchLotTableDTO.class);
		return mapAsList;
	}
	
	
	public static CreateAndSearchLotMapper getInstance(){
        if(myObj == null){
            myObj = new CreateAndSearchLotMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	//public 
	
	
}
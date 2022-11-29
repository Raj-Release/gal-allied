[1mdiff --git a/src/main/java/com/shaic/claim/reimbursement/financialapproval/pages/billinghospitalization/FinancialHospitalizationPageUI.java b/src/main/java/com/shaic/claim/reimbursement/financialapproval/pages/billinghospitalization/FinancialHospitalizationPageUI.java[m
[1mindex dd6cea1..4833e45 100644[m
[1m--- a/src/main/java/com/shaic/claim/reimbursement/financialapproval/pages/billinghospitalization/FinancialHospitalizationPageUI.java[m
[1m+++ b/src/main/java/com/shaic/claim/reimbursement/financialapproval/pages/billinghospitalization/FinancialHospitalizationPageUI.java[m
[36m@@ -523,7 +523,7 @@[m [mpublic class FinancialHospitalizationPageUI extends ViewComponent implements Cla[m
 		if(!hasError) {[m
 			if (bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {[m
 				if((bean.getHospitalizaionFlag() != null && bean.getHospitalizaionFlag()) || (bean.getPartialHospitalizaionFlag() != null && bean.getPartialHospitalizaionFlag()) || (bean.getIsHospitalizationRepeat() != null && bean.getIsHospitalizationRepeat()) ) {[m
[31m-					Integer amt = (bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() != null ?  bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() : 0) + (bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null ?  bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : 0);[m
[32m+[m					[32mInteger amt = bean.getHospitalizationCalculationDTO().getNetPayableAmt() != null ? bean.getHospitalizationCalculationDTO().getNetPayableAmt() : 0;[m
 					if(amt <= 0) {[m
 						hasError = true;[m
 						eMsg += "Approved Amount is Zero. Hence this ROD can not be submitted. ";[m

package com.shaic.restservices.bancs;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.WSConstants;


@Path("/claims")
@Stateless
public class ClaimReverseFeedService {

        @Inject
        GLXWBService wbService;
        
        private String entrySource="";

        private final Logger log = LoggerFactory.getLogger(ClaimReverseFeedService.class);

        @POST
        @Path("/claimReverseFeed")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public String checkReverseFeed(ClaimReverseFeedRequest request , @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
                List<ClaimReverseFeedData> reverseObjList = request.getDetails();
                boolean validationErrorFlag = false;
                StringBuilder docErrorMsg = new StringBuilder();
                JSONObject reqStatus = new JSONObject();
                entrySource="";

                  ObjectMapper mapper = new ObjectMapper();
                  log.info("PAYMENT REVERSE FEED REQ : " +  mapper.writeValueAsString(request));
                try{
                        if(authString.trim().equals(BPMClientContext.BANCS_EXP_WS_AUTH_KEY)){
                                for (int i=0;i<reverseObjList.size();i++) {
                                        ClaimReverseFeedData reverseObj = reverseObjList.get(i);
                                        StringBuilder dataErrorMsg = new StringBuilder();

                                        if(StringUtils.isNotBlank(reverseObj.getPrfFieldIndicator())){
                                                //As per CR requirement below header validation section has been commented - 12-20-2020
                                                
                                                /*if(reverseObj.getPrfFieldIndicator().equals(WSConstants.HEADER)){
                                                        if((StringUtils.isBlank(reverseObj.getPrfBankAccountNumber()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - BANK_ACCOUNT_NUMBER is Empty or null");
                                                        }
                                                        else if((StringUtils.isBlank(reverseObj.getPrfTotalAmountPaid()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - TOTAL_AMOUNT_PAID is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfVoucherDate()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - VOUCHER_DATE is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfChequeDate()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - CHEQUE_DATE is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfChequeNumber()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - CHEQUE_NUMBER is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfPaymentMethod()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - PAYMENT_METHOD is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getCheckId()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - CHECK_ID is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getLastUpdateDate()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - LAST_UPDATE_DATE is Empty or null");
                                                        }        
                                                }*/
                                                /*if(reverseObj.getPrfFieldIndicator().equals(WSConstants.LINE)){
                                                        if((StringUtils.isBlank(reverseObj.getPrfInvoiceNumber()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - INVOICE_NUMBER is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfVoucherDate()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - VOUCHER_DATE is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfChequeDate()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - CHEQUE_DATE is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfChequeNumber()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - CHEQUE_NUMBER is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfPaymentMethod()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - PAYMENT_METHOD is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfPaymentVoucherNumber()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - PAYMENT_VOUCHER_NUMBER is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getPrfInvoicePaidAmount()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - INVOICE_PAID_AMOUNT is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getCheckId()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - CHECK_ID is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getLastUpdateDate()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - LAST_UPDATE_DATE is Empty or null");
                                                        }else if((StringUtils.isBlank(reverseObj.getEntrySource()))){
                                                                validationErrorFlag = true;
                                                                dataErrorMsg.append("Error - ENTRY_SOURCE is Empty or null");
                                                        }*/
                                                        if (!StringUtils.isBlank(reverseObj.getEntrySource()))
                                                        {
                                                                if ((reverseObj.getEntrySource().equalsIgnoreCase("BANCS") || reverseObj.getEntrySource().equalsIgnoreCase("INDIA TDS")) && (entrySource.equalsIgnoreCase("")))
                                                                {
                                                                        entrySource=reverseObj.getEntrySource();
                                                                }
                                                                else if (reverseObj.getEntrySource().equalsIgnoreCase(entrySource))
                                                                {
                                                                        validationErrorFlag = true;
                                                                        dataErrorMsg.append("Error - Duplicate ENTRY_SOURCE in line items : " + entrySource);        

                                                                }        
                            }
                                                }
                                        else
                                        {
                                                dataErrorMsg.append("Error - FIELD_INDICATOR is Empty or null");
                                        }
                                        if(!StringUtils.isBlank(dataErrorMsg.toString())){
                                                docErrorMsg.append("Set No:- "+(i+1)+" "+dataErrorMsg+" \n ");
                                        }

                                }
                                if(!StringUtils.isBlank(docErrorMsg)){
                                        reqStatus.put(WSConstants.ERROR_CODE, "600");
                                        reqStatus.put(WSConstants.ERROR_DESCRIPTION,docErrorMsg);        
                                }
                                if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
                                        for (ClaimReverseFeedData reverseObj:reverseObjList) {
                                                ClaimReverseFeedTable prfData = wbService.ClaimReverseFeedData(reverseObj,request.getClaimUprId());
                                                if(prfData != null){
                                                        reqStatus.put(WSConstants.ERROR_CODE, "200");
                                                        reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_SUCCESS);        
                                                }else{
                                                        reqStatus.put(WSConstants.ERROR_CODE, "600");
                                                        reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+prfData);
                                                }
                                        }
                                }
                        }
                        else{
                                reqStatus.put(WSConstants.ERROR_CODE, "600");
                                reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
                        }
                }catch(Exception exp){
                        reqStatus.put(WSConstants.ERROR_CODE, "600");
                        reqStatus.put(WSConstants.ERROR_DESCRIPTION, exp.getMessage());
                        log.error("Exception occured in while Creating Reverse Feed details"+exp.getMessage());
                }        
                log.info(reqStatus.toString());
                return reqStatus.toString();
        }
}

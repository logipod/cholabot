package com.servion.etso.ivr.watson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.http.HttpRequest;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import co.sgsl.db.DbConstants;
import co.sgsl.db.DbManager;

/**
 * @author logesh.m
 *
 */
public class ConversationUtility {

	MessageResponse watsonResponse;
	private String NLPResult;
	private String intent;
	private com.ibm.watson.developer_cloud.conversation.v1.model.Intent intents;
	private String mobileNumber;

	static Map context = new HashMap();

	public String conversationUtil(Map context, String englishText, String mobileNumber) {
		NLPResult = "";
		watsonResponse = conversationAPI(englishText, context);
		// mobileNumber =
		// (String)request.getSession().getAttribute("PolicyHolderNumber");
		if (watsonResponse.getIntents() != null && !watsonResponse.getIntents().isEmpty()) {
			intents = watsonResponse.getIntents().get(0);
			intent = intents.getIntent();
			if (intent.equalsIgnoreCase("Claim_Status")) {
				List<HashMap<String, String>> dataMap = DbManager.instance
						.fetchData(DbConstants.FETCH_CLAIM_STATUS.replace(DbConstants.MOBILENUMBER, mobileNumber));
				if (dataMap != null && !dataMap.isEmpty()) {
				String applyedDate = "", calim_number = "", status = "";
				for (HashMap<String, String> value : dataMap) {
					calim_number = value.get("calim_number");
					System.out.println("policy number is = " + calim_number);
					List<HashMap<String, String>> masterDataMap = DbManager.instance
							.fetchData(DbConstants.FETCH_POLICY_DETILS_WITH_POLICY_NUM.replace(DbConstants.CLAIM_ID,
									calim_number));
					System.out.println(
							"query-->" + DbManager.instance.fetchData(DbConstants.FETCH_POLICY_DETILS_WITH_POLICY_NUM
									.replace(DbConstants.CLAIM_ID, calim_number)));
					applyedDate = masterDataMap.get(0).get("appliedDate");
					status = value.get("calim_status");
					System.out.println("applyedDate=" + applyedDate + "& status is =" + status);
				}
				if (status.equalsIgnoreCase("waiting for survey inspector report")) {
					NLPResult = "Sure, I will help with the status of your claim, intimated on " + applyedDate
							+ " and claim id " + calim_number + ".";
					NLPResult = NLPResult
							+ " Our survey inspector has completed review of your claim. Survey report analysis is in progress, we will update you once survey report is received."
							+ " Is there anything else I can help you with?";
				} else if (status.equalsIgnoreCase("claim registered")) {
					NLPResult = "Sure, I will help with the status of your claim, intimated on " + applyedDate
							+ " and claim id " + calim_number + ".";
					NLPResult = NLPResult
							+ " Your claim intimation request has been registered successfully. Our Survey inspector will be contacting you for claim review. Is there anything else I can help you with?";
				}
				}else{
					NLPResult = "You have not initiated any claim with us yet";
				}
			}else if (intent.equalsIgnoreCase("Policy_copy")){
				List<HashMap<String, String>> dataMap = DbManager.instance.fetchData(DbConstants.FETCH_POLICY_NUMBER.replace(DbConstants.MOBILENUMBER, mobileNumber+" "));
				String poicyNumber ="";
				if (dataMap != null && !dataMap.isEmpty()) {
					poicyNumber = dataMap.get(0).get("Policynumber");
				}
				
				StringBuilder insterDate = new StringBuilder(DbConstants.insertpolicyCopy(poicyNumber, "N"));
				System.out.println("query->"+insterDate.toString());
				DbManager.instance.insterData(insterDate.toString());
				NLPResult = "You will be soon receiving a copy of your poicy number "+poicyNumber+" on your registered email address. Is there anything else I can help you with? ";
				
			/*	StringBuilder strBuil = new StringBuilder("ok, we see multiple policies linked with your account, please selece one from the following details.");
				List<HashMap<String, String>> dataMap = DbManager.instance.fetchData(DbConstants.FETCH_POLICY_NUMBER.replace(DbConstants.MOBILENUMBER, mobileNumber+" "));
				//Map<String,Map<String,String>> policyCopy = new HashMap<String,Map<String,String>>();
				Map<String,String> multipleCopy = new HashMap<String,String>();
				if(dataMap.size()>1){
					watsonResponse.getContext().put("multiplePolicy", true);
					watsonResponse.getContext().put("singlePolicy", false);
					if (dataMap != null && !dataMap.isEmpty()) {
						int i = 1;
						for (Map<String, String> hashMap : dataMap) {
							for (String entry : hashMap.keySet()) {
								break;
							}
							break;
						}
						for (Map<String, String> hashMap : dataMap) {
							for (String entry : hashMap.keySet()) {
								if("Policynumber".equals(entry))
								{
									strBuil.append("press "+i+" if you need a copy for the policy number "+hashMap.get("Policynumber"));
									multipleCopy.put(i+"", hashMap.get("Policynumber"));
								}
								i++;
								if("vehicle_registration_no".equals(entry))
								{
									strBuil.append("and vehicle registration number"+hashMap.get("vehicle_registration_no"));
								}
								strBuil.append("press "+i+" if you need copy  for the policy number");
							}
						}
					}
					
					NLPResult = strBuil.toString();
					
				}else{
						String poicyNumber ="";
						watsonResponse.getContext().put("singlePolicy", true);
						watsonResponse.getContext().put("multiplePolicy", false);
						for (Map<String, String> hashMap : dataMap) {
							for (String entry : hashMap.keySet()) {
								break;
							}
							break;
						}
						for (Map<String, String> hashMap : dataMap) {
							for (String entry : hashMap.keySet()) {
								if("Policynumber".equals(entry))
								{
									poicyNumber =  hashMap.get("Policynumber");
								}							
							}
						}
						StringBuilder insterDate = new StringBuilder(DbConstants.insertpolicyCopy(poicyNumber, "N"));
						System.out.println("query->"+insterDate.toString());
						DbManager.instance.insterData(insterDate.toString());
						NLPResult = "You will be soon receiving the policy copy on your registered email id. Is there anything else I can help you with? ";
					}
				*/
			}else{
				NLPResult = watsonResponse.getText().get(0);
			}
		} else {
			NLPResult = watsonResponse.getText().get(0);
		}
		System.out.println("Watson Response: " + NLPResult);
		context = watsonResponse.getContext();
		return NLPResult;
	}

	public String getIntent() {
		if (watsonResponse.getIntents() != null && !watsonResponse.getIntents().isEmpty()) {
			intents = watsonResponse.getIntents().get(0);
			intent = intents.getIntent();
		} else {
			intent = "";
		}
		return intent;
	}

	public MessageResponse conversationAPI(String input, Map context) {
		ConversationService service = new ConversationService("2017-02-03");
	//	service.setEndPoint("http://202.125.92.65:8080/conversationproxy/soe2");
		service.setUsernameAndPassword("96612e0b-b5cd-497d-9126-1987131fdd14", "ZHSMhUycHBl1");
		MessageRequest newMessage = new MessageRequest.Builder().inputText(input).context(context).build();
		/* dynamic */
		String workspaceId = "eae0c409-16a3-4b73-ab84-e54819ea82ba";
		/* non dynamic */
		/* String workspaceId = "0a6eb48e-a376-49e5-9610-fef915c64d63"; */
		MessageResponse response = service.message(workspaceId, newMessage).execute();
		return response;
	}

	/*
	 * public static void main(String[] args) { ConversationUtility utility =
	 * new ConversationUtility(); utility.conversationUtil(context,
	 * "claim status"); }
	 */
}

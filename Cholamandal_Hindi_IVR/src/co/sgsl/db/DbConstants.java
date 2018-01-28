package co.sgsl.db;

import java.util.Date;

public class DbConstants {

	public final static String USERNAME = "#USERNAME#";
	public final static String BENEFICIARYNAME = "#BENEFICIARYNAME#";
	public final static String BALANCE = "#BALANCE#";
	public final static String ACCOUNT_NUMBER = "#ACCOUNT_NUMBER#";
	public final static String CARDENDDIGITS = "#CARDENDDIGITS#";
	public final static String MOBILENUMBER = "#MOBILENUMBER#";
	public final static String PASSWORD = "#PASSWORD#";
	public final static String CUSTID = "#CUSTID#";
	public final static String FULLNAME = "#FULLNAME#";
	public final static String ACCOUNTTYPE = "#ACCOUNTTYPE#";
	public final static String FROMDATE = "#FROMDATE#";
	public final static String TODATE = "#TODATE#";
	public final static String TIME = "#TIME#";
	public final static String LCOATION = "#LCOATION#";
	public final static String CUSTOMERID ="#CUSTOMERID#";
	public final static String ACCOUNTNUMBER = "#ACCOUNTNUMBER#";
	public final static String USER = "#USER#";

	public final static String CUSTOMER_EMAIL = "#CUSTOMER_EMAIL#";
	public final static String POLICYNUMBER = "#POLICYNUMBER#";
	public final static String VECHILENAME = "#VECHILENAME#";
	public final static String VECHILENUMBER = "#VECHILENUMBER#";
	public final static String CUST_ID = "CUST_ID";
	public final static String PWD = "PWD";
	
	public final static String CONVERSATION_ID="CONVERSATION_ID";
	public final static String TRANSCRIPT="TRANSCRIPT";
	public final static String CURRENT_DATE="CURRENT_DATE";
	
	public final static String POLICY_NUMBER="#POLICYNUMBER#";
	public final static String CLAIM_ID="#CLAIM_ID#";
	
	public final static String POLICY_HOLDER = "#POLICY_HOLDER#";
	
	public final static String FETCH_USER_BY_MOBILE = "Select * from [dbo].[TBL_POLICY_CUSTOMER_LOGIN] where MobileNumber = '#MOBILENUMBER#' and Password = '#PASSWORD#';";
	
	public final static String FETCH_POLICY_NUMBER_AND_VEHICLE_REGISTRATION = "SELECT [vehicle_registration_no],[Policynumber] FROM [dbo].[TBL_MASTER_POLICY] where Mobilenumber ='#MOBILENUMBER#';";
	
	
	public final static String FETCH_POLICY_NUMBER = "SELECT [vehicle_registration_no],[Policynumber] FROM [dbo].[TBL_MASTER_POLICY] where Mobilenumber ='#MOBILENUMBER#';";	

	public final static String FETCH_POLICY_DETILS_WITH_POLICY_NUM = "SELECT [appliedDate] FROM [dbo].[TBL_POLICY_CLAIM] where Claim_ID ='#CLAIM_ID#';";
	
	public final static String FETCH_POLICY_RENEWAL = "SELECT [Policynumber],[risk_end_dt],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where Mobilenumber ='#MOBILENUMBER#';";
	
	public final static String FETCH_CLAIM_STATUS = "Select [cust_reference_num],[Policynumber],[calim_number],[calim_status] from [dbo].[TBL_CLAIM_STATUS] where MobileNumber = '#MOBILENUMBER#';";
	
	public final static String FETCH_POLICY_FROM_MAIN = "SELECT [Policynumber] FROM [dbo].[TBL_MASTER_POLICY] where [reg_status] = '0' ";

	public final static String UPDATE_DETILS_MASTER_POLICY_BY_POLICY_NUMBER = "UPDATE [dbo].[TBL_MASTER_POLICY] SET [Mobilenumber] = '#MOBILENUMBER#' ,[CustomerEmail] = '#CUSTOMER_EMAIL#', [PolicyHolder]='#POLICY_HOLDER#',[reg_status] = '1' WHERE Policynumber='#POLICYNUMBER#'";
	
	public final static String FETCH_POLICY_NUMBER_BY_POLICYNUMBER = "SELECT [vehicle_registration_no],[Policynumber],[vehicle_model],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where Policynumber ='#POLICYNUMBER#' and Mobilenumber ='#MOBILENUMBER#';";
	
	public final static String FETCH_POLICY_NUMBER_BY_VECHILE_NUMBER = "SELECT [vehicle_registration_no],[Policynumber],[vehicle_model],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where vehicle_registration_no ='#VECHILENUMBER#' and Mobilenumber ='#MOBILENUMBER#';";
	
	//SELECT [Policynumber],[vehicle_model],[vehicle_registration_no],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where vehicle_registration_no ='MH-20-CH-0847' and Mobilenumber ='9764533239';
	
	//public final static String UPDATE_MOBILE_NUMBER_IN_USER_LOGIN = "SELECT [Policynumber],[vehicle_model],[vehicle_registration_no],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where vehicle_model ='#VECHILENAME#' and Mobilenumber ='#MOBILENUMBER#';";
	
	public final static String FETCH_POLICY_NUMBER_BY_VECHIL_NAME = "SELECT [vehicle_registration_no],[Policynumber],[vehicle_model],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where vehicle_model ='#VECHILENAME#' and Mobilenumber ='#MOBILENUMBER#';";
	
	public final static String FETCH_DETILS_POLICY_NUMBER = "SELECT [Policynumber],[vehicle_model],[vehicle_registration_no],[Mobilenumber] FROM [dbo].[TBL_MASTER_POLICY] where PolicyDependent ='#VECHILENAME#' and Mobilenumber ='#MOBILENUMBER#';";
	
	public final static String INSERT_CHAT_HISTORY="insert into [dbo].[chat_History] (chat_empid,conversationID,transcript,chatdate)  values ('CUSTOMER_ID','CONVERSATION_ID','TRANSCRIPT','CURRENT_DATE');";
	public static final CharSequence CUSTOMER_ID = "CUSTOMER_ID";
	
	
	public final static String FETCH_NAME_MAIN = "SELECT [PolicyHolder] FROM [dbo].[TBL_MASTER_POLICY] where Mobilenumber ='#MOBILENUMBER#' ";
	
	
	
	
	
	
	public static String insertAccidentClaim(String refNumber , String fromLoction,	String date, String time, String placeHappen , String PolicyNumber, 
			String CRDairyNumber, String accidentDescribe , String reportedToPolice , String status, String applyedDate , String EmpAssign ,String mobileNumber , String vechielNumber ) {

		StringBuilder sb = new StringBuilder(
				"INSERT INTO [dbo].[TBL_POLICY_CLAIM]([Claim_ID],[fromLoction],[date],[time],[placeHappen ],"
				+ "[vechielNum],[CRDairyNumber],[accidentDescribe],[reportedToPolice],[mobileNumber],[empAss],[status],"
				+ "[appliedDate],[refNumber],[Policynumber])VALUES(");
		sb.append("'").append(refNumber).append("',");
		sb.append("'").append(fromLoction).append("',");
		sb.append("'").append(date).append("',");
		sb.append("'").append(time).append("',");
		sb.append("'").append(placeHappen).append("',");
		sb.append("'").append(vechielNumber).append("',");
		sb.append("'").append(CRDairyNumber).append("',");
		sb.append("'").append(accidentDescribe).append("',");
		sb.append("'").append(reportedToPolice).append("',");
		sb.append("'").append(mobileNumber).append("',");
		sb.append("'").append(EmpAssign).append("',");
		sb.append("'").append(status).append("',");
		sb.append("'").append(applyedDate).append("',");
		sb.append("'").append(refNumber).append("',");
		sb.append("'").append(PolicyNumber).append("')");

		return sb.toString();
	}
	
	public static String insertPolicyRenewalUpdate(String PolicyNumber, String status, String renwedDate) {

		StringBuilder sb = new StringBuilder(
				"INSERT INTO [dbo].[TBL_POLICY_RENEW]([Policynumber],[requestStatus] ,[renewed_dt]) VALUES(");
		sb.append("'").append(PolicyNumber).append("',");
		sb.append("'").append(status).append("',");
		sb.append("'").append(renwedDate).append("')");

		return sb.toString();
	}
	
	public static String insertpolicyCopy(String PolicyNumber, String status) {

		StringBuilder sb = new StringBuilder(
				"INSERT INTO [dbo].[TBL_POLICY_COPY]([Policynumber],[requestStatus])VALUES(");
		sb.append("'").append(PolicyNumber).append("',");
		sb.append("'").append(status).append("')");

		return sb.toString();
	}
	
	public static String insertNewCustomer(String MobileNumber, String PolicyHolder,String Password) {

		StringBuilder sb = new StringBuilder("INSERT INTO [dbo].[TBL_POLICY_CUSTOMER_LOGIN]([MobileNumber],[PolicyHolder],[Password])VALUES(");
			sb.append("'").append(MobileNumber).append("',");
			sb.append("'").append(PolicyHolder).append("',");
			sb.append("'").append(Password).append("')");
			return sb.toString();
	}
	
	public static String insertClaimStatus(String Policynumber, String calim_number,String calim_status ,String Mobilenumber) {

		StringBuilder sb = new StringBuilder("INSERT INTO [dbo].[TBL_CLAIM_STATUS]([cust_reference_num],[Policynumber],[calim_number],[calim_status],[Mobilenumber])VALUES('',");
		sb.append("'").append(Policynumber).append("',");
		sb.append("'").append(calim_number).append("',");
		sb.append("'").append(calim_status).append("',");
		sb.append("'").append(Mobilenumber).append("')");
		return sb.toString();
	}
}

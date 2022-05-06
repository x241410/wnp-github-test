package com.telus.wnp.tests;

import java.util.Map;

import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.telus.api.test.utils.APIJava;
import com.telus.wnp.pages.CODASearchPage;
import com.telus.wnp.pages.PACDashboardPage;
import com.telus.wnp.pages.PACSearchPage;
import com.telus.wnp.steps.CODASearchPageSteps;
import com.telus.wnp.steps.LoginPageSteps;
import com.telus.wnp.steps.PACDashboardPageSteps;
import com.telus.wnp.steps.PACSearchPageSteps;
import com.telus.wnp.steps.PublicMobilePageSteps;
import com.telus.wnp.steps.RCMPageSteps;
import com.telus.wnp.steps.RKPortalPageSteps;
import com.telus.wnp.steps.SMGICPPortDetailsPageSteps;
import com.telus.wnp.steps.SMGICPPortSelectionPageSteps;
import com.telus.wnp.steps.SmartDesktopDashboardPageSteps;
import com.telus.wnp.steps.SmartDesktopSearchPageSteps;
import com.telus.wnp.utils.GenericUtils;
import com.test.files.interaction.ReadJSON;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.SystemProperties;

/**
 ****************************************************************************
 * > DESCRIPTION: Public Mobile -inter carrier port out request is rejected with
 * new rejection code 9A > AUTHOR: x241410 > Test Case: TC06_WNP_REG_2FA_Verify
 * that inter carrier port out request is rejected with new rejection code 9A
 * when customer respond NO _Public Mobile
 ****************************************************************************
 */
public class WLSC_169393_WNP_REG_PublicMobilePortOutRej extends BaseTest {

	String publicMobileNum = null;
	String publicMobileBan = null;
	String sub = null;
	String ban = null;
	String internalReqNumFromPAC = null;
	String testCaseName = null;
	String scriptName = null;
	String testCaseDescription = null;
	String environment = null;
	String smgRequestNum = null;
	String testDataFilePath = null;
	JSONObject testDataJson = null;
	JSONObject tnTestData = null;
	JSONObject sdTestData = null;
	JSONObject pacTestData = null;
	JSONObject rcmTestData = null;
	JSONObject codaTestData = null;
	JSONObject smgTestData = null;
	JSONObject lsmsTestData = null;
	JSONObject smsResponseTestData = null;
	JSONObject publicMobileTestData = null;

	/**
	 * @param iTestContext
	 */
	@BeforeTest(alwaysRun = true)
	public void BeforeMethod(ITestContext iTestContext) {
		testCaseName = this.getClass().getName();
		scriptName = GenericUtils.getTestCaseName(testCaseName);
		testCaseDescription = "The purpose of this test case is to verify \"" + scriptName + "\" workflow";

		testDataFilePath = "\\TestData\\" + scriptName + ".json";
		JSONObject jsonFileObject = GenericUtils.getJSONObjectFromJSONFile(testDataFilePath);
		environment = SystemProperties.EXECUTION_ENVIRONMENT;
		testDataJson = jsonFileObject.getJSONObject(environment);

		tnTestData = testDataJson.getJSONObject("SUB_AND_BAN_DETAILS");
		sdTestData = testDataJson.getJSONObject("SMART_DESKTOP_CONSTANTS");
		pacTestData = testDataJson.getJSONObject("PAC_CONSTANTS");
		rcmTestData = testDataJson.getJSONObject("RCM_CONSTANTS");
		codaTestData = testDataJson.getJSONObject("CODA_CONSTANTS");
		smgTestData = testDataJson.getJSONObject("SMG_CONSTANTS");
		publicMobileTestData = testDataJson.getJSONObject("PUBLIC_MOBILE_CONSTANTS");
		lsmsTestData = testDataJson.getJSONObject("LSMS_CONSTANTS");
		smsResponseTestData = testDataJson.getJSONObject("SMS_RESPONSE_CONSTANTS");
		
		sub = tnTestData.getString("TELUS_SUB");
		ban = tnTestData.getString("TELUS_BAN");
	}

	@Test(groups = { "WLSC_169393_WNP_REG_PublicMobilePortOutRej", "Port-Out", "CompleteRegressionSuite" })
	public void testMethod_PublicMobileToForeign(ITestContext iTestContext) throws Exception {

		Reporting.setNewGroupName("Automation Configurations / Environment Details & Data Setup");
		Reporting.logReporter(Status.INFO,
				"Automation Configuration - Environment Configured for Automation Execution [" + environment + "]");
		Reporting.logReporter(Status.INFO, "Test Data File for " + scriptName + " placed at : " + testDataFilePath);
		Reporting.printAndClearLogGroupStatements();

		/*** Test Case Details ***/
		Reporting.setNewGroupName("Test Case Details");
		Reporting.logReporter(Status.INFO, "Test Case Name : [" + scriptName + "]");
		Reporting.logReporter(Status.INFO, "Test Case Description : [" + testCaseDescription + "]");
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM SMARTDESKTOP ***/
		Reporting.setNewGroupName("SMART DESKTOP - SUB PRE VALIDATION");
		SmartDesktopSearchPageSteps.verifySubscriberStatusFromSD(sdTestData, sub, ban, "active");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER PORTING STATUS CHECK FROM PAC ***/
		Reporting.setNewGroupName("PAC - SUB PREVALIDATION");
		PACSearchPageSteps.verifyNoPortReqForSubFromPAC(pacTestData, sub);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		
		/*** STEPS FOR ACTIVATE PUBMIC MOBILE ***/
		Reporting.setNewGroupName("PUBLIC MOBILE NUMBER ACTIVATION");

		PublicMobilePageSteps.activatePublicMobileNumber(publicMobileTestData, "", "");
		publicMobileNum = PublicMobilePageSteps.activatedPublicMobileNumber;
		publicMobileBan = PublicMobilePageSteps.activatedPublicMobileAccountNumber;

		publicMobileNum = publicMobileNum.replaceAll(" ", "").replaceAll("-", "").replaceAll("\\)", "")
				.replaceAll("\\(", "");

		Reporting.logReporter(Status.INFO, "Activated publicMobileNum is: " + publicMobileNum);
		Reporting.logReporter(Status.INFO, "Activated publicMobileBan is: " + publicMobileBan);

		System.out.println("Activated publicMobileNum is: " + publicMobileNum);
		System.out.println("Activated publicMobileBan is: " + publicMobileBan);

		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();



		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM CODA ***/
		Reporting.setNewGroupName("CODA - SUB PREVALIDATION");
		CODASearchPageSteps.verifySubscriberStatusFromCODA(codaTestData, publicMobileNum, "active");
		CODASearchPageSteps.verifySubOrderStatus(codaTestData, publicMobileNum);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();
		/**
		 * 
		 * pt 148 test data publicMobileNum = "4161766775"; publicMobileBan =
		 * "10000001922011";
		 * 
		 */

		/*** STEPS FOR SUBSCRIBER PORTING STATUS CHECK FROM PAC ***/
		Reporting.setNewGroupName("PAC - SUB PREVALIDATION");
		PACSearchPageSteps.verifyNoPortReqForSubFromPAC(pacTestData, publicMobileNum);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR NAVIGATE TO SMG AND INITIATE PORT OUT REQUEST ***/
		Reporting.setNewGroupName("SMG - PORT OUT REQUEST");
		SMGICPPortDetailsPageSteps.saveCreatePortRequestDetailsForSingleSub(smgTestData, publicMobileNum,
				publicMobileBan, "portout");
		smgRequestNum = SMGICPPortDetailsPageSteps.SMGRequestNumber;
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT SUMMARY VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, publicMobileNum, "preValidation");
		internalReqNumFromPAC = PACDashboardPageSteps.getPACRequestNumber();
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM SMARTDESKTOP ***/
		Reporting.setNewGroupName("SMART DESKTOP - COMM HISTORY VALIDATION");
		SmartDesktopSearchPageSteps.verifySubscriberStatusFromSD(sdTestData, sub, ban, "active");
		SmartDesktopDashboardPageSteps.navigateToCommunicationHistoryTabToVerifySMSDetails(sdTestData, publicMobileNum);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR VERIFYING 8k DELAY CODE FROM SMG ***/
		Reporting.setNewGroupName("SMG - DELAY CODE 8k VALIDATION");
		SMGICPPortDetailsPageSteps.navigateToSMGAndVerifyDelyCode8k(smgTestData, publicMobileNum);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR RESPONDING NO TO 2FA SMS AUTHENTICATION **/
		Reporting.setNewGroupName("2FA SMS API CALL - RESPONDED NO");
		Reporting.logReporter(Status.INFO, "STEP===Responding No to 2FA SMS===");

		String expectedStatus = smsResponseTestData.getString("EXPECTED_STATUS");
		String response = smsResponseTestData.getString("2FA_SMS_RESPONSE");
		String laguageCode = smsResponseTestData.getString("LANGUAGE_CODE");
		String localDate = GenericUtils.getFormattedSystemDate("yyyy-mm-ddThh:mm:ss:ttt");

		System.setProperty("karate.pacInternalReqNum", internalReqNumFromPAC);
		System.setProperty("karate.TN", publicMobileNum);
		System.setProperty("karate.responseYN", response);
		System.setProperty("karate.dateTime", localDate);
		System.setProperty("karate.languageCode", laguageCode);
		System.setProperty("karate.BAN", publicMobileBan);

		Map<String, Object> apiOperation1 = APIJava.runKarateFeature(environment,
				"classpath:tests/WNP/Test_2FA_SMS_Response.feature");

		Reporting.logReporter(Status.INFO,
				"API Operation status: " + apiOperation1.get("sendResponseSMSAPIfeatureCallStatus"));
		Reporting.logReporter(Status.INFO, "API SIZE : " + apiOperation1.size());
		Reporting.logReporter(Status.INFO,
				"API Operation Request: " + apiOperation1.get("sendResponseSMSAPIfeatureCallRequest"));
		Reporting.logReporter(Status.INFO,
				"API Operation response: " + apiOperation1.get("sendResponseSMSAPIfeatureCallResponse"));

		String actualStatus = apiOperation1.get("status").toString();
		String actualStatusMsg = apiOperation1.get("statusMessage").toString();
		String actualOrderId = apiOperation1.get("orderId").toString();

		Validate.assertEquals(actualStatus, expectedStatus, "2fa sms response mismtach", true);
		Validate.assertEquals(actualOrderId, internalReqNumFromPAC, "2fa sms order Id mismtach", true);
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR VERIFYING 9A DELAY CODE FROM SMG ***/
		Reporting.setNewGroupName("SMG - REJECTION CODE VALIDATION");
		SMGICPPortDetailsPageSteps.navigateToSMGAndVerifyRejectionCode9A(smgTestData, publicMobileNum);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT SUMMARY VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, publicMobileNum, "postValidation");
		PACDashboardPageSteps.verifySMSDetailsCapturedUnderNotesInPAC(pacTestData, "no");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();


		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM CODA ***/
		Reporting.setNewGroupName("CODA - SUB POSTVALIDATION");
		CODASearchPageSteps.verifySubscriberStatusFromCODA(codaTestData, publicMobileNum, "active");
		CODASearchPageSteps.verifyCurrentBrandFromCODA("PUBLIC_MOBILE");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

	}

	/**
	 * Close any opened browser instances
	 */
	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		Reporting.setNewGroupName("Close All Browser");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();
	}
}

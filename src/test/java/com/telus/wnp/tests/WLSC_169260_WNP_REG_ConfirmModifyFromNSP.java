package com.telus.wnp.tests;

import java.util.Map;

import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.intuit.karate.XmlUtils;
import com.telus.api.test.utils.APIJava;
import com.telus.wnp.steps.CODASearchPageSteps;
import com.telus.wnp.steps.LoginPageSteps;
import com.telus.wnp.steps.PACDashboardPageSteps;
import com.telus.wnp.steps.PACSearchPageSteps;
import com.telus.wnp.steps.RCMPageSteps;
import com.telus.wnp.steps.SMGICPPortDetailsPageSteps;
import com.telus.wnp.steps.SMGICPPortSelectionPageSteps;
import com.telus.wnp.steps.SmartDesktopDashboardPageSteps;
import com.telus.wnp.steps.SmartDesktopSearchPageSteps;
import com.telus.wnp.utils.GenericUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.SystemProperties;

/**
 ****************************************************************************
 * > DESCRIPTION: Port Out_Telus To Foreign Portout Modify from NSP > AUTHOR:
 * x241410 > Test Case: TC10_WNP_REG_Port Out_Confirm _Modify from NSP(Modify
 * Due Date, Modify LRN)
 ****************************************************************************
 */

public class WLSC_169260_WNP_REG_ConfirmModifyFromNSP extends BaseTest {

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
		lsmsTestData = testDataJson.getJSONObject("LSMS_CONSTANTS");
		smsResponseTestData = testDataJson.getJSONObject("SMS_RESPONSE_CONSTANTS");

		sub = tnTestData.getString("TELUS_SUB");
		ban = tnTestData.getString("TELUS_BAN");

	}

	@Test(groups = { "WLSC_169260_WNP_REG_ConfirmModifyFromNSP", "Port-Out", "CompleteRegressionSuite" })
	public void testMethod_PortOutConfirmModifyFromNSP(ITestContext iTestContext) throws Exception {

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
		Reporting.setNewGroupName("SMART DESKTOP - SUB PREVALIDATION");
		SmartDesktopSearchPageSteps.verifySubscriberStatusFromSD(sdTestData, sub, ban, "active");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM RCM ***/
		Reporting.setNewGroupName("RCM - SUB PREVALIDATION");
		RCMPageSteps.verifySubscriberStatusFromRCM(rcmTestData, sub, "active");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM CODA ***/
		Reporting.setNewGroupName("CODA - SUB PREVALIDATION");
		CODASearchPageSteps.verifySubscriberStatusFromCODA(codaTestData, sub, "active");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER PORTING STATUS CHECK FROM PAC ***/
		Reporting.setNewGroupName("PAC - SUB PREVALIDATION");
		PACSearchPageSteps.verifyNoPortReqForSubFromPAC(pacTestData, sub);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR NAVIGATE TO SMG AND INITIATE PORT OUT REQUEST ***/
		Reporting.setNewGroupName("SMG - PORT OUT REQUEST");
		SMGICPPortDetailsPageSteps.saveCreatePortRequestDetailsWithModifiableDDDTAndLRN(smgTestData, sub, ban,
				"portout", "dddtandlrn");
		smgRequestNum = SMGICPPortDetailsPageSteps.SMGRequestNumber;
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT SUMMARY VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS & SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, sub, "preValidation");
		internalReqNumFromPAC = PACDashboardPageSteps.getPACRequestNumber();
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR VERIFYING 8k DELAY CODE FROM SMG ***/
		Reporting.setNewGroupName("SMG - DELAY CODE 8k VALIDATION");
		SMGICPPortDetailsPageSteps.navigateToSMGAndVerifyDelyCode8k(smgTestData, sub);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/***
		 * STEPS FOR VALIDATING 2WAY SMS LOGS IN COMMUNICATION HISTORY TAB FROM
		 * SMARTDESKTOP
		 ***/
		Reporting.setNewGroupName("SMART DESKTOP - COMMUNICATION HISTORY TAB VALIDATION");
		SmartDesktopSearchPageSteps.verifySubscriberStatusFromSD(sdTestData, sub, ban, "active");
		SmartDesktopDashboardPageSteps.navigateToCommunicationHistoryTabToVerifySMS(sdTestData, sub);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR RESPONDING YES TO 2FA SMS AUTHENTICATION **/
		Reporting.setNewGroupName("2FA SMS API CALL - RESPONDED YES");
		Reporting.logReporter(Status.INFO, "STEP===Responding Yes to 2FA SMS===");

		String expectedStatus = smsResponseTestData.getString("EXPECTED_STATUS");
		String response = smsResponseTestData.getString("2FA_SMS_RESPONSE");
		String laguageCode = smsResponseTestData.getString("LANGUAGE_CODE");
		String localDate = GenericUtils.getFormattedSystemDate("yyyy-mm-ddThh:mm:ss:ttt");

		System.setProperty("karate.pacInternalReqNum", internalReqNumFromPAC);
		System.setProperty("karate.TN", sub);
		System.setProperty("karate.responseYN", response);
		System.setProperty("karate.dateTime", localDate);
		System.setProperty("karate.languageCode", laguageCode);
		System.setProperty("karate.BAN", ban);

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

		/*** STEPS FOR PORT SUMMARY VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, sub, "intermediate1");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR NAVIGATE TO SMG AND INITIATE PORT OUT REQUEST ***/
		Reporting.setNewGroupName("SMG - PORT OUT REQUEST");
		String dddtDate = GenericUtils.getDDDTWithDelay(smgTestData.getInt("DDDT_DELAY_IN_MINUTES"));
		Reporting.logReporter(Status.INFO, "updated DDDT is: " + dddtDate);
		System.out.println("dddtDate:  " + dddtDate);
		SMGICPPortDetailsPageSteps.updateDDDTAndLRNFromSMG(smgTestData, sub, dddtDate);
		smgRequestNum = SMGICPPortDetailsPageSteps.SMGRequestNumber;
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT SUMMARY VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, sub, "intermediate2");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEP FOR DDDT Required Wait ***/
		Reporting.setNewGroupName("WAIT FOR DDDT MINUTES");
		Reporting.logReporter(Status.INFO, "Step === Wait for defined DDDT ===");
		int DelayTime = smgTestData.getInt("DDDT_DELAY_IN_MINUTES");
		int delayTimeInSeconds = (DelayTime + 1) * 60 * 1000;
		System.out.println("WAIT STARTED FOR ========> " + DelayTime + " MINUTES.");
		BaseSteps.Waits.waitGeneric(delayTimeInSeconds);
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT SUMMARY VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, sub, "postValidation");
		PACDashboardPageSteps.checkModifiedDDTFromPAC(dddtDate);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR VERIFYING REQUEST COMPLETION FROM SMG ***/
		Reporting.setNewGroupName("SMG - REQUEST COMPLETION VALIDATION");
		String expectedLRN = smgTestData.getString("LRN_ID");
		SMGICPPortSelectionPageSteps.verifyUpdatedLRNValueFromSMG(smgTestData, sub, expectedLRN);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR VERIFYING THE SUBSCRIBER SPID AS TU02 ***/
		Reporting.setNewGroupName("LSMS API - SPID POSTVALIDATION");
		Reporting.logReporter(Status.INFO, "STEP === SPID Validation --> SPID validation check via API call===");

		System.setProperty("karate.TN", sub);

		Map<String, Object> apiOperation = APIJava.runKarateFeature(environment,
				"classpath:tests/WNP/Test_LSMS_SPID.feature");

		Reporting.logReporter(Status.INFO, "API Operation status: " + apiOperation.get("spidAPIfeatureCallStatus"));
		Reporting.logReporter(Status.INFO, "API SIZE : " + apiOperation.size());
		Reporting.logReporter(Status.INFO, "API Operation Request: " + apiOperation.get("spidAPIfeatureCallRequest"));

		Reporting.logReporter(Status.INFO, "API Operation response: " + apiOperation.get("spidAPIfeatureCallResponse"));
		Reporting.logReporter(Status.INFO, "API Operation response Soap conversion: "
				+ XmlUtils.toXml(apiOperation.get("spidAPIfeatureCallResponse")));

		String expectedSPID = lsmsTestData.getString("EXPECTED_SPID");
		String resp = XmlUtils.toXml(apiOperation.get("spidAPIfeatureCallResponse"));
		String actualSPID = GenericUtils.returnKeyValueFromXMLNode(resp, "lspId");
		String actualLRN = GenericUtils.returnKeyValueFromXMLNode(resp, "lrn");
		Validate.assertEquals(actualSPID, expectedSPID, "SPID is not as expected", true);
		Validate.assertEquals(actualLRN, expectedLRN, "LRN is not as expected", true);
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM SMARTDESKTOP ***/
		Reporting.setNewGroupName("SMART DESKTOP - SUB POSTVALIDATION");
		SmartDesktopSearchPageSteps.verifySubscriberStatusFromSD(sdTestData, sub, ban, "cancelled");
		SmartDesktopDashboardPageSteps.verifyPortingInfo(sdTestData);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM CODA ***/
		Reporting.setNewGroupName("CODA - SUB POSTVALIDATION");
		CODASearchPageSteps.verifySubscriberStatusFromCODA(codaTestData, sub, "cancelled");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM RCM ***/
		Reporting.setNewGroupName("RCM - SUB POSTVALIDATION");
		RCMPageSteps.verifySubscriberStatusFromRCM(rcmTestData, sub, "cancelled");
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

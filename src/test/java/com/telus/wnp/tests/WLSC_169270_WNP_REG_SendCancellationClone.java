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
import com.telus.wnp.pages.PACDashboardPage;
import com.telus.wnp.steps.CODASearchPageSteps;
import com.telus.wnp.steps.PACDashboardPageSteps;
import com.telus.wnp.steps.PACSearchPageSteps;
import com.telus.wnp.steps.RCMPageSteps;
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
 * > DESCRIPTION: Send Cancellation to External Service Provider : Cancel and
 * Clone > AUTHOR: x241410 > Test Case: TC06_WNP_REG_Send Cancellation to from
 * External Service Provider _Cancel and Clone
 ****************************************************************************
 */

public class WLSC_169270_WNP_REG_SendCancellationClone extends BaseTest {

	String sub = null;
	String ban = null;
	String foreignSub = null;
	String foreignBan = null;
	String testDataFilePath = null;
	String testCaseName = null;
	String scriptName = null;
	String expectedStatus = null;
	String actualStatus = null;
	String formattedSub = null;
	String testCaseDescription = null;
	String environment = null;
	JSONObject testDataJson = null;
	JSONObject tnTestData = null;
	JSONObject sdTestData = null;
	JSONObject pacTestData = null;
	JSONObject rcmTestData = null;
	JSONObject codaTestData = null;
	JSONObject smgTestData = null;
	JSONObject lsmsTestData = null;

	/**
	 * @param iTestContext
	 */
	@BeforeTest(alwaysRun = true)
	public void BeforeMethod(ITestContext iTestContext) {
		testCaseName = this.getClass().getName();
		scriptName = GenericUtils.getTestCaseName(testCaseName);
		testCaseDescription = "The purpose of this test case is to verify \"" + scriptName + "\" workflow.";

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

		sub = tnTestData.getString("TELUS_SUB");
		ban = tnTestData.getString("TELUS_BAN");
		foreignSub = tnTestData.getString("FOREIGN_SUB");
		foreignBan = tnTestData.getString("FOREIGN_BAN");

	}

	@Test(groups = { "WLSC_169270_WNP_REG_SendCancellationClone", "SendCancelation", "CompleteRegressionSuite", "Batch90" })
	public void testMethod_SendCancellationClone(ITestContext iTestContext) throws Exception {

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

		/*** STEPS FOR SUBSCRIBER PORTING STATUS CHECK FROM PAC ***/
		Reporting.setNewGroupName("PAC - SUB PREVALIDATION");
		PACSearchPageSteps.verifyNoPortReqForSubFromPAC(pacTestData, sub);
		PACSearchPageSteps.verifyNoPortReqForAnotherSubFromPAC(pacTestData, foreignSub);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM CODA ***/
		Reporting.setNewGroupName("CODA - SUB PREVALIDATION");
		CODASearchPageSteps.verifySubscriberStatusFromCODA(codaTestData, sub, "active");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR VERIFYING THE SUBSCRIBER SPID AS TU02 ***/
		Reporting.setNewGroupName("LSMS API - SPID POSTVALIDATION");
		Reporting.logReporter(Status.INFO, "STEP === SPID Validation --> SPID validation check via API call===");

		System.setProperty("karate.TN", foreignSub);

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
		Validate.assertEquals(actualSPID, expectedSPID, "SPID is not as expected", true);
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR NAVIGATE TO SMARTDESKTOP AND PERFORM OSP CANCEL REQUEST ***/
		Reporting.setNewGroupName("SMART DESKTOP - SUBMIT OSP CANCEL REQUEST");
		SmartDesktopDashboardPageSteps.submitOSPCancelReqFromSubscriberTab(sdTestData, sub, ban, foreignSub,
				foreignBan);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT STATUS VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, foreignSub, "preValidation");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT CREATE CANCEL SVC RESPONSE FROM SMG ***/
		Reporting.setNewGroupName("SMG - CREATE CANCEL SVC RESPONSE");
		SMGICPPortSelectionPageSteps.createCancelSVCResponseFromSMG(smgTestData, foreignSub, sub, "");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR CANCEL AND CLONE RQUEST FROM PAC ***/
		Reporting.setNewGroupName("PAC - CANCEL AND CLONE REQUEST");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, foreignSub, "intermediate1");
		PACDashboardPageSteps.cancelRequestFromPAC(pacTestData);

		PACDashboardPage PACDashboardPage = new PACDashboardPage();
		BaseSteps.Waits.waitForElementToBeClickableLongWait(PACDashboardPage.refreshBtn);
		BaseSteps.Clicks.clickElement(PACDashboardPage.refreshBtn);
		PACDashboardPageSteps.cloneRequestFromPACWithoutModifyingAnything(pacTestData, foreignBan);
		BaseSteps.Clicks.clickElement(PACDashboardPage.searchPageLinkBtn);
		PACSearchPageSteps.searchPhoneNo(foreignSub);
		PACDashboardPageSteps.handleMultipleRecordsDisplayed();

		PACDashboardPageSteps.verifyPortSummary(pacTestData, "preValidation");

		String expectedPortStatus = pacTestData.getString("PORT_STATUS_BEFORE");
		String actualPortStatus = PACDashboardPage.getPortStatus();
		Validate.assertEquals(actualPortStatus, expectedPortStatus, "Port status is not as expected", false);
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT CREATE CANCEL SVC RESPONSE FROM SMG ***/
		Reporting.setNewGroupName("SMG - AGAIN CONFIRM CREATE CANCEL SVC RESPONSE");
		SMGICPPortSelectionPageSteps.createCancelSVCResponseFromSMG(smgTestData, foreignSub, sub, "");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEP FOR DDDT WAIT ***/
		Reporting.setNewGroupName("WAIT FOR DEFINED DDDT MINUTES");
		Reporting.logReporter(Status.INFO, "Step === Wait for defined DDDT ===");
		int DelayTime = smgTestData.getInt("NEW_DDDT_DELAY_IN_MINUTES") + 1;
		//int DelayTime = 90;
		int delayTimeInSeconds = DelayTime * 60 * 1000;
		BaseSteps.Waits.waitGeneric(delayTimeInSeconds);
		Reporting.logReporter(Status.INFO, "Wait for defined DDDT of " + DelayTime + " minutes.");
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR PORT STATUS VALIDATION FROM PAC ***/
		Reporting.setNewGroupName("PAC - PORT STATUS AND SUMMARY VALIDATION");
		PACDashboardPageSteps.VerifyPortSummaryAndPortStatus(pacTestData, foreignSub, "postValidation");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		// *** STEPS FOR VALIDATE REQUEST STATUS FROM SMG **
		Reporting.setNewGroupName("SMG - VERIFY REQUEST STATUS");
		SMGICPPortSelectionPageSteps.verifyRequestStatusFromICPPortSelectionPage(foreignSub, "Port Out",
				smgTestData.getString("EXTERNAL_TU02_MOBILITY"), smgTestData.getString("SMG_STATUS_CONFIRMED"));
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM SMARTDESKTOP ***/
		Reporting.setNewGroupName("SMART DESKTOP - SUB POST VALIDATION");
		SmartDesktopSearchPageSteps.verifySubscriberStatusFromSD(sdTestData, sub, ban, "cancelled");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM CODA ***/
		Reporting.setNewGroupName("CODA - SUB POST VALIDATION");
		CODASearchPageSteps.verifySubscriberStatusFromCODA(codaTestData, sub, "cancelled");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();

		/*** STEPS FOR SUBSCRIBER ACTIVE STATUS FROM RCM ***/
		Reporting.setNewGroupName("RCM - SUB PREVALIDATION");
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

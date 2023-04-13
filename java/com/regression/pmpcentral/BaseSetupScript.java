package com.regression.pmpcentral;



import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;


import com.utilities.DriverFactory;
import com.utilities.DriverFactory.BrowserType;
import com.utilities.ExcelFactory;

public class BaseSetupScript {
	
	protected WebDriver driver;
	public String sEnv, sBrowser, snapShotFileLoc, reportFileLoc, snapShotImgLoc, txtLoanNumber, sContactName= "auto1234", txtSellerName; 
	public String fileSeperator = System.getProperty("file.separator");
	public Assertion hardAssert = new Assertion();
	public SoftAssert softAssert = new SoftAssert();
	public JavascriptExecutor jse = (JavascriptExecutor)driver;
	int intLoanNumber;
	
	
	Date now = new Date();
	
	//FileHandling Location
	public String testDir = System.getProperty("user.dir");
	public String uploadFileLoc = testDir+ fileSeperator+ "src\\test\\resources"+ fileSeperator+  "FileHandling"+ fileSeperator;
	public String downloadFileLocation = testDir+ fileSeperator+ "src\\test\\resources"+ fileSeperator+  "DownloadFile"+ fileSeperator;	
	public String downloadFilepath=downloadFileLocation+"Loans.xls";
	//DateFormat.getInstance().format(now);
	SimpleDateFormat timeformatter = new SimpleDateFormat("hhmm");
	SimpleDateFormat dayformatter = new SimpleDateFormat("MMdd");
	public String timeValue = timeformatter.format(now);
	public String dateValue = dayformatter.format(now);
	
	
	//Initialize pages and external classes
	
	
	
	
	@BeforeClass(alwaysRun = true)
	@Parameters({"Browser","Env"})
	public void setup(@Optional("Chrome") String Browser, @Optional("QA") String Env){
		//Set environment
		sEnv = Env;	
		sBrowser = Browser;		
		
		switch(Browser){
			case "Chrome":
				DriverFactory.BrowserType type = BrowserType.CHROME;
				driver = DriverFactory.getDriver(type);
				break;
				
			case "IE":
				DriverFactory.BrowserType type1 = BrowserType.IE;
				driver = DriverFactory.getDriver(type1);
				break;
				
			case "FireFox":
				DriverFactory.BrowserType type2 = BrowserType.FIREFOX;
				driver = DriverFactory.getDriver(type2);
				break;
		}
		
		//Initialize Page Classes
		
		
	}
		
	@AfterClass(alwaysRun = true)//@AfterClass(alwaysRun = true)
	public void teardown(){
		DriverFactory.close(driver);
	}
}
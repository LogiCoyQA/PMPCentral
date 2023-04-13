package com.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;

import com.regressionsuite.pmpcentral.BaseSetupScript;

public class DriverFactory {
	
	public enum BrowserType{
		FIREFOX,
		CHROME,
		IE,
		SAFARI
	}
public static WebDriver driver;
	
	public static WebDriver getDriver(BrowserType type){
		
		driver = null;
			switch(type){
				case FIREFOX:
					DesiredCapabilities dcap=new DesiredCapabilities();
					dcap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
					driver = new FirefoxDriver(dcap);
					break;
					
				case CHROME:
					System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
					//Set download loc
					String fileSeperator = System.getProperty("file.separator");
					String testDir = System.getProperty("user.dir");
					String uploadFileLoc = testDir+ fileSeperator+ "src\\test\\resources"+ fileSeperator+  "FileHandling"+ fileSeperator;
					String downloadFilepath = testDir+ fileSeperator+ "src\\test\\resources"+ fileSeperator+  "DownloadFile"+ fileSeperator;	

					
					//Turn off Save Password dialog
					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					chromePrefs.put("download.default_directory", downloadFilepath);
					chromePrefs.put("credentials_enable_service", false);
					chromePrefs.put("profile.password_manager_enabled", false);
					ChromeOptions options = new ChromeOptions();
					options.setExperimentalOption("prefs", chromePrefs);
					options.addArguments("--disable-extensions"); 
					options.addArguments("test-type");
					options.addArguments("start-maximized");
					
					DesiredCapabilities dc = DesiredCapabilities.chrome();
					dc.setCapability(ChromeOptions.CAPABILITY, options);
					dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.IGNORE);
					dc.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
					dc.setCapability("chrome.switches", Arrays.asList("--incognito"));
					driver = new ChromeDriver(dc);
					break;
					
				case IE:
					System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
					driver = new InternetExplorerDriver();
					break;
					
				default:
					driver = new FirefoxDriver();
					break;
			}
				
		return driver;
	}
	
	public static WebDriver getDriveFactoryDriver(){
		return driver;
	}
	
	public static void close(WebDriver wDriver){
		System.out.println("Closing browser");
		wDriver.quit();
		wDriver = null;
	}
}
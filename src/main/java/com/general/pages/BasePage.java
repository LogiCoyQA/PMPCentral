package com.general.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.utilities.DriverFactory;

public class BasePage {
	public String PAGE_URL, PAGE_TITLE;
	public WebDriver driver;
	public String fileSeperator = System.getProperty("file.separator");
	public String testDir = System.getProperty("user.dir");
	public String uploadFileLoc = testDir+ fileSeperator+ "src\\test\\resources"+ fileSeperator+  "FileHandling"+ fileSeperator;
	public String downloadFileLocation = testDir+ fileSeperator+ "src\\test\\resources"+ fileSeperator+  "DownloadFile"+ fileSeperator;	
	public String downloadFilepath=downloadFileLocation+"Loans.xls";
	
	//Generate current date/time for a timestamp	
	Date now = new Date();
	SimpleDateFormat timeformatter = new SimpleDateFormat("hhmm");
	SimpleDateFormat dayformatter = new SimpleDateFormat("MMdd");
	String timeValue = timeformatter.format(now);
	String dateValue = dayformatter.format(now);
	public BasePage(WebDriver wDriver){
		driver = wDriver;
	}
	
	public String captureSnapShot(WebDriver wDriver, String sSnapShotLoc, String screenShotName){
		File fSnapshot =((TakesScreenshot)wDriver).getScreenshotAs(OutputType.FILE);
		UUID uuid = UUID.randomUUID();
		String fileLocation = sSnapShotLoc+ "snapShots"+ File.separator+ screenShotName+".png";

		try {
			FileUtils.copyFile(fSnapshot, new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String imgLocation = screenShotName+ ".png";
		return imgLocation;
	}
	
	public void safeJavaScriptClick(WebElement element) throws Exception {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				reportNote("Clicking on element with using java script click");

				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].scrollIntoView();",element);
				jse.executeScript("arguments[0].click();", element);
			} else {
				reportNote("Unable to click on element");
			}
		} catch (StaleElementReferenceException e) {
			reportNote("Element is not attached to the page document "+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			reportNote("Element was not found in DOM "+ e.getStackTrace());
		} catch (Exception e) {
			reportNote("Unable to click on element "+ e.getStackTrace());
		}
	}
	
	public void waitForDownloadFile(File file,int timeInSecond) throws Exception{
		int count=0;
		while (!file.exists()) {
			count++;
		    Thread.sleep(4000);
		    if(count==timeInSecond)
		    reportFailNote("unable to download file");
		}
		if(count<timeInSecond){
			reportPassNote("Download File Found: "+file);
		}
	}
	
	public WebDriver getDriver(){
		driver = DriverFactory.getDriveFactoryDriver();
		return driver;
	}
	
	public String getElementText(WebElement element){
		return element.getText();
	}
	
	public String getPageTitle(){
		return driver.getTitle();//PAGE_TITLE;
	}
	
	public String getPageURL(){
		return driver.getCurrentUrl();//PAGE_URL;
	}
	
	public boolean isElementVisible(final By by) throws InterruptedException {
        boolean value = false;

        if (driver.findElements(by).size() > 0) {
            value = true;
        }
        return value;
    }

	public void loadPage(String Url, String Title){
		driver.get(Url);
		Assert.assertEquals(driver.getTitle(), Title);
	}
	
	public String pageSnapShot(WebDriver wDriver, String sSnapShotLoc){
		File fSnapshot =((TakesScreenshot)wDriver).getScreenshotAs(OutputType.FILE);
		UUID uuid = UUID.randomUUID();
		String imgLocation = sSnapShotLoc+ ".png";
		
		try {
			FileUtils.copyFile(fSnapshot, new File(imgLocation));
		} catch (IOException e) {
			
			reportNote(e.getMessage());
		}
		return imgLocation;
	}
	
	public void reportFailNote(String text){
		reportNote("FAILURE: "+ text);
		Assert.assertTrue(false, "FAILURE: "+ text);
	}
	
	//========== Reporting Methods  ============================
	public void reportNote(String text){
		System.out.println(text);
	}
	
	public void reportPassNote(String text){
		reportNote("PASS: "+ text);
		Assert.assertTrue(true);
	}
	
	public void validateWebElement(WebElement element,String store,String message){
		if(element.getText()==null){
			reportFailNote("Element is not present on the WebPage");
		}else{
			//Store the value
	        store=element.getText();
	        reportPassNote(message+": "+store);
		}
	}

	public void reportStatusNote(String sStatus, String text){
		switch(sStatus){
			case "Pass":
				reportNote("PASS: "+ text);
				Assert.assertTrue(true);
				break;
				
			case "Fail":
				reportNote("FAILURE: "+ text);
				Assert.assertTrue(false);
				break;
				
			case "Info":
				reportNote("INFO: "+ text);
				break;
				
			case "Warning":
				reportNote("WARNING: "+ text);
				break;
				
			default:
				reportNote("PASS: "+ text);
				Assert.assertTrue(true);
				break;
		}
	}
	
	public void returnKeyStroke(WebElement element){
		element.sendKeys(Keys.RETURN);
	}
	
	public void scrollToElement(WebElement element){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView();", element);
	}
	
	public void scrollToTop(){
		JavascriptExecutor jse6 = (JavascriptExecutor)driver;
		jse6.executeScript("scroll(900,0)");
	}
	
	public boolean selectListValue(WebElement element, String value){
		boolean chkItemSet = false;
		
		reportNote("Set "+ element.getTagName()+ "[List] to '"+ value+ "'.");
		if(validateValueInList(element, value) == true){
			Select oElement = new Select(element);
			oElement.selectByVisibleText(value);
			chkItemSet = true;
		}
		return chkItemSet;
	}
	
	public void setElementText(WebElement element, String text){
		try{
			reportNote("Set the "+ element.getTagName()+"[field] to: "+ text+".");
			
			element.clear();
			element.sendKeys(text);Thread.sleep(3000);	
			element.sendKeys(Keys.TAB);
		}catch (Exception e) {
			reportNote(e.getMessage());
		}		
	}
	
	
	public void validatePageTitle(String pgTitle){
		Assert.assertEquals(driver.getTitle(), pgTitle);
	}
	
	public boolean validatePageURL(String pgUrl){
		boolean chkURL = false;
		String sCurrURL = getPageURL();
		
		if(sCurrURL.contains(pgUrl) == true){
			reportPassNote("Navigated successfully to ["+ sCurrURL+"].");
			chkURL = true;
		}else{
			reportFailNote("URL["+sCurrURL+"] did not contain ["+pgUrl+"].");
		}
		return chkURL;
	}
	
	public boolean validateValueInList(WebElement element, String value){
		boolean chkValue = false;
		
		String eName = element.getAttribute("name");
		Select eList = new Select(element);
		
		int lstCount = eList.getOptions().size();
		
		if(lstCount > 0){
			for(int i=0;i<=lstCount-1;i++){
				String sValue = eList.getOptions().get(i).getText();
				if(sValue.equalsIgnoreCase(value)){
					chkValue = true;
					break;
				}
			}
			if(chkValue == true){
				reportNote(eName+"[list] contains ["+value+"]value.");
			}else{
				reportNote(value+"[value] not found in "+eName+"[list].");
			}
		}
		return chkValue;
	}
	
	public boolean verifyElementExists(WebDriver wDriver, String locBy, String sLocator)throws InterruptedException{
		int iCount = 0;
		boolean isPresent = false;
		while(iCount < 10){
			switch(locBy){
				case "id":
					if (wDriver.findElements(By.id(sLocator)).size() > 0) {
			            isPresent = true;
			        }
					break;
					
				case "xpath":
					if (wDriver.findElements(By.xpath(sLocator)).size() > 0) {
			            isPresent = true;
					}
					break;
					
				case "css":
					if (wDriver.findElements(By.cssSelector(sLocator)).size() > 0) {
			            isPresent = true;
					}
					break;
					
				case "name":
					if (wDriver.findElements(By.name(sLocator)).size() > 0) {
			            isPresent = true;
					}
					break;
			}
			Thread.sleep(3000);
			iCount++;
		}
		return isPresent;		
	}
	
	
	FileInputStream fis=null;
	Workbook wb=null;
	Sheet sh=null;
	int rowNumber=0;
	String[] data;
	String excelValues=null;
	FileOutputStream fos=null;
	
	public int getTotalRows(String path,String sheetName){
		int rows=0;
		try{
			fis = new FileInputStream(path);
			wb=WorkbookFactory.create(fis);
			sh=wb.getSheet(sheetName);
			rows=sh.getLastRowNum();
			 rows=rows+1;
		}catch(Exception e){
			reportFailNote(e.getMessage());
		}
		reportNote("Rows Found in the excel - "+rows);
		return rows;		
	}
	
	
	public  String getExcelData(String path,String sheetName,int row,int col){
		try {
			fis = new FileInputStream(path);
			System.out.println(path);
			wb=WorkbookFactory.create(fis);
			sh=wb.getSheet(sheetName);
			excelValues=sh.getRow(row).getCell(col).getStringCellValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelValues;
	}
	
	public void setExcelRowData(String path,String sheetName,int rowNumber,int colNumber,Object actualResult) {
		try{	
			fis=new FileInputStream(path);
			wb=WorkbookFactory.create(fis);
			sh=wb.getSheet(sheetName);
			if(actualResult instanceof  String){
				String data=(String) actualResult;	
				sh.getRow(rowNumber).createCell(colNumber).setCellValue(data);
			}else if(actualResult instanceof  Long){
				long data=(long) actualResult;
				sh.getRow(rowNumber).createCell(colNumber).setCellValue(data);
			}else if(actualResult instanceof  Integer){
				int data=(int) actualResult;
				sh.getRow(rowNumber).createCell(colNumber).setCellValue(data);
			}else if(actualResult instanceof  Double){
				double data=(double) actualResult;
				sh.getRow(rowNumber).createCell(colNumber).setCellValue(data);
			}else if(actualResult instanceof  Character){
				char data=(char) actualResult;
				sh.getRow(rowNumber).createCell(colNumber).setCellValue(data);
			}
			
			fos=new FileOutputStream(path);
			wb.write(fos);
			fos.close();
			reportNote("data has been inserted to excel sheet");
		}catch(Exception e){
			reportNote(data+"_data is not inserted");
			e.printStackTrace();
			try {
				fos.close();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
	}
	
	public boolean isAlertPresent() {
	    try {
	        Alert a=driver.switchTo().alert();
	        a.accept();
	        return true;
	    } // try
	    catch (Exception e) {
	        return false;
	    } // catch
	}
	
	

}

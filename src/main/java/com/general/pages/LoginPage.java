package com.general.pages;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pdmp.base.TestBase;
import pdmp.utilities.TestUtil;

public class LoginPage extends TestBase {
	@FindBy(xpath = "//input[@name='username']")WebElement username;
	@FindBy(xpath = "//input[@name='password']")WebElement password;
	@FindBy(xpath = "//button[text()='Login']")WebElement login;

	public LoginPage() {
		PageFactory.initElements(driver, this);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
	}

	public void login(String un, String pwd) throws InterruptedException {
		username.sendKeys(un);
		password.sendKeys(pwd);
		Thread.sleep(15000);
		login.click();
		Thread.sleep(5000);
	}

}
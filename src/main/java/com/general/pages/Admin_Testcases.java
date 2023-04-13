package com.general.pages;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pdmp.base.TestBase;
import pdmp.pom.pages.admin.PatientSearch;
import pdmp.pom.pages.admin.SearchHistory;
import pdmp.pom.pages.generic.LoginPage;

public class Admin_TestCases extends TestBase {
	LoginPage lp;
	PatientSearch ps;
	SearchHistory sh;
	SoftAssert sa;

	public Admin_TestCases() {
		super();
	}

	@BeforeSuite
	public void setUp() throws InterruptedException {
		initialization();
		lp = new LoginPage();
		lp.login(prop.getProperty("username"), prop.getProperty("password"));
		ps = new PatientSearch();
		sh = new SearchHistory();
		sa = new SoftAssert();
	}

	@Test(priority = 1)
	public void TestCase1() throws InterruptedException {
		ps.valid_search();
		sa.assertAll();
	}

	@Test(priority = 2)
	public void TestCase2() throws InterruptedException {
		ps.invalid_search();
		sa.assertAll();
	}

	@Test(priority = 3)
	public void TestCase3() throws InterruptedException {

		sh.testPageHeader();
		sa.assertAll();
	}

	@Test(priority = 4)
	public void TestCase4() {

		sh.testRequestSource();
		sa.assertAll();
	}

	@Test(priority = 5)
	public void TestCase5() throws InterruptedException {

		sh.testPrescriberDEA();
		sa.assertAll();
	}

	@AfterSuite
	public void tearDown() {
		driver.quit();
	}
}
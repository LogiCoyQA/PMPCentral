package com.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import com.general.pages.BasePage;
import com.google.common.io.PatternFilenameFilter;

import jxl.Sheet;
import jxl.Workbook;
//TODO:EXPERIMENTAL NOT READY FOR PRODUCTION
public class ExcelFactory extends BasePage{
	public ExcelFactory(WebDriver wDriver) {
		super(wDriver);
	}

	// fetch column values from spreadsheet
	public ArrayList getColumnValuesOfSpreadsheet(String path,int colNumber){
		reportNote("retrieve value of column");
		ArrayList<String> list=new ArrayList<String>();
		File inputWorkbook = new File(path);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			reportNote("Total no of rows:"+sheet.getRows());
			reportNote("Total no of columns:"+sheet.getColumns());
			for (int i = 0; i < sheet.getRows(); i++) {
				list.add(sheet.getCell(colNumber,i).getContents());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getColumnValuesOfXLSX_Spreadsheet(String path, int columnIndex){
        ArrayList<String> columndata = null;
        try {
            File f = new File(path);
            FileInputStream ios = new FileInputStream(f);
            XSSFWorkbook workbook = new XSSFWorkbook(ios);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            columndata = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if(row.getRowNum() > 0){ //To filter column headings
                        if(cell.getColumnIndex() == columnIndex){// To match column index
                            switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                columndata.add(cell.getNumericCellValue()+"");
                                break;
                            case Cell.CELL_TYPE_STRING:
                                columndata.add(cell.getStringCellValue());
                                break;
                            }
                        }
                    }
                }
            }
            ios.close();
            /*System.out.println(columndata);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columndata;
    }

	public void waitForFileToDownload() throws InterruptedException{	
		File f1 = new File(downloadFileLocation); 
		reportNote("Check Location : "+downloadFileLocation);
		int i=0, waitTime = 20000;
		long iniFileSize, fileSize;
		boolean chkFile = false;


		while(!FileUtils.listFiles(f1, new String[]{"CRDOWNLOAD"}, false).isEmpty()){
			Thread.sleep(10000);
			if(i==18){
				reportFailNote("The File is not downloaded after 3 mins");
				break;
			}
			i++;	
		}

		//Get current file size.
		iniFileSize = folderSize(f1);
		i = 0;
		while(chkFile == false){
			Thread.sleep(waitTime);
			fileSize = folderSize(f1);
			if(i > 0){
				if(iniFileSize == fileSize && i < 25){
					chkFile = true;
				}
			}
			iniFileSize = folderSize(f1);
			i++;
			if(i == 25){
				reportFailNote("Download time exceeded 5 minutes.");
			}
		}
	}

	//Rename the downloaded file
	public void renameFile() throws Exception{
		File f1 = new File(downloadFileLocation);
		File[] matchingDownloadedFiles = f1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("Pipeline") && name.endsWith("xlsx");
			}
		});
		int j = 0;
		for(File downloadedFile:matchingDownloadedFiles) {
			String name = downloadedFile.getName();
			Thread.sleep(5000);
			String newName = "Loans.xls";
			downloadedFile.renameTo(new File(downloadFilepath));
			reportNote(name + " changed to " + newName);
			j++;	        
		}
	}

	//Delete downloaded file from directory - Downloads
	public void deleteFileInDirectory(){
		File xdownloadedFile=null;
		File f =null;
		File[] matchingFiles=null;		
		/*xdownloadedFile = new File(downloadFileLocation);
		if(xdownloadedFile.exists()){
			xdownloadedFile.delete();
			reportNote("File deleted successfully");
		}else{
			reportNote("File doesn't exist");
		}*/
		f = new File(downloadFileLocation);
		 matchingFiles = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("Pipeline") && name.endsWith("xlsx");
			}
		});
		int i = 0;
		reportNote("Found ["+matchingFiles.length+"] for Matching Criteria XLSX");
		for(File file:matchingFiles) {
			file.delete();
			String fileName=file.getName();
			i++;
		}
		 f = new File(downloadFileLocation);
		matchingFiles = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("Pipeline") && name.endsWith("xls");
			}
		});
		i = 0;
		reportNote("Found ["+matchingFiles.length+"] for Matching Criteria XLS");
		for(File file:matchingFiles) {
			file.delete();
			String fileName=file.getName();
			i++;
		}
		 f = new File(downloadFileLocation);
			matchingFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("Pipeline") && name.endsWith("crdownload");
				}
			});
			i = 0;
			reportNote("Found ["+matchingFiles.length+"] for Matching Criteria CRDOWNLOAD");
			for(File file:matchingFiles) {
				file.delete();
				String fileName=file.getName();
				i++;
			}
			reportNote("All Files deleted successfully from the location - "+downloadFileLocation);
	}	

	//Rename the downloaded file
	public void renameSpecificExcelFile(String oldFileName, String fileName) throws Exception{
		File f1 = new File(downloadFileLocation);
		File[] matchingDownloadedFiles = f1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(oldFileName) && name.endsWith("xlsx");
			}
		});
		int j = 0;
		for(File downloadedFile:matchingDownloadedFiles) {
			String name = downloadedFile.getName();
			String newName = fileName;
			downloadedFile.renameTo(new File(downloadFileLocation+ newName));
			reportNote(name + " changed to " + newName);
			j++;	        
		}
	}

	//Delete downloaded file from directory - Downloads
	public void deleteSpecificFileInDirectory(String fileName){
		File dlFile=null;

		dlFile = new File(downloadFileLocation+ fileName);
		if(dlFile.exists()){
			dlFile.delete();
			reportNote("File deleted successfully");
		}else{
			reportNote("File doesn't exist");
		}			
	}

	// fetch header column values from spreadsheet
	public ArrayList getHeaderValuesOfSpreadsheet(String path,int rowNumber){
		reportNote("retrieve value of column");
		ArrayList<String> list=new ArrayList<String>();
		File inputWorkbook = new File(path);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			reportNote("Total no of columns:"+sheet.getColumns());
			for (int i = 0; i < sheet.getColumns(); i++) {
				list.add(sheet.getCell(i,rowNumber).getContents());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	// fetch the header column values from the XLSX spreadheet
	public ArrayList<String> getHeaderValuesofXLSXSpreadhsheet ( String path, int rowNumber) throws Exception{
		reportNote("retrieve value of the first row");
		ArrayList<String> list=new ArrayList<String>();
		//reportNote("No of rows - "+list.size());
		FileInputStream fis = new FileInputStream(path);
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		reportNote("Total no of columns:"+mySheet.getRow(rowNumber).getPhysicalNumberOfCells());
		XSSFRow row = mySheet.getRow(0);
			for (int i = 0; i < mySheet.getRow(rowNumber).getPhysicalNumberOfCells(); i++) {
			XSSFCell cell = row.getCell(i);
			String Val=cell.getStringCellValue();
			list.add(Val);
		}
		return list;
		
		
		
	}
	public void renameRateSheetFile(String oldFileName, String fileType, String newFileName) {
		File f1 = new File(downloadFileLocation);
		File[] matchingDownloadedFiles = f1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(oldFileName) && name.endsWith(fileType);
			}
		});
		for(File downloadedFile:matchingDownloadedFiles) {
			String newName = downloadFileLocation+ newFileName+"."+fileType;
			switch(fileType){
			case "xls":
				boolean renameCheckXLS=downloadedFile.renameTo(new File(newName));
				if(renameCheckXLS==true){
					reportPassNote("Successfully renamed the downloaded Rate Sheet to "+newFileName+"."+fileType);
				}else{
					reportFailNote("FAILED to rename the downloaded Rate Sheet");
				}
				break;
			case "csv":
				boolean renameCheckCSV=downloadedFile.renameTo(new File(newName));
				if(renameCheckCSV==true){
					reportPassNote("Successfully renamed the downloaded Rate Sheet to "+newFileName+"."+fileType);

				}else{
					reportFailNote("FAILED to rename the downloaded Rate Sheet");
				}
				break;
			case "pdf":
				boolean renameCheckPDF=downloadedFile.renameTo(new File(newName));
				if(renameCheckPDF==true){
					reportPassNote("Successfully renamed the downloaded Rate Sheet to "+newFileName+"."+fileType);

				}else{
					reportFailNote("FAILED to rename the downloaded Rate Sheet");
				}
				break;
			}
		}
	}
	
	public long getFileSize(String fileName, String fileType){
		long length = 0;
		File f1 = new File(downloadFileLocation);
		File[] matchingDownloadedFiles = f1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(fileName) && name.endsWith(fileType);
			}
		});
		try{
			for(File downloadedFile:matchingDownloadedFiles) {
				if (downloadedFile.isFile()){
					length += downloadedFile.length();
					reportPassNote("The File has been downloaded successfully, it's size is of: " +length+" bytes");
				}else
					reportFailNote("The File is not downloaded correctly");
			}
		}
		catch(Exception e){
			reportFailNote(e.getMessage());
		}
		return length;
	}
	
}



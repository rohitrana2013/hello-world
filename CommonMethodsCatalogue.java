package Catalogue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Generic.Excel_reader;
import Generic.Xls_Reader;

public class CommonMethodsCatalogue

{
	public WebDriver openBrowser(Properties prop,String propertyFilePath,WebDriver driver) throws Exception
	{
		// open browser in chrome
				String exePathChrome = System.getProperty("user.dir")+"//ExeFile//chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", exePathChrome);
				driver= new ChromeDriver();
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				LoadPropertyFile(prop, propertyFilePath);
				
				return driver;
	}
	
	public WebDriver login(Properties prop,String propertyFilePath,WebDriver driver) throws Exception
	{
		
		// enter the appurl
		driver.navigate().to(prop.getProperty("appURL"));
		// get the title of page and save in variable
		String getTitle = driver.getTitle();
		// Compare the title

		Assert(getTitle, "Login");

		// Enter user name
		WebElement ele=driver.findElement(By.id(prop.getProperty("username_id")));
		Actions act=new Actions(driver);
		act.moveToElement(ele).click().sendKeys(prop.getProperty("username")).build().perform();
		// Enter password
		WebElement ele1=driver.findElement(By.id(prop.getProperty("password_id")));
		act.moveToElement(ele1).click().sendKeys(prop.getProperty("password")).build().perform();
		// click on submit button
		driver.findElement(By.xpath(prop.getProperty("SubmitHome"))).click();
		// get the title of page and save in variable
		String getTitlehome = driver.getTitle();
		// Compare the title
		Assert(getTitlehome, "Home");

		//To click on sub module at home page'
		driver.findElement(By.xpath(prop.getProperty("homePage_SubModule_Xpath"))).click();	

		WebDriverWait wait= new WebDriverWait(driver,180);

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(prop.getProperty("loaderXpath"))));

		return driver;
	}




	public void toTextfile(String data,String filepath){

		File file = new File(filepath);
		try{
			if (!file.exists()) {
				file.createNewFile();
				//				System.out.println("New file has been created" + file.createNewFile());
			}
			else
			{
				//				System.out.println("obsolute file path: "+ file.getAbsolutePath());
			}
			//To keep appending the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			if(file.length()==0){
				bw.flush();
			}else{
				bw.newLine();
				//bw.flush();
			}
			bw.write(data);
			bw.close();
		}catch(IOException e){System.out.println(e);;}
	}
	//Property Files
	public void LoadPropertyFile(Properties prop,String propertyFilePath){
		FileInputStream fis;
		try {
			fis = new FileInputStream(propertyFilePath);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.err.println("Property File not found");
			e.printStackTrace();
		}catch (IOException e) {
			System.err.println("Property File not found");
			e.printStackTrace();
		}
	}





	//Assert method
	public void Assert(String data1, String data2)
	{
		try{
			Assert.assertEquals(data1, data2);
		}
		catch(Throwable e)
		{
			System.out.println(e.getMessage());
		}
		return;
	}


	public  void takeScreenShot(WebDriver driver, String screenShotPath,String screenShotName) {
		try{
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenShotPath+screenShotName+".png"));
		}catch(Exception e){

		}
	}
	public  void takeScreenShot(WebDriver driver, String screenShotPath,int screenShotName) {
		try{
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenShotPath+screenShotName+".png"));
		}catch(Exception e){

		}
	}
	public void mergTxtFils(WebDriver driver,String outputPath,String outputPath1,String outputPath2,String outputPathmerge ) throws Exception
	{
		// PrintWriter object for file3.txt
		PrintWriter pw = new PrintWriter(outputPathmerge);

		// BufferedReader object for file1.txt
		BufferedReader br = new BufferedReader(new FileReader(outputPath));

		String line = br.readLine();

		// loop to copy each line of 
		// file1.txt to  file3.txt
		while (line != null)
		{
			pw.println(line);
			line = br.readLine();
		}

		br = new BufferedReader(new FileReader(outputPath1));

		line = br.readLine();

		// loop to copy each line of 
		// file2.txt to  file3.txt
		while(line != null)
		{
			pw.println(line);
			line = br.readLine();
		}
		br = new BufferedReader(new FileReader(outputPath2));

		line = br.readLine();

		// loop to copy each line of 
		// file2.txt to  file3.txt
		while(line != null)
		{
			pw.println(line);
			line = br.readLine();
		}

		pw.flush();

		// closing resources
		br.close();
		pw.close();

		System.out.println("Merged file1.txt and file2.txt into file3.txt");
	}


	public void toDeleteFile(String downloadPath) {
		boolean flag = false;
		File dir = new File(downloadPath);
		//		    File[] dir_contents = dir.listFiles();

		for (File file: dir.listFiles()) if (!file.isDirectory()) file.delete();
		//		    dir.is("IKEA- Ad Performance.xlsx").delete();
	}

	public boolean isFileDownloaded(String downloadPath, String fileName) {
		boolean flag = false;
		File dir = new File(downloadPath);
		File[] dir_contents = dir.listFiles();

		for (int i = 0; i < dir_contents.length; i++) {
			if (dir_contents[i].getName().contains(fileName.replaceAll("[0-9]","")))
				return flag=true;
		}

		return flag;
	}

	public String getDuration(String strStartTime, String strEndTime) {
		int intDur = 0;
		DateTime date1 = new DateTime(strStartTime);
		DateTime date2 = new DateTime(strEndTime);
		//System.out.println("date1" + date1);
		//System.out.println("date2" + date2);
		Period p = new Period(date1, date2);
		//System.out.println("Period" + p);
		intDur = (p.getHours() * 360) + (p.getMinutes() * 60) + p.getSeconds();
		//System.out.println("intDur" + intDur);
		return String.valueOf(intDur+" Sec");
	}
	public void mergeTxtFiles(String outputPath,String outputPath1,String outputPath2,String outputPathmerge ) throws Exception
	{
		// PrintWriter object for file3.txt
		PrintWriter pw = new PrintWriter(outputPathmerge);

		// BufferedReader object for file1.txt
		BufferedReader br = new BufferedReader(new FileReader(outputPath));

		String line = br.readLine();

		// loop to copy each line of 
		// file1.txt to  file3.txt
		while (line != null)
		{
			pw.println(line);
			line = br.readLine();
		}

		br = new BufferedReader(new FileReader(outputPath1));

		line = br.readLine();

		// loop to copy each line of 
		// file2.txt to  file3.txt
		while(line != null)
		{
			pw.println(line);
			line = br.readLine();
		}
		br = new BufferedReader(new FileReader(outputPath2));

		line = br.readLine();

		// loop to copy each line of 
		// file2.txt to  file3.txt
		while(line != null)
		{
			pw.println(line);
			line = br.readLine();
		}

		pw.flush();

		// closing resources
		br.close();
		pw.close();

		System.out.println("Merged file1.txt and file2.txt into file3.txt");
	}

	// convert any string in to camel case or proper case
	public String changeFirstlaterToUpperCase(String s) {
		String[] str = s.split(" ");
		String newStr = "";
		for (int i = 0; i < str.length; i++) {
			if (Character.isAlphabetic(str[i].charAt(0))) {
				if (!Character.isUpperCase(str[i].charAt(0))) {
					str[i] = str[i].substring(0, 1).toUpperCase()
							+ str[i].substring(1).toLowerCase();
					newStr = newStr + str[i] + " ";
				} else {
					str[i] = str[i];
					newStr = newStr + str[i] + " ";

				}
			} else {

				if (!Character.isUpperCase(str[i].charAt(1))) {
					str[i] = str[i].substring(1, 2).toUpperCase()
							+ str[i].substring(2).toLowerCase();
					newStr = newStr + str[i] + " ";
				} else {
					str[i] = str[i];
					newStr = newStr + str[i] + " ";

				}
			}

		}
		return newStr.trim();

	}

	public  String  chaFstLtrupper(String name)
	{

		//					String[] Fir =null;
		String finalres="";

		//	

		String[] Firtpart=name.split("\\ ");
		for(int i=0;i<Firtpart.length;i++)
		{
			//						if(!First.equals(""))
			//						{
			Firtpart[i]=Firtpart[i].substring(0, 1).toUpperCase()+Firtpart[i].replace(Firtpart[i].substring(0, 1),"").toLowerCase();
			finalres=finalres+Firtpart[i]+" ";
			//						System.out.print(Fir+" ");
			//						}
			//						else
			//							System.out.println("value is null");
		}
		System.out.println("\n");
		return finalres.trim();
	}
	public int getSpecialCharacterCount(String s) {
		if (s == null || s.trim().isEmpty()) {
			System.out.println("Incorrect format of string");
			return 0;
		}
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		// boolean b = m.matches();
		boolean b = m.find();
		int num;
		if (b == true)
		{
			System.out.println("There is a special character in my string ");
			num=1;
		}
		else
		{
			System.out.println("There is no special char.");
			num=0;
		}

		return num;
	}

}




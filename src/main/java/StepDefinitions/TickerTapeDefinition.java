package StepDefinitions;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.DriverClass;
import resources.LocatorsFile;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TickerTapeDefinition {

    WebDriver driver;
    List<WebElement> timelineOptions;
    static XSSFWorkbook workbook = new XSSFWorkbook();

    @Given("Verify Chrome Browser is Open")
    public String verifyChromeBrowserIsOpen() {
        driver = DriverClass.setUp();
        if (driver.getWindowHandles().size() > 0) {
            return "Chrome browser is open.";
        }
        return "Chrome browser is not open.";
    }


    @When("^Navigate to the website \"([^\"]*)\"$")
    public void navigateToTheWebsite(String url) {
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @When("^In the search box type company name \"([^\"]*)\" navigate to it$")
    public void inTheSearchBoxTypeCompanyName(String companiesName) {
        //finding search box
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement searchBox = driver.findElement(LocatorsFile.searchBox);
        searchBox.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        searchBox.sendKeys(companiesName);

        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(8));
        wait.until(ExpectedConditions.elementToBeClickable(LocatorsFile.companiesName));
        driver.findElement(LocatorsFile.companiesName).click();
    }

    @Then("^Select the period from timeline and fetch \"([^\"]*)\" info$")
    public void selectThePeriodFromTimelineAndFetchInfo(String companyName) {

        String sheetName = "LogBook";
        XSSFSheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        int lastRowNum = sheet.getLastRowNum();
        int rowNum = lastRowNum + 1, colNum = 0;
        Row row1, row2, row3, row4;
        //adding the timeStamp to the sheet
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        row1 = sheet.createRow(rowNum++);
        row2 = sheet.createRow(rowNum++);
        row1.createCell(colNum).setCellValue("TimeStamp");
        row2.createCell(colNum).setCellValue(dtf.format(now));
        colNum++;

        // fetch the companies name and current stock price
        row1.createCell(colNum).setCellValue("Companies Name");
        row2.createCell(colNum).setCellValue(companyName);
        colNum++;
        row1.createCell(colNum).setCellValue("Current Price");
        colNum++;

        // timeline info
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        timelineOptions = driver.findElements(LocatorsFile.timeline);
        row3 = sheet.createRow(rowNum++);
        row4 = sheet.createRow(rowNum++);
        for (int i = 0; i < 7; i++) {
            WebElement ele = timelineOptions.get(i);
            ele.click();
            System.out.println(ele.getText());
            row1.createCell(colNum).setCellValue(ele.getAttribute("value"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<WebElement> list = driver.findElements(LocatorsFile.lowHighPercentage);
            row2.createCell(colNum).setCellValue(list.get(0).getText());
            row3.createCell(colNum).setCellValue(list.get(1).getText());
            row4.createCell(colNum).setCellValue(list.get(2).getText());
            colNum++;
        }

        //get the ScoreBoard Options info
        List<WebElement> scoreBoardTag = driver.findElements(LocatorsFile.scoreBoardTag);
        List<WebElement> scoreBoardBadge = driver.findElements(LocatorsFile.scoreBoardBadge);
        for (int i = 0; i < 6 ; i++) {
            row1.createCell(colNum).setCellValue(scoreBoardTag.get(i).getText());
            row2.createCell(colNum).setCellValue(scoreBoardBadge.get(i).getText());
            colNum++;
        }

        //get the key metrics PB-Ratio
        row1.createCell(colNum).setCellValue("PB-Ratio");
        String value = driver.findElement(LocatorsFile.pbRatioValue).getText();
        row2.createCell(colNum).setCellValue(value);
        colNum++;

        //get the key Metrics Dividend Yield
        row1.createCell(colNum).setCellValue("Dividend Yeild");
        String yieldValue = driver.findElement(LocatorsFile.dividendYieldValue).getText();
        row2.createCell(colNum).setCellValue(yieldValue);

        String currentPrice = driver.findElement(LocatorsFile.currentPrice).getText();
        row2.createCell(2).setCellValue(currentPrice);


        try (FileOutputStream fileOut = new FileOutputStream("src/main/resources/data.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void convert_Excel_Data_To_JsonArray() throws IOException {
        FileOutputStream fos = new FileOutputStream("src/main/resources/data.json");
        try (FileInputStream fis = new FileInputStream("src/main/resources/data.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);


            JSONArray jsonArray = new JSONArray();
//            System.out.println(sheet.getLastRowNum());
            int lastRowNum = sheet.getLastRowNum();

            for(int i = 0; i < lastRowNum; i += 4){
                JSONObject jsonObject = new JSONObject();
                Row headRow = sheet.getRow(i);
                Row row = sheet.getRow(i + 1);
                Row row1 = sheet.getRow(i + 2);
                Row row2 = sheet.getRow(i + 3);
                JSONArray priceChart = new JSONArray();
                JSONArray scoreBoard = new JSONArray();
                for (int j = 3; j <= 9; j++) {
                    String priceValue = headRow.getCell(j).getStringCellValue() + ":" + row.getCell(j).getStringCellValue() + "," + row1.getCell(j).getStringCellValue() + "," + row2.getCell(j).getStringCellValue();
                    priceChart.put(priceValue.trim());
                }
                jsonObject.put("PriceChart", priceChart);

                for (int j = 10; j <= 15; j++) {
                    String scoreValues = headRow.getCell(j).getStringCellValue() + ":" + row.getCell(j).getStringCellValue();
                    scoreBoard.put(scoreValues.trim());
                }
                jsonObject.put("ScoreBoard", scoreBoard);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    if (j <= 2 || j > 15) {
                            jsonObject.put(headRow.getCell(j).getStringCellValue(), row.getCell(j).getStringCellValue());
                    }
                }
                jsonArray.put(jsonObject);
            }
//            System.out.println(jsonArray.toString());
            fos.write(jsonArray.toString().getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void before_or_after_all() throws IOException {
        convert_Excel_Data_To_JsonArray();

    }

}

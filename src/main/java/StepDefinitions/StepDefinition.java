package StepDefinitions;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StepDefinition {

    WebDriver driver;
    List<WebElement>  timelineOptions;
    static XSSFWorkbook workbook = new XSSFWorkbook();

    @Before
    public void setDriver(){

        driver = driverClass.setUp();

    }
    @Given("I am on Chrome browser")
    public void iAmOnChromeBrowser() {

    }

    @When("Navigate to the website {string}")
    public void navigateToTheWebsite(String url) {
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @When("In the search box type company name {string} navigate to it")
    public void inTheSearchBoxTypeCompanyName(String companiesName) {
        //finding search box
        WebElement searchBox = driver.findElement(By.xpath("//div//input[@type = 'search']"));
        searchBox.click();
        searchBox.sendKeys(companiesName);

        driver.findElement(By.xpath("//span[@class='jsx-2243824175 jsx-152776563 suggest-desc'][normalize-space()='"+companiesName+"']")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Then("Select the period from timeline and fetch {string} {int} info")
    public void selectThePeriodFromTimelineAndFetchInfo(String companyName, int num) {

        String sheetName = "LogBook";
        XSSFSheet sheet = workbook.getSheet(sheetName);

        if(sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        int rowNum = num, colNum = 0;
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
        //String companiesName = driver.findElement(By.xpath("//aside//div//span[starts-with(@class, 'jsx-4049911629 ticker')]")).getText();
        row2.createCell(colNum).setCellValue(companyName);
        colNum++;
        row1.createCell(colNum).setCellValue("Current Price");
        // //aside//div//span[starts-with(@class, 'jsx-3168773259 current-price')]
        String currentPrice = driver.findElement(By.xpath("(//span[@class=\"jsx-3168773259 current-price typography-h1 text-primary\"])[1]")).getText();
        row2.createCell(colNum).setCellValue(currentPrice);
        colNum++;

        // timeline info
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        timelineOptions = driver.findElements(By.xpath("(//*[@name=\"chart-scope-radio\"])"));
        row3 = sheet.createRow(rowNum++);
        row4 = sheet.createRow(rowNum++);
        for(int i=0;i<7;i++){
            WebElement ele = timelineOptions.get(i);
            ele.click();
            System.out.println(ele.getText());
            row1.createCell(colNum).setCellValue(ele.getAttribute("value"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<WebElement> list = driver.findElements(By.xpath("//*[@class='jsx-3420801268']"));
            row2.createCell(colNum).setCellValue(list.get(0).getText());
            row3.createCell(colNum).setCellValue(list.get(1).getText());
            row4.createCell(colNum).setCellValue(list.get(2).getText());
            colNum++;
        }

        //get the ScoreBoard Options info
        List<WebElement> scoreBoardTag = driver.findElements(By.xpath("//div[@class='jsx-1448665461 cards-container']//div[contains(@class, 'flex-start')]//p[contains(@class, 'typography-body-medium-m text-primary mr8')]"));
        List<WebElement> scoreBoardBadge = driver.findElements(By.xpath("//div[@class='jsx-1448665461 cards-container']//div[contains(@class, 'flex-start')]//span[contains(@class, 'typography-special-smallcaps badge')]"));
        for(int i = 0; i < 6; i++){
            row1.createCell(colNum).setCellValue(scoreBoardTag.get(i).getText());
            row2.createCell(colNum).setCellValue(scoreBoardBadge.get(i).getText());
            colNum++;
        }

        //get the key metrics PB-Ratio
        String pbRatio = driver.findElement(By.xpath("//span[contains(@class,'jsx-3104643842 ellipsis desktop--only')][normalize-space()='PB Ratio']")).getText();
        row1.createCell(colNum).setCellValue(pbRatio);
        String value = driver.findElement(By.xpath("(//tbody[@class='jsx-3086567756']//td[2])[1]")).getText();
        row2.createCell(colNum).setCellValue(value);
        colNum++;

        //get the key Metrics Dividend Yield
        row1.createCell(colNum).setCellValue("Dividend Yeild");
        String yieldValue = driver.findElement(By.xpath("(//tbody[@class='jsx-3086567756']//td[3])[1]")).getText();
        row2.createCell(colNum).setCellValue(yieldValue);
    }
    @Test
    @And("convert excel data to json array")
    public void convert_Excel_Data_To_JsonArray() {
        String filePath = "C:\\Users\\ayush.saxena\\IdeaProjects\\TickerTapeAutomation\\data.xlsx";
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            JSONArray jsonArray = new JSONArray();

            int lastRowNum = sheet.getLastRowNum();
            Row row0 = sheet.getRow(0);
            Row row1 = sheet.getRow(1);
            Row row2= sheet.getRow(2);
            Row row3 = sheet.getRow(3);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("TimeStamp", row1.getCell(0).getStringCellValue());
            jsonObject.put("Company Name", row1.getCell(1).getStringCellValue());
            jsonObject.put("Current Price", row1.getCell(2).getStringCellValue());

            JSONArray scoreBoard = new JSONArray();
            for(int i = 10; i <= 15; i++){
                String scoreValues = row0.getCell(i).getStringCellValue() + ":" + row1.getCell(i).getStringCellValue();
                scoreBoard.put(scoreValues.trim());
            }
            jsonObject.put("ScoreBoard", scoreBoard);

            JSONArray priceChart = new JSONArray();
            for(int i = 3; i <= 9; i++){
                String priceValue = row0.getCell(i).getStringCellValue() + ":" +row1.getCell(i).getStringCellValue() + "," + row2.getCell(i).getStringCellValue() + "," + row3.getCell(i).getStringCellValue();
                priceChart.put(priceValue.trim());
            }
            jsonObject.put("PriceChart", priceChart);

            jsonObject.put("PB-Ratio", row1.getCell(16).getStringCellValue());
            jsonObject.put("Dividend Yield", row1.getCell(17).getStringCellValue());

            jsonArray.put(jsonObject);
            System.out.println(jsonArray.toString());

            // 2nd Company
            JSONArray jsonArray1 = new JSONArray();
            Row row5 = sheet.getRow(5);
            Row row6 = sheet.getRow(6);
            Row row7= sheet.getRow(7);
            Row row8 = sheet.getRow(8);

            JSONObject jsonObject1 = new JSONObject();

            jsonObject1.put("TimeStamp", row6.getCell(0).getStringCellValue());
            jsonObject1.put("Company Name", row6.getCell(1).getStringCellValue());
            jsonObject1.put("Current Price", row6.getCell(2).getStringCellValue());

            JSONArray scoreBoard1 = new JSONArray();
            for(int i = 10; i <= 15; i++){
                String scoreValues = row5.getCell(i).getStringCellValue() + ":" + row6.getCell(i).getStringCellValue();
                scoreBoard1.put(scoreValues.trim());
            }
            jsonObject1.put("ScoreBoard", scoreBoard1);

            JSONArray priceChart1 = new JSONArray();
            for(int i = 3; i <= 9; i++){
                String priceValue = row5.getCell(i).getStringCellValue() + ":" +row6.getCell(i).getStringCellValue() + "," + row7.getCell(i).getStringCellValue() + "," + row8.getCell(i).getStringCellValue();
                priceChart1.put(priceValue.trim());
            }
            jsonObject1.put("PriceChart", priceChart1);

            jsonObject1.put("PB-Ratio", row6.getCell(16).getStringCellValue());
            jsonObject1.put("Dividend Yield", row6.getCell(17).getStringCellValue());

            jsonArray1.put(jsonObject1);
            System.out.println(jsonArray1.toString());

            try (FileOutputStream fos = new FileOutputStream("C:\\Users\\ayush.saxena\\IdeaProjects\\TickerTapeAutomation\\data.json")) {
                JSONArray combinedArray = new JSONArray();
                combinedArray.putAll(jsonArray);
                combinedArray.putAll(jsonArray1);

                fos.write(combinedArray.toString().getBytes());

            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        @AfterAll
    public static void before_or_after_all()
    {
        try (FileOutputStream fileOut = new FileOutputStream("data.xlsx"))
        { workbook.write(fileOut); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
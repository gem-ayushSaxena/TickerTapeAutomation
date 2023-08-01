package resources;


import org.openqa.selenium.By;

public class LocatorsFile {
     public static By searchBox = By.xpath("//input[@type = 'search']");
     public static By companiesName = By.xpath("//span[contains(@class,'suggest-desc')]");
     public static By currentPrice = By.xpath("//span[@class='jsx-3168773259 current-price typography-h1 text-primary']");
     public static By timeline = By.xpath("(//*[@name='chart-scope-radio'])");
     public static By lowHighPercentage = By.xpath("//*[@class='jsx-3420801268']");
     public static By scoreBoardTag = By.xpath("//p[contains(@class, 'typography-body-medium-m text-primary mr8')]");
     public static By scoreBoardBadge = By.xpath("//span[contains(@class, 'typography-special-smallcaps badge')]");
     public static By pbRatioValue = By.xpath("//tbody[@class='jsx-3086567756']//td[2]");
     public static By dividendYieldValue = By.xpath("//tbody[@class='jsx-3086567756']//td[3]");

}

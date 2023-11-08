package org.heroku.stepdefinition;

import com.google.gson.Gson;
import io.cucumber.java.en.*;
import org.heroku.runner.TestRunner;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepDefinition {
    public static WebDriver driver = TestRunner.driver;

    public static String jsonFilePath = "src//main//resources//data.json";
    public static String inputJsonData = readJsonFromFile(jsonFilePath);
    @Given("launch the Heroku application")
    public void launch_the_heroku_application() {
        driver.manage().window().maximize();
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
    }
    @When("click the table data button")
    public void click_the_table_data_button() {
        WebElement button = driver.findElement(By.xpath("//summary[text()='Table Data']"));
        WebElement markerParent = button.findElement(By.xpath(".."));
        WebElement marker = markerParent.findElement(By.cssSelector("::marker"));
        marker.click();
    }
    @When("enter the data in the data field")
    public void enter_the_data_in_the_data_field() {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//textarea[@id='jsondata']"))));
//        WebElement textBox = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
        inputJsonDataIntoTextBox(driver,inputJsonData);
    }
    @When("click refresh table")
    public void click_refresh_table() {
        WebElement refreshBtn = driver.findElement(By.xpath("//button[text()='Refresh Table']"));
        refreshBtn.click();
    }
    @Then("verify the table")
    public void verify_the_table() {

//        List<Map<String, Object>> tableData = getTableData();
//        String jsonData = convertToJSON(tableData);
        assertTableData(expectedJsonData(), getTableData());
    }

    private static String getTableData() {
        List<Map<String, Object>> tableDataList = new ArrayList<>();
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dynamictable']//tr[1]//following-sibling::tr"));

        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            Map<String, Object> rowData = new HashMap<>();

            rowData.put("Column1", columns.get(0).getText());
            rowData.put("Column2", columns.get(1).getText());
            // Add more columns as needed

            tableDataList.add(rowData);
        }

        return tableDataList.toString();
    }

    private static String convertToJSON(List<Map<String, Object>> tableData) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (Map<String, Object> rowData : tableData) {
            jsonBuilder.append("{");
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                jsonBuilder.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
            }
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // Remove the trailing comma
            jsonBuilder.append("},");
        }
        if (!tableData.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // Remove the trailing comma
        }
        jsonBuilder.append("]");

        return jsonBuilder.toString();
    }

    private static List<Map<String, Object>> parseJsonData(String jsonData) {
        return new Gson().fromJson(jsonData, new ArrayList<Map<String, Object>>().getClass());
    }
    private static void assertTableData(String expected, String actual) {
        Assert.assertEquals("Table data does not match!", expected, actual);
    }

    private static String readJsonFromFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String expectedJsonData(){
        String expectedJsonData = readJsonFromFile(jsonFilePath);
        return expectedJsonData;
    }

    private static void inputJsonDataIntoTextBox(WebDriver driver, String jsonData) {
        WebElement textBox = driver.findElement(By.id("//textarea[@id='jsondata']"));
        textBox.sendKeys(jsonData);

        WebElement refreshButton = driver.findElement(By.id("table1-refresh"));
        refreshButton.click();
    }
}

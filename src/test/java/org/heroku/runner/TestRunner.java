package org.heroku.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src//test//java//org//heroku//feature//HeroKu.feature",
                glue = "stepdefinition",
        dryRun = false,
        publish = true,
        plugin = {"html:Report/rep.html","pretty"}
)
public class TestRunner {

    public static WebDriver driver;
    @Before
    public static void start(){
        ChromeOptions op = new ChromeOptions();
        op.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(op);

    }

    @After
    public static void end(){
//        driver.quit();
    }


}

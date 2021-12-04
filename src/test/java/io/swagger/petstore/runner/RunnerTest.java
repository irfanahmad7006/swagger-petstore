package io.swagger.petstore.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = {"src\\test\\resources\\featureFiles"},
        glue = {"io.swagger.petstore.stepsDefinitions"}, //specify hooks location if not present in the same package as step defs
 //       plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        monochrome = true,
        publish = true
)
public class RunnerTest extends AbstractTestNGCucumberTests {
}

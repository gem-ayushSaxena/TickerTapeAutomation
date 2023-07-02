package RunnerFiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/main/resources/Features/Feature.feature",
        glue="StepDefinitions",
        plugin = {"pretty","json:target/Jsonreports/reports.json","html:target/Htmlreports/reports.html","junit:target/JunitReports/reports/reports.xml"},
        monochrome = true)
public class Runner {
}

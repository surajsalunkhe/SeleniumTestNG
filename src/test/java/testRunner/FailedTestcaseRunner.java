package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = { "@target/failedrerun.txt" },
        glue = {"stepDef","appHooks"}, // path of step definition
        plugin = {"pretty",
                "rerun:target/failedrerun.txt"},
        monochrome =true
)
public class FailedTestcaseRunner extends AbstractTestNGCucumberTests {
}

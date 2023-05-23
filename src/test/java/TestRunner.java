import cucumber.api.CucumberOptions;
import core.CucumberRunnerBase;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "stepDefs",
        tags = {"not @Ignore"},
        plugin = {
                "pretty",
                "junit:target/cucumber-reports/junit-report.xml",
                "html:target/cucumber-reports/cucumber-pretty",
                "json:target/cucumber-reports/CucumberTestReport.json",
                "rerun:target/cucumber-reports/rerun.txt"
        })

class TestRunner extends CucumberRunnerBase {

}

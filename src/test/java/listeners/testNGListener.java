package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class testNGListener implements ITestListener {
    public void onTestStart(ITestResult result) {
        LogTestNG.info("");
        LogTestNG.info(result.getMethod().getMethodName()+"Started");
        LogTestNG.info(result.getMethod().getDescription());
    }

    public void onTestSuccess(ITestResult result) {
        LogTestNG.info(result.getMethod().getMethodName()+"Passed");
    }

    public  void onTestFailure(ITestResult result) {
        LogTestNG.info("Failed because=\t"+result.getThrowable());
    }

    public  void onTestSkipped(ITestResult result) {
        LogTestNG.info("Skipped because=\t"+result.getThrowable());
    }

    public  void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public  void onTestFailedWithTimeout(ITestResult result) {
        this.onTestFailure(result);
    }

    public  void onStart(ITestContext context) {
        LogTestNG.info("<==============On Start:-"+context.getName()+"==============>");
    }

    public  void onFinish(ITestContext context) {
        LogTestNG.info("<==============On Finish:-"+context.getName()+"==============>");
    }

}

package org.liteUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestResult{
    protected List<TestFailure> failures;
    protected List<TestFailure> errors;
    protected int testCount;
    private boolean stop;

    public TestResult() {
        failures = new ArrayList<>();
        errors = new ArrayList<>();
        testCount = 0;
        stop = false;
    }

    public void addError(Test test, Throwable throwable) {

        errors.add(new TestFailure(test,throwable));
    }

    public void addFailure(Test test, Throwable throwable) {
        failures.add(new TestFailure(test, throwable));
    }

    public void startTest(Test test) {
        testCount += test.countTestCases();
    }

    public void endTest(Test test) {

    }

    protected void run(final TestCase testCase) {
        startTest(testCase);
        try {
            testCase.doRun();
        } catch (AssertionFailedError error) {
            addFailure(testCase, error);
        } catch (Throwable e) {
            addError(testCase, e);
        }
        endTest(testCase);

    }
    public  int runCount() {
        return testCount;
    }


    public  boolean shouldStop() {
        return stop;
    }

    public  void stop() {
        stop= true;
    }

    public  int errorCount() {
        return errors.size();
    }

    public  Iterator errors() {
        return errors.iterator();
    }

    public  int failureCount() {
        return failures.size();
    }

    public Iterator<TestFailure> failures() {
        return failures.iterator();
    }
    public  boolean wasSuccessful() {
        return this.failureCount() == 0 && this.errorCount() == 0;
    }
}

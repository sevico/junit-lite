package org.liteUnit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class TestCase extends Assert implements Test {
    private String name;

    public TestCase(String name) {
        this.name = name;
    }

    @Override
    public int countTestCases() {
        return 1;
    }

    public void doRun(TestResult testResult) throws Throwable {
        setUp();
        try{
            runTest();
        }finally {
            tearDown();
        }
    }

    protected void setUp() {

    }
    protected void runTest() throws Throwable {
        Method runMethod= null;
        try {
            runMethod= getClass().getMethod(name, null);
        } catch (NoSuchMethodException e) {
            fail("Method \""+name+"\" not found");
        }
        if (!Modifier.isPublic(runMethod.getModifiers())) {
            fail("Method \""+name+"\" should be public");
        }

        try {
            runMethod.invoke(this, new Class[0]);
        }
        catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        }
        catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    protected void tearDown() {

    }

    @Override
    public void run(TestResult testResult) {
        testResult.run(this);
    }
    public void doRun() throws Throwable{
        setUp();
        try {
            runTest();
        }
        finally {
            tearDown();

        }
    }
}

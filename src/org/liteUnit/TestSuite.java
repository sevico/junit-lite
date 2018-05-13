package org.liteUnit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class TestSuite implements Test {
    private List<Test> tests = new ArrayList<>(10);
    private String name;

    public TestSuite() {

    }

    public TestSuite(final Class<?> theclass) throws NoSuchMethodException {
        this.name = theclass.getName();
        Constructor<?> constructor = null;
        constructor = getConstructor(theclass);
        if (!Modifier.isPublic(theclass.getModifiers())) {
            addTest(warning("Class "+theclass.getName()+" is not public"));
            return;
        }
        Vector<String> names = new Vector<>();
        Method[] methods = theclass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            addTestMethod(methods[i], names, constructor);
        }
        if (tests.size() == 0) {
            addTest(warning("No tests found in "+theclass.getName()));
        }
    }

    private void addTestMethod(Method m, Vector<String> names, Constructor<?> constructor) {
        String name = m.getName();
        if (names.contains(name)) {
            return;
        }
        if (isPublicTestMethod(m)) {
            names.addElement(name);
            Object[] args = new Object[]{name};
            try {
                addTest((Test) constructor.newInstance(args));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isPublicTestMethod(Method method) {
        return isTestMethod(method) && Modifier.isPublic(method.getModifiers());
    }

    private boolean isTestMethod(Method method) {
        String name = method.getName();
        Class<?>[] parameters = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        return parameters.length == 0 && name.startsWith("test") && returnType.equals(Void.TYPE);
    }

    private Constructor<?> getConstructor(Class<?> theclass) throws NoSuchMethodException {
        Class<?>[] args = {String.class};
        return theclass.getConstructor(args);
    }



    public void addTest(Test test) {
        tests.add(test);
    }

    @Override
    public void run(TestResult testResult) {
        for (Iterator<Test> e = tests(); e.hasNext(); ) {
            if (testResult.shouldStop()) {
                break;
            }
            Test test = e.next();
            test.run(testResult);

        }
    }
    private String exceptionToString(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        return stringWriter.toString();

    }
    private Test warning(final String message) {
        return new TestCase("warning") {
            public void doRun() {
                fail(message);

            }
        };
    }

    public int countTestCases() {
        int count= 0;

        for (Iterator<Test> e= tests(); e.hasNext(); ) {
            Test test= e.next();
            count= count + test.countTestCases();
        }
        return count;
    }
    public Iterator<Test> tests() {
        return tests.iterator();
    }
}

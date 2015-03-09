package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.*;

/**
 * Created by Morten on 25.02.15.
 */
public class TestRunner {
    public static void main(String[] args) {
        List testCases = new ArrayList();

        //Add test cases
        testCases.add(PersonTest.class);
        testCases.add(PersonTest.class);

        for (Object testCase : testCases)
        {
            runTestCase((Class) testCase);
        }
    }

    private static void runTestCase(Class testCase) {
        Result result = JUnitCore.runClasses(testCase);
        for (Failure failure : result.getFailures())
        {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}

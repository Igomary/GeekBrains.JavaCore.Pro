package lesson7.hw;

import lesson7.hw.annotation.AfterSuite;
import lesson7.hw.annotation.BeforeSuite;
import lesson7.hw.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class TestRunner {
    public static void start(Class<?> tClass) {
        Method[] methods = tClass.getMethods();
        int ifBefore = 0;
        int ifAfter = 0;
        Method before = null;
        Method after = null;
        for (Method method:methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                before = method;
                ifBefore++;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                after = method;
                ifAfter++;
            }
        }
        if (ifAfter > 1 || ifBefore > 1) {
            throw new RuntimeException("Before/After duplication error");
        }
        runTest(methods, before, after);
    }

    private static void runTest(Method[] methods, Method before, Method after) {
        TestClass testClass = new TestClass();
        try {
            if (before != null) {
                //System.out.println(before.getName());
                before.invoke(testClass);
            }
            Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Test.class))
                    .sorted(Comparator.comparingInt(method->method.getAnnotation(Test.class).priority()))
                    .forEach(method -> {
                        //System.out.println(method.getName());
                       try {
                            method.invoke(testClass);
                        } catch (IllegalAccessException |InvocationTargetException e) {
                            e.printStackTrace();
                       }
                    });
           if (after != null) {
                after.invoke(testClass);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }



    }
}

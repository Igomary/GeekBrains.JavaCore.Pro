package lesson7.hw;

import lesson7.hw.annotation.AfterSuite;
import lesson7.hw.annotation.BeforeSuite;
import lesson7.hw.annotation.Test;

public class TestClass {

    @BeforeSuite
    public void beforeTest1() {
        System.out.println("First before");
    }

   /* @BeforeSuite
    public void beforeTEst2() {
        System.out.println("Second before");
    }*/

    @Test
    public void test1() {
        System.out.println("Приоритет по умолчанию (5)");
    }

    @Test(priority = 8)
    public void test2() {
        System.out.println("Приоритет 8");
    }


    @Test(priority = 2)
    public void test3() {
        System.out.println("Приоритет 2");
    }

    @Test (priority = 10)
    public void test4() {
        System.out.println("Приоритет 10");
    }

    @AfterSuite
    public void afterTest() {
        System.out.println("The last one");
    }
}

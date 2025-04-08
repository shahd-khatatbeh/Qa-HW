package main.najah.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CalculatorTest.class,
    ProductTest.class,
    UserServiceSimpleTest.class,
    RecipeBookTest.class,
    RecipeTest.class
})
public class AllTests {
    // This class is empty and serves only as a marker for the JUnit 5 test suite
}

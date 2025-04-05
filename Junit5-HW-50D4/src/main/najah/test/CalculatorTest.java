package main.najah.test;


import main.najah.code.Calculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Test Suite")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CalculatorTest {

    private static Calculator calculator;

    @BeforeAll
    static void setupAll() {
        System.out.println("Setting up Calculator tests");
        calculator = new Calculator();
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Calculator tests completed");
        calculator = null;
    }

    @BeforeEach
    void setup() {
        System.out.println("Setting up individual test");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Individual test completed");
    }

    @Test
    @Order(1)
    @DisplayName("Adding two numbers should work")
    void testAddTwoNumbers() {
        assertEquals(5, calculator.add(2, 3), "2 + 3 should equal 5");
    }

    @Test
    @Order(2)
    @DisplayName("Adding multiple numbers should work")
    void testAddMultipleNumbers() {
        assertEquals(15, calculator.add(1, 2, 3, 4, 5), "1 + 2 + 3 + 4 + 5 should equal 15");
    }

    @Test
    @Order(3)
    @DisplayName("Adding zero numbers should return zero")
    void testAddNoNumbers() {
        assertEquals(0, calculator.add(), "Adding no numbers should return 0");
    }

    @Test
    @Order(4)
    @DisplayName("Division should work correctly")
    void testDivide() {
        assertEquals(2, calculator.divide(10, 5), "10 / 5 should equal 2");
        assertEquals(0, calculator.divide(1, 2), "1 / 2 should equal 0 due to integer division");
    }

    @Test
    @Order(5)
    @DisplayName("Division by zero should throw exception")
    void testDivisionByZero() {
        Exception exception = assertThrows(ArithmeticException.class, 
                () -> calculator.divide(10, 0),
                "Division by zero should throw ArithmeticException");
        
        String expectedMessage = "Cannot divide by zero";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), 
                "Exception message should contain 'Cannot divide by zero'");
    }

    @ParameterizedTest
    @Order(6)
    @DisplayName("Factorial of various inputs")
    @CsvSource({
        "0, 1", 
        "1, 1", 
        "2, 2", 
        "3, 6", 
        "4, 24", 
        "5, 120"
    })
    void testFactorialWithValidInputs(int input, int expected) {
        assertEquals(expected, calculator.factorial(input), 
                "Factorial of " + input + " should be " + expected);
    }

    @Test
    @Order(7)
    @DisplayName("Factorial of negative number should throw exception")
    void testFactorialWithNegativeInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> calculator.factorial(-1),
                "Factorial of negative number should throw IllegalArgumentException");
        
        assertEquals("Negative input", exception.getMessage(), 
                "Exception message should be 'Negative input'");
    }

    @Test
    @Order(8)
    @DisplayName("Calculator operations should complete in reasonable time")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void testCalculatorPerformance() {
        // Testing a more expensive operation that should still complete quickly
        calculator.factorial(10);
    }

    @Disabled("This test intentionally fails to demonstrate @Disabled. To fix: adjust expected value to 720")
    @Test
    @Order(9)
    @DisplayName("Intentionally failing factorial test")
    void testFactorialFailingIntentionally() {
        assertEquals(700, calculator.factorial(6), 
                "Factorial of 6 should be 720, not 700");
    }
}
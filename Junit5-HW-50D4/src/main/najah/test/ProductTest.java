package main.najah.test;

import main.najah.code.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Test Suite")
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Test Product", 100.0);
    }

    @Test
    @DisplayName("Product should be created with valid parameters")
    void testProductCreation() {
        Product laptop = new Product("Laptop", 999.99);
        
        assertEquals("Laptop", laptop.getName(), "Product name should be set correctly");
        assertEquals(999.99, laptop.getPrice(), 0.001, "Product price should be set correctly");
        assertEquals(0.0, laptop.getDiscount(), 0.001, "Initial discount should be zero");
    }

    @Test
    @DisplayName("Product creation should fail with negative price")
    void testProductCreationWithNegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> new Product("Invalid", -10.0),
                "Creating product with negative price should throw IllegalArgumentException");
        
        assertTrue(exception.getMessage().contains("Price must be non-negative"), 
                "Exception message should mention that price must be non-negative");
    }

    @ParameterizedTest
    @DisplayName("Valid discounts should be applied correctly")
    @CsvSource({
        "0, 100.0",
        "10, 90.0",
        "25, 75.0",
        "50, 50.0"
    })
    void testApplyValidDiscount(double discount, double expectedPrice) {
        product.applyDiscount(discount);
        assertEquals(expectedPrice, product.getFinalPrice(), 0.001, 
                "Final price with " + discount + "% discount should be " + expectedPrice);
    }

    @ParameterizedTest
    @DisplayName("Invalid discounts should throw exceptions")
    @ValueSource(doubles = {-5.0, 51.0, 100.0})
    void testApplyInvalidDiscount(double invalidDiscount) {
        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> product.applyDiscount(invalidDiscount),
                "Applying " + invalidDiscount + "% discount should throw IllegalArgumentException");
        
        assertEquals("Invalid discount", exception.getMessage(), 
                "Exception message should be 'Invalid discount'");
    }

    @Test
    @DisplayName("Product price calculations should complete within timeout")
    @Timeout(value = 50, unit = TimeUnit.MILLISECONDS)
    void testProductPriceCalculationPerformance() {
        // Apply discount and calculate final price multiple times
        for (int i = 0; i < 1000; i++) {
            product.applyDiscount(i % 50); // Keep within valid range 0-50
            product.getFinalPrice();
        }
    }
}

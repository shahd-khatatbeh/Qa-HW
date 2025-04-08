package main.najah.test;

import main.najah.code.Recipe;
import main.najah.code.RecipeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Recipe Tests")
class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
    }

    @Test
    @DisplayName("New recipe should have default values")
    void testNewRecipeHasDefaultValues() {
        assertEquals("", recipe.getName(), "Default name should be empty string");
        assertEquals(0, recipe.getPrice(), "Default price should be 0");
        assertEquals(0, recipe.getAmtCoffee(), "Default coffee amount should be 0");
        assertEquals(0, recipe.getAmtMilk(), "Default milk amount should be 0");
        assertEquals(0, recipe.getAmtSugar(), "Default sugar amount should be 0");
        assertEquals(0, recipe.getAmtChocolate(), "Default chocolate amount should be 0");
    }

    @Test
    @DisplayName("Recipe name should be set correctly")
    void testSetName() {
        recipe.setName("Espresso");
        assertEquals("Espresso", recipe.getName(), "Name should be set to Espresso");
        
        // Test with null (should not change name)
        recipe.setName(null);
        assertEquals("Espresso", recipe.getName(), "Name should not change when null is provided");
    }

    @ParameterizedTest
    @DisplayName("Valid price strings should be parsed correctly")
    @CsvSource({
        "0, 0",
        "5, 5",
        "10, 10",
        "999, 999"
    })
    void testSetValidPrice(String priceStr, int expectedPrice) throws RecipeException {
        recipe.setPrice(priceStr);
        assertEquals(expectedPrice, recipe.getPrice(), "Price should be set correctly");
    }

    @ParameterizedTest
    @DisplayName("Invalid price strings should throw RecipeException")
    @ValueSource(strings = {"-1", "abc", "1.5", ""})
    void testSetInvalidPrice(String invalidPrice) {
        Exception exception = assertThrows(RecipeException.class, 
                () -> recipe.setPrice(invalidPrice),
                "Setting invalid price should throw RecipeException");
        
        assertTrue(exception.getMessage().contains("Price must be a positive integer"), 
                "Exception message should mention that price must be positive integer");
    }

    @ParameterizedTest
    @DisplayName("Valid ingredient amounts should be parsed correctly")
    @CsvSource({
        "coffee, 0, 0",
        "coffee, 5, 5",
        "milk, 3, 3",
        "sugar, 2, 2",
        "chocolate, 1, 1"
    })
    void testSetValidIngredientAmounts(String ingredient, String amountStr, int expectedAmount) 
            throws RecipeException {
        
        switch (ingredient) {
            case "coffee":
                recipe.setAmtCoffee(amountStr);
                assertEquals(expectedAmount, recipe.getAmtCoffee(), 
                        "Coffee amount should be set correctly");
                break;
            case "milk":
                recipe.setAmtMilk(amountStr);
                assertEquals(expectedAmount, recipe.getAmtMilk(), 
                        "Milk amount should be set correctly");
                break;
            case "sugar":
                recipe.setAmtSugar(amountStr);
                assertEquals(expectedAmount, recipe.getAmtSugar(), 
                        "Sugar amount should be set correctly");
                break;
            case "chocolate":
                recipe.setAmtChocolate(amountStr);
                assertEquals(expectedAmount, recipe.getAmtChocolate(), 
                        "Chocolate amount should be set correctly");
                break;
        }
    }

    @ParameterizedTest
    @DisplayName("Invalid ingredient amounts should throw RecipeException")
    @ValueSource(strings = {"-1", "abc", "1.5", ""})
    void testSetInvalidIngredientAmounts(String invalidAmount) {
        assertAll(
            () -> {
                Exception exception = assertThrows(RecipeException.class, 
                        () -> recipe.setAmtCoffee(invalidAmount),
                        "Setting invalid coffee amount should throw RecipeException");
                assertTrue(exception.getMessage().contains("Units of coffee must be a positive integer"));
            },
            () -> {
                Exception exception = assertThrows(RecipeException.class, 
                        () -> recipe.setAmtMilk(invalidAmount),
                        "Setting invalid milk amount should throw RecipeException");
                assertTrue(exception.getMessage().contains("Units of milk must be a positive integer"));
            },
            () -> {
                Exception exception = assertThrows(RecipeException.class, 
                        () -> recipe.setAmtSugar(invalidAmount),
                        "Setting invalid sugar amount should throw RecipeException");
                assertTrue(exception.getMessage().contains("Units of sugar must be a positive integer"));
            },
            () -> {
                Exception exception = assertThrows(RecipeException.class, 
                        () -> recipe.setAmtChocolate(invalidAmount),
                        "Setting invalid chocolate amount should throw RecipeException");
                assertTrue(exception.getMessage().contains("Units of chocolate must be a positive integer"));
            }
        );
    }

    @Test
    @DisplayName("Recipe toString should return name")
    void testToString() {
        recipe.setName("Mocha");
        assertEquals("Mocha", recipe.toString(), "toString should return recipe name");
    }

    @Test
    @DisplayName("Recipe equals should compare by name only")
    void testEquals() throws RecipeException {
        // Create two recipes with same name but different other properties
        Recipe recipe1 = new Recipe();
        recipe1.setName("Latte");
        recipe1.setPrice("5");
        recipe1.setAmtCoffee("2");
        
        Recipe recipe2 = new Recipe();
        recipe2.setName("Latte");
        recipe2.setPrice("10"); // Different price
        recipe2.setAmtCoffee("3"); // Different coffee amount
        
        // Test equality
        assertEquals(recipe1, recipe2, "Recipes with same name should be equal");
        assertEquals(recipe1.hashCode(), recipe2.hashCode(), "Equal recipes should have same hash code");
        
        // Change name and test inequality
        recipe2.setName("Cappuccino");
        assertNotEquals(recipe1, recipe2, "Recipes with different names should not be equal");
    }

    @Test
    @DisplayName("Recipe operations should complete within timeout")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void testRecipePerformance() throws RecipeException {
        // Test multiple recipe operations
        for (int i = 0; i < 1000; i++) {
            recipe.setName("Recipe " + i);
            recipe.setPrice("5");
            recipe.setAmtCoffee("3");
            recipe.setAmtMilk("1");
            recipe.setAmtSugar("1");
            recipe.setAmtChocolate("0");
            
            recipe.getName();
            recipe.getPrice();
            recipe.getAmtCoffee();
            recipe.toString();
            recipe.equals(new Recipe());
        }
    }
}

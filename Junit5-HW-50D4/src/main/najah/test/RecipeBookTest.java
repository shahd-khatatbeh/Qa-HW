package main.najah.test;

import main.najah.code.Recipe;
import main.najah.code.RecipeBook;
import main.najah.code.RecipeException;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Recipe Book Tests")
class RecipeBookTest {

    private RecipeBook recipeBook;
    private Recipe testRecipe;

    @BeforeEach
    void setUp() throws RecipeException {
        recipeBook = new RecipeBook();
        
        // Set up a test recipe
        testRecipe = new Recipe();
        testRecipe.setName("Test Coffee");
        testRecipe.setPrice("5");
        testRecipe.setAmtCoffee("3");
        testRecipe.setAmtMilk("1");
        testRecipe.setAmtSugar("1");
        testRecipe.setAmtChocolate("0");
    }

    @Test
    @DisplayName("New recipe book should have empty recipe array")
    void testNewRecipeBookHasEmptyArray() {
        Recipe[] recipes = recipeBook.getRecipes();
        
        assertNotNull(recipes, "Recipe array should not be null");
        assertEquals(4, recipes.length, "Recipe array should have length 4");
        
        for (Recipe recipe : recipes) {
            assertNull(recipe, "All recipe slots should be null initially");
        }
    }

    @Test
    @DisplayName("Adding a recipe should succeed")
    void testAddRecipe() {
        assertTrue(recipeBook.addRecipe(testRecipe), "Adding a recipe should return true");
        
        Recipe[] recipes = recipeBook.getRecipes();
        boolean found = false;
        
        for (Recipe recipe : recipes) {
            if (recipe != null && recipe.equals(testRecipe)) {
                found = true;
                break;
            }
        }
        
        assertTrue(found, "Added recipe should be in the recipe array");
    }

    @Test
    @DisplayName("Adding a duplicate recipe should fail")
    void testAddDuplicateRecipe() {        
        // Add the recipe first time
        assertTrue(recipeBook.addRecipe(testRecipe), "First addition should succeed");
        
        // Try to add the same recipe again
        assertFalse(recipeBook.addRecipe(testRecipe), "Adding duplicate recipe should return false");
        
        // Count occurrences of the recipe in the array
        Recipe[] recipes = recipeBook.getRecipes();
        int count = 0;
        
        for (Recipe recipe : recipes) {
            if (recipe != null && recipe.equals(testRecipe)) {
                count++;
            }
        }
        
        assertEquals(1, count, "Recipe should appear only once in the array");
    }

    @Test
    @DisplayName("Adding more recipes than capacity should fail")
    void testAddMoreThanCapacity() throws RecipeException {
        // Create 5 different recipes (one more than capacity)
        Recipe[] testRecipes = new Recipe[5];
        
        for (int i = 0; i < testRecipes.length; i++) {
            testRecipes[i] = new Recipe();
            testRecipes[i].setName("Recipe " + (i + 1));
            testRecipes[i].setPrice("5");
            testRecipes[i].setAmtCoffee("3");
            testRecipes[i].setAmtMilk("1");
            testRecipes[i].setAmtSugar("1");
            testRecipes[i].setAmtChocolate("0");
        }
        
        // Add the first 4 recipes
        for (int i = 0; i < 4; i++) {
            assertTrue(recipeBook.addRecipe(testRecipes[i]), 
                    "Adding recipe " + (i + 1) + " should succeed");
        }
        
        // Try to add the 5th recipe
        assertFalse(recipeBook.addRecipe(testRecipes[4]), 
                "Adding 5th recipe should fail because capacity is 4");
    }

    @Test
    @DisplayName("Deleting an existing recipe should return its name")
    void testDeleteExistingRecipe() {
        // Add a recipe
        recipeBook.addRecipe(testRecipe);
        
        // Delete the recipe
        String deletedName = recipeBook.deleteRecipe(0);
        
        assertEquals("Test Coffee", deletedName, 
                "Deleted recipe name should be returned");
        
        // Verify the recipe was replaced with an empty recipe
        Recipe[] recipes = recipeBook.getRecipes();
        assertNotNull(recipes[0], "Recipe slot should not be null after deletion");
        assertEquals("", recipes[0].getName(), "Recipe name should be empty after deletion");
    }

    @Test
    @DisplayName("Deleting a non-existent recipe should return null")
    void testDeleteNonExistentRecipe() {
        // Delete a recipe from an empty slot
        String deletedName = recipeBook.deleteRecipe(0);
        
        assertNull(deletedName, "Deleting non-existent recipe should return null");
    }

    @Test
    @DisplayName("Editing an existing recipe should return its old name")
    void testEditExistingRecipe() throws RecipeException {
        // Add a recipe
        recipeBook.addRecipe(testRecipe);
        
        // Create a new recipe
        Recipe newRecipe = new Recipe();
        newRecipe.setName("New Coffee");
        newRecipe.setPrice("10");
        newRecipe.setAmtCoffee("5");
        newRecipe.setAmtMilk("2");
        newRecipe.setAmtSugar("2");
        newRecipe.setAmtChocolate("1");
        
        // Edit the recipe
        String oldName = recipeBook.editRecipe(0, newRecipe);
        
        assertEquals("Test Coffee", oldName, "Old recipe name should be returned");
        
        // Verify the recipe was updated
        Recipe[] recipes = recipeBook.getRecipes();
        assertEquals("", recipes[0].getName(), "Recipe name should be empty after edit");
        assertEquals(10, recipes[0].getPrice(), "Recipe price should be updated after edit");
    }

    @Test
    @DisplayName("Editing a non-existent recipe should return null")
    void testEditNonExistentRecipe() throws RecipeException {
        // Create a new recipe
        Recipe newRecipe = new Recipe();
        newRecipe.setName("New Coffee");
        newRecipe.setPrice("10");
        
        // Edit an empty slot
        String oldName = recipeBook.editRecipe(0, newRecipe);
        
        assertNull(oldName, "Editing non-existent recipe should return null");
    }

    @Test
    @DisplayName("Recipe book operations should complete within timeout")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void testRecipeBookPerformance() throws RecipeException {
        // Test multiple recipe book operations
        for (int i = 0; i < 100; i++) {
            Recipe recipe = new Recipe();
            recipe.setName("Recipe " + i);
            recipe.setPrice("5");
            recipe.setAmtCoffee("3");
            
            recipeBook.addRecipe(recipe);
            recipeBook.getRecipes();
            
            if (i % 10 == 0) {
                recipeBook.deleteRecipe(0);
                recipeBook.editRecipe(1, new Recipe());
            }
        }
    }
}

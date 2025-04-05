package main.najah.test;

import main.najah.code.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Service Tests")
@Execution(ExecutionMode.CONCURRENT)
class UserServiceSimpleTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @ParameterizedTest
    @DisplayName("Valid email addresses should be validated correctly")
    @ValueSource(strings = {
        "user@example.com", 
        "firstname.lastname@domain.com",
        "email@subdomain.domain.com",
        "user.name@example.co.uk"
    })
    void testValidEmails(String email) {
        assertTrue(userService.isValidEmail(email), 
                "Email " + email + " should be considered valid");
    }

    @ParameterizedTest
    @DisplayName("Invalid email addresses should be rejected")
    @ValueSource(strings = {
        "plainaddress", 
        "without.at.sign", 
        "without.dot@domain",
        "@domain.com"
    })
    void testInvalidEmails(String email) {
        assertFalse(userService.isValidEmail(email), 
                "Email " + email + " should be considered invalid");
    }

    @ParameterizedTest
    @DisplayName("Null or empty emails should be rejected")
    @NullAndEmptySource
    void testNullOrEmptyEmails(String email) {
        assertFalse(userService.isValidEmail(email), 
                "Null or empty email should be considered invalid");
    }

    @Test
    @DisplayName("Authentication should work with correct credentials")
    void testAuthenticationSuccess() {
        assertTrue(userService.authenticate("admin", "1234"), 
                "Authentication should succeed with correct credentials");
    }

    @ParameterizedTest
    @DisplayName("Authentication should fail with incorrect credentials")
    @CsvSource({
        "admin, wrongpass",
        "wronguser, 1234",
        "wronguser, wrongpass",
        ", 1234",
        "admin, "
    })
    void testAuthenticationFailure(String username, String password) {
        assertFalse(userService.authenticate(username, password), 
                "Authentication should fail with incorrect credentials");
    }

    @Test
    @DisplayName("User service operations should complete quickly")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void testUserServicePerformance() {
        // Test both main functionalities multiple times
        for (int i = 0; i < 1000; i++) {
            userService.isValidEmail("test" + i + "@example.com");
            userService.authenticate("admin", "1234");
        }
    }
}

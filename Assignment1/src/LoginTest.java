import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoginTest {

    // TC01 - Username exactly 6 characters -> accepted
    @Test
    void validUsername_TC01() {
        Login login = new Login();
        assertTrue(login.verifyUserName("shop12"));
    }

    // TC02 - Username exactly 12 characters -> accepted
    @Test
    void validUsernameMaxBoundary_TC02() {
        Login login = new Login();
        assertTrue(login.verifyUserName("shopper12345"));
    }

    // TC03 - Username between 6 and 12 characters -> accepted
    @Test
    void validUsernameWithinRange_TC03() {
        Login login = new Login();
        assertTrue(login.verifyUserName("shopper1"));
    }

    // TC04 - Username less than 6 characters -> rejected
    @Test
    void invalidUsernameBelowMinimum_TC04() {
        Login login = new Login();
        assertFalse(login.verifyUserName("shop1"));
    }

    // TC05 - Username more than 12 characters -> rejected
    @Test
    void invalidUsernameAboveMaximum_TC05() {
        Login login = new Login();
        assertFalse(login.verifyUserName("shopper1234567"));
    }

    // TC06 - Password exactly 4 digits -> accepted
    @Test
    void validPasswordMinBoundary_TC06() {
        Login login = new Login();
        assertTrue(login.verifyPassword("1234"));
    }

    // TC07 - Password exactly 6 digits -> accepted
    @Test
    void validPasswordMaxBoundary_TC07() {
        Login login = new Login();
        assertTrue(login.verifyPassword("123456"));
    }

    // TC08 - Password between 4 and 6 digits -> accepted
    @Test
    void validPasswordWithinRange_TC08() {
        Login login = new Login();
        assertTrue(login.verifyPassword("12345"));
    }

    // TC09 - Password less than 4 digits -> rejected
    @Test
    void invalidPasswordBelowMinimum_TC09() {
        Login login = new Login();
        assertFalse(login.verifyPassword("123"));
    }

    // TC10 - Password more than 6 digits -> rejected
    @Test
    void invalidPasswordAboveMaximum_TC10() {
        Login login = new Login();
        assertFalse(login.verifyPassword("1234567"));
    }

    // TC11 - Valid username + valid password -> login successful
    @Test
    void successfulLogin_TC11() {
        Login login = new Login();
        assertTrue(login.login("shopper1", "12345"));
    }

    // TC12 - Invalid username + valid password -> login denied
    @Test
    void loginFailsInvalidUsername_TC12() {
        Login login = new Login();
        assertFalse(login.login("shop", "12345"));
    }

    // TC13 - Valid username + invalid password -> login denied
    @Test
    void loginFailsInvalidPassword_TC13() {
        Login login = new Login();
        assertFalse(login.login("shopper1", "123"));
    }

    // TC14 - Invalid username + invalid password -> login denied
    @Test
    void loginFailsInvalidUsernameAndPassword_TC14() {
        Login login = new Login();
        assertFalse(login.login("shop", "12"));
    }
}
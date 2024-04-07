import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginUserTest {

    private User user;
    private int statusCode;

    @Before
    public void setUp() {
        RestAssured.baseURI = Site.URL;
    }

    @Test
    public void testLoginUser() {
        user = new User("Mars", "marsisthegod@gmail.com", "ourancientisbetter");
        Response response = new UserActions().loginUser(user);
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertNotNull(response.jsonPath().getString("accessToken"));
        assertNotNull(response.jsonPath().getString("refreshToken"));
        assertNotNull(response.jsonPath().getString("user.email"));
        assertNotNull(response.jsonPath().getString("user.name"));
    }

    @Test
    public void testLoginUserWithWrongPassword() {
        user = new User("Mars", "marsisthegod@gmail.com", "wrongpassword");
        Response response = new UserActions().loginUser(user);
        statusCode = response.getStatusCode();
        assertEquals(401, statusCode);
        assertEquals(false, response.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect", response.jsonPath().getString("message"));
    }
}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetUsersOrdersTest {
    private OrderActions oActions;
    private UserActions uActions;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Site.URL;
        User user = new User("asasasdas", "asdasasdasd@example.com", "qwerty");
        uActions = new UserActions();
        oActions = new OrderActions();
        Response responseAccessToken = uActions.createUser(user);
        responseAccessToken.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        this.accessToken = responseAccessToken.body().jsonPath().getString("accessToken");
    }
    @Test
    public void testGetOrdersForAuthorizedUser() {
        Response response = oActions.getOrderWithToken(accessToken);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().jsonPath().getBoolean("success"));
    }

    @Test
    public void testGetOrdersForUnauthorizedUser() {
        Response response = oActions.getOrderWithoutToken();
        assertEquals(401, response.getStatusCode());
        assertFalse(response.getBody().jsonPath().getBoolean("success"));
    }
    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            uActions.deleteUser(accessToken);
        }
    }
}

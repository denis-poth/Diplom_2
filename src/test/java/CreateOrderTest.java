import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    OrderActions oActions;
    UserActions uActions;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Site.URL;
        uActions = new UserActions();
        oActions = new OrderActions();
        User user = new User("Jackie", "jackie@example.com", "qwerty");
        Response responseAccessToken = uActions.createUser(user);
        responseAccessToken.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        this.accessToken = responseAccessToken.body().jsonPath().getString("accessToken");
    }

    @Test
    public void testOrderCreationWithAuthorizationAndIngredients() {
        Response getIngredient = oActions.getIngredient();
        List<String> ingredients = new ArrayList<>(getIngredient.then().log().all().statusCode(200).extract().path("data._id"));
        Order order = new Order(ingredients);
        Response response = oActions.createOrderWithToken(order, accessToken);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    public void testOrderCreationWithoutAuthorizationAndWithIngredients() {
        Response getIngredient = oActions.getIngredient();
        List<String> ingredients = new ArrayList<>(getIngredient.then().log().all().statusCode(200).extract().path("data._id"));
        Order order = new Order(ingredients);
        Response response = oActions.createOrderWithoutToken(order);
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    public void testOrderCreationWithoutIngredients() {
        Order order = new Order(null);
        oActions.createOrderWithToken(order, accessToken)
                .then().assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    public void testOrderCreationWithInvalidIngredientHash() {
            List<String> invalidIngredients = Arrays.asList("notAValidIngredientId");
            Response response = oActions.createOrderWithInvalidIngredients(accessToken, invalidIngredients);
            assertEquals(500, response.getStatusCode());
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            uActions.deleteUser(accessToken);
        }
    }
}

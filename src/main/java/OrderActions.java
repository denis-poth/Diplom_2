import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;

public class OrderActions {

    public Response createOrderWithToken(Order order, String accessToken) {
        return given().spec(Site.baseRequestSpec())
                .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post(Site.CREATE_ORDER);
    }

    public Response createOrderWithoutToken(Order order) {
        return given().spec(Site.baseRequestSpec())
                .body(order)
                .when()
                .post(Site.CREATE_ORDER);
    }

    public Response getOrderWithToken(String accessToken) {
        return given().spec(Site.baseRequestSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(Site.CREATE_ORDER);
    }
    public Response getOrderWithoutToken() {
        return given().spec(Site.baseRequestSpec())
                .when()
                .get(Site.CREATE_ORDER)
                .thenReturn();
    }
    public Response createOrderWithInvalidIngredients(String accessToken, List<String> invalidIngredients) {
        Order order = new Order(invalidIngredients);
        return given().spec(Site.baseRequestSpec())
                .header("Authorization", accessToken)
                .body(order)
                .post(Site.CREATE_ORDER)
                .thenReturn();
    }
    public Response getIngredient() {
        return given()
                .get(Site.INGREDIENTS);
    }
}


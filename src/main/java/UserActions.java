import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class UserActions {
    public Response createUser(User user) {
        return given()
                .spec(Site.baseRequestSpec())
                .body(user)
                .post(Site.CREATE_USER)
                .thenReturn();
    }

    public Response loginUser(User user) {
        return given()
                .spec(Site.baseRequestSpec())
                .body(user)
                .post(Site.LOGIN_USER)
                .thenReturn();
    }

    public Response deleteUser(String accessToken) {
        return given()
                .spec(Site.baseRequestSpec())
                .header("Authorization", accessToken)
                .delete(Site.DELETE_USER);
    }

    public Response changeUsersDataWithoutToken(User user) {
        return given().spec(Site.baseRequestSpec())
                .body(user)
                .when()
                .patch(Site.CHANGE_USERS_DATA);
    }

    public Response changeUsersDataWithToken(User user, String accessToken) {
        return given().spec(Site.baseRequestSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(Site.CHANGE_USERS_DATA);
    }
}

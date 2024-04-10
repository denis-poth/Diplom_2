
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateNewUserTest {

    private User user;
    private String accessToken;
    UserActions actions;


    @Before
    public void setUp() {
        RestAssured.baseURI = Site.URL;
        actions = new UserActions();
    }

    @Test
    public void testCreateUser() {
        user = new User("Zeuze", "zeuze@gmail.com", "qwerty");
        Response create = actions.createUser(user);
        create.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
        Response login = actions.loginUser(user);
        login.then().assertThat()
                .body("accessToken", notNullValue())
                .and()
                .statusCode(200);
        accessToken = login.body().jsonPath().getString("accessToken");
    }
    @Test
    public void testCreateUserTwice() {
        user = new User("Mars", "marsisthegod@gmail.com", "ourancientisbetter");
        // Попытка создать пользователя повторно
        Response response = actions.createUser(user);
        response.then().assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);

        // Создаем пользователя еще раз
        Response response2 = actions.createUser(user);
        response2.then().assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    public void testCreateUserWithoutPassword() {
        user = new User("Mars", "marsisthegod@gmail.com", "");
        Response response = actions.createUser(user);
        response.then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            actions.deleteUser(accessToken);
        }
    }
}

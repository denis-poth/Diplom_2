import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;


public class ChangeUsersDataTest {

    String email = "marsisourgod@example.com";
    String password = "qwerty";
    String name = "Mars";
    String accessToken;
    UserActions actions;

    @Before
    public void setUp() {
        RestAssured.baseURI = Site.URL;
        User user = new User(name, email, password);
        actions = new UserActions();
        Response responseAccessToken = actions.createUser(user);
        responseAccessToken.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        this.accessToken = responseAccessToken.body().jsonPath().getString("accessToken");
    }

    @Test
    public void testUpdateUsersPassword() {
        // Выполнение запроса на обновление данных пользователя с авторизацией (смена пароля)
        User userNew = new User(name,email,"ytrewq");
        Response user = actions.changeUsersDataWithToken(userNew, accessToken);
        user.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void testUpdateUsersName() {
        // Выполнение запроса на обновление данных пользователя с авторизацией (смена имени)
        User userNew = new User("Zeus",email,password);
        Response user = actions.changeUsersDataWithToken(userNew, accessToken);
        user.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void testUpdateUsersEmail() {
        // Выполнение запроса на обновление данных пользователя с авторизацией (смена email)
        User userNew = new User(name,"zeusisourgod@example.com",password);
        Response user = actions.changeUsersDataWithToken(userNew, accessToken);
        user.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    public void testUpdateUsersDataWithoutToken() {
        User userNew = new User(email, password, name);
        Response userForChange = actions.changeUsersDataWithoutToken(userNew);
        userForChange.then().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            actions.deleteUser(accessToken);
        }
    }
}

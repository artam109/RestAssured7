package GoRest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class GoRestUsersTest {
    Faker randValue = new Faker();
    RequestSpecification requestSpecification;
    int id;

    @BeforeClass
    public void Setup() {
        baseURI = "https://gorest.co.in/";

        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization",
                        "Bearer 9fb621947deb2eba8c8aab04f19ba7932265a5049ddb0d0d80e38aaef7391254")
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void CreateUser() {
        Map<String, String> credentials = new HashMap<>() {{
            put("name", randValue.name().fullName());
            put("gender", "male");
            put("email", randValue.internet().emailAddress());
            put("status", "active");
        }};

        id = given()
                .spec(requestSpecification)
                .body(credentials)
                .when()
                .post("public/v2/users") // http ile başlamıyorsa baseURI geçerli
                .then().log().body()
                .statusCode(201).extract().body().path("id");
    }

    @Test(dependsOnMethods = "CreateUser")
    public void GetUserByID() {
        given()
                .spec(requestSpecification)
                .when()
                .get("public/v2/users/" + id) // http ile başlamıyorsa baseURI geçerli
                .then().log().body()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test(dependsOnMethods = "GetUserByID")
    public void UpdateUser() {
        Map<String, String> credentials = new HashMap<>() {{
            put("name", "Artam Ksor");
        }};
        given()
                .spec(requestSpecification)
                .body(credentials)
                .when()
                .put("public/v2/users/" + id) // http ile başlamıyorsa baseURI geçerli
                .then().log().body()
                .statusCode(200)
                .body("name", equalTo(credentials.get("name")));
    }


    @Test(dependsOnMethods = "UpdateUser")
    public void DeleteUser() {
        given()
                .spec(requestSpecification)
                .when()
                .delete("public/v2/users/" + id) // http ile başlamıyorsa baseURI geçerli
                .then()
                .statusCode(204);

    }

    @Test(dependsOnMethods = "DeleteUser")
    public void DeleteUserNegative() {
        given()
                .spec(requestSpecification)
                .when()
                .delete("public/v2/users/" + id) // http ile başlamıyorsa baseURI geçerli
                .then()
                .statusCode(404);

    }

}



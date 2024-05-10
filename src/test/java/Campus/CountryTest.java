package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class CountryTest {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    Map<String, String> userCredentials = new HashMap<>();

    Faker randValue = new Faker();

    String randCountryName;
    String id;

    @BeforeClass
    public void LoginCampus() {
        baseURI = "https://test.mersys.io/";


        userCredentials = new HashMap<>() {{
            put("username", "turkeyts");
            put("password", "TechnoStudy123");
            put("rememberMe", "true");
        }};

        // login ile cookies'den tokeni alacağız
        Cookies cookies =
                given()
                        .body(userCredentials).contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies() // bu method cookies döner
                ;


        requestSpecification = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void CreateCountry() {
        randCountryName = randValue.country().name() + randValue.number().digits(3);

        userCredentials = new HashMap<>() {{
            put("name", randCountryName);
            put("code", randValue.address().countryCode());
        }};


        // burada gelen token'in yine cookies içinde geri gitmesi lazım
        id = given()
                .spec(requestSpecification)
                .body(userCredentials)
                .when()
                .post("/school-service/api/countries")
                .then().log().body()
                .statusCode(201).extract().body().path("id")
        ;

    }

    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative() {
        userCredentials = new HashMap<>() {{
            put("name", randCountryName);
        }};

        given()
                .spec(requestSpecification)
                .body(userCredentials)
                .when()
                .post("/school-service/api/countries")
                .then().log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "CreateCountryNegative")
    public void UpdateCountry() {
        // yukarıda create edilen ülke adını update edin
        userCredentials = new HashMap<>() {{
            put("id", id);
            put("name", randCountryName + randValue.number().digits(3));
        }};

        given()
                .spec(requestSpecification)
                .body(userCredentials)
                .when()
                .put("/school-service/api/countries")
                .then().log().body()
                .statusCode(200)
                .body("name", equalTo(userCredentials.get("name")))
        ; // jenkinste hata için 200'den 201 yapıldı

    }

    @Test(dependsOnMethods = "UpdateCountry")
    public void DeleteCountry(){
        given()
                .spec(requestSpecification)
                .when()
                .delete("/school-service/api/countries/"+id)
                .then().log().body()
                .statusCode(200);


    }

    @Test(dependsOnMethods = "DeleteCountry")
    public void DeleteCountryNegative(){
        given()
                .spec(requestSpecification)
                .when()
                .delete("/school-service/api/countries/"+id)
                .then().log().body()
                .statusCode(400);


    }





    // TODO : CitizenShip in API testini yapınız


}

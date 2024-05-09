import Model.Location;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;


public class _03_APITestExtract {

    @Test
    public void extractingJsonPath() {

        String country = given().when().get("http://api.zippopotam.us/us/90210").then()
                .statusCode(200)
                .extract().path("country"); // PATH'i country olan bilgiyi aldık

        System.out.println("country = " + country);
        Assert.assertEquals(country, "United States");
    }

    @Test
    public void extractingJsonPath2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız

        String state = given().when().get("http://api.zippopotam.us/us/90210").then()
                .statusCode(200)
                .extract().path("places[0].state"); // PATH'i state olan bilgiyi aldık

        System.out.println("state = " + state);
        Assert.assertEquals(state, "California");
    }

    @Test
    public void extractingJsonPath3() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının place name değerinin  "Beverly Hills"
        // olduğunu testNG Assertion ile doğrulayınız

        String placeName = given().when().get("http://api.zippopotam.us/us/90210").then().log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'");

        Assert.assertEquals(placeName, "Beverly Hills");
    }

    @Test
    public void extractingJsonPath4() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.

        int limit = given().when().get("https://gorest.co.in/public/v1/users").then()
                .statusCode(200)
                .extract().path("meta.pagination.limit");

        Assert.assertEquals(limit, 10);
    }

    @Test
    public void extractingJsonPath5() {

        List<Integer> idS = given().when().get("https://gorest.co.in/public/v1/users").then()
                .statusCode(200)
                .extract().path("data.id"); // data içerisindeki tüm ID'leri LIST OLARAK döndü.


        System.out.println("idS = " + idS);
    }

    @Test
    public void extractingJsonPath6() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün name leri yazdırınız.

        List<String> names = given().when().get("https://gorest.co.in/public/v1/users").then()
                .statusCode(200)
                .extract().path("data.name");

        System.out.println("names = " + names);
    }

    @Test
    public void extractingJsonPathResponseAll() {
        // tüm json verisini aldık
        Response response = given().when().get("https://gorest.co.in/public/v1/users").then()
                .statusCode(200)
                .extract().response();

        // json'nın istediğimiz kısmına ulaşabiliriz
        List<Integer> idS = response.path("data.id");
        List<String> names = response.path("data.name");
        int limit = response.path("meta.pagination.limit");

        System.out.println("idS = " + idS);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(idS.contains(6886499));
        Assert.assertTrue(names.contains("Vedanshi Kapoor"));
        Assert.assertEquals(limit, 10);
    }


}

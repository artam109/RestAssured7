import Model.Location;
import Model.Place;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class _04_APITestPOJO {
    @Test
    public void extractJsonAll_POJO() {

        Location location = given().when().get("http://api.zippopotam.us/us/90210")
                .then().log().body()
                .statusCode(200)
                .extract().body().as(Location.class);

        System.out.println("location = " + location);

        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces().get(0).getState() = " + location.getPlaces().get(0).getState());

    }


    @Test
    public void extractPOJO_Soru() {
        // http://api.zippopotam.us/tr/01000  endpointinden dönen verilerden
        // "Dörtağaç Köyü" ait bilgileri yazdırınız
        Location location = given().when().get("http://api.zippopotam.us/tr/01000").then()
                .statusCode(200)
                .extract().body().as(Location.class);

        for (Place p : location.getPlaces()){
            if (p.getPlacename().equalsIgnoreCase("Camuzcu Köyü")){
                System.out.println("p = " + p);
            }
        }
    }
}

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _01_APITest {

    @Test
    public void test() {

        given()
                // Hazırlık kodları buraya yazılır.

                .when()
                // endpoint(url), metoduyla birlikte request gönderilir

                .then();
        // assertion test, data işlemleri

    }

    @Test
    public void statusCodeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200); // status code assertion


    }


    @Test
    public void contentTypeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200) // status code assertion
                .contentType(ContentType.JSON) // dönen datanın tipi JSON mı?
        ;

    }

    @Test
    public void checkCountryInResponseBody() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200) // status code assertion
                .body("country", equalTo("United States")) // response body dışarı almadan, variable'a atamadan
        // içeride assert yaptık
        // hamcrest kütüphanesi metodu
        ;

    }


    @Test
    public void checkCountryInResponseBody2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu doğrulayınız

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200) // status code assertion
                .body("places[0].state", equalTo("California"));
    }

    @Test
    public void checkCountryInResponseBodyHasItem() {
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .statusCode(200) // status code assertion
                .body("places.'place name'", hasItem("Dörtağaç Köyü")); // places içindeki bütün place name'ler
    }

    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1)) // places elemanı size'ı

        ; // status code assertion

    }


    @Test
    public void combininTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))

        ;

    }


    @Test
    public void pathParamTest() {

        given()
                // headers
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")

                //gönderilecek url'yi görmek için
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{country}/{zipCode}")
                .then()
                .log().body()


        ;


    }

    @Test
    public void queryParamTest() {

        given()
                .param("page", 1) // ?page=1
                .log().uri()

                .when()
                //.get("https://gorest.co.in/public/v1/users?page=3")
                .get("https://gorest.co.in/public/v1/users") // soru işaretini kendi ekliyor. Soru işaretli linklerde kullanılabilir

                .then()

                .log().body()

        ;

    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i) // ?page=1
                    .log().uri()
                    .when()
                    //.get("https://gorest.co.in/public/v1/users?page=3")
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .body("meta.pagination.page", equalTo(i));
        }
    }



}

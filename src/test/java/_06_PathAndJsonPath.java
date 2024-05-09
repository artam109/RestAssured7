import Model.Location;
import Model.Place;
import Model.UsersData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class _06_PathAndJsonPath {


    @Test
    public void extractingPath() {
        // gelen body de bilgiyi dışarı almanın 2 yöntemini gördük
        // .extract.path("")     ,   as(Todo.Class)

        String postCode =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");

        System.out.println("postCode = " + postCode);
        int postCodeInt = Integer.parseInt(postCode);
        System.out.println("postCodeInt = " + postCodeInt);
    }

    @Test
    public void extractingJosPath() {
        int postCode =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'") // tip dönüşümü otomatik, uygun tip verilmeli
                ;
        System.out.println("postCode = " + postCode);
    }

    @Test
    public void getZipCode() {
        Response response =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().response();

        Location locationAsPath = response.as(Location.class); // Bütün class yapısını body'e göre yazmak zorundayız

        // bana sadece place array'i lazım olsa bile diğer bütün body property'leri yazmak zorundayım
        System.out.println("locationAsPath.getPlaces() = " + locationAsPath.getPlaces());

        // jsonPath ile sadece ihtiyacımız olan kısmı alabiliriz.
        List<Place> places = response.jsonPath().getList("places", Place.class);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }

    // https://gorest.co.in/public/v1/users  endpointte dönen Sadece Data Kısmını POJO
    // dönüşümü ile alarak yazdırınız.

    @Test
    public void jsonPathSoru(){
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then().statusCode(200)
                        .extract().response();

        List<UsersData> dataList = response.jsonPath().getList("data", UsersData.class);
        System.out.println("dataList = " + dataList);

    }















}

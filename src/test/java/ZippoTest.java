import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {


    @Test
    public void test(){

     given()
                // hazırlık işlemlerini yapacağız (token, send body, parametreler)
             .when()
                // Link i ve metodu veriyoruz
             .that()
                // assertion ve verileri ele alma extract

     ;

    }



    @Test
    public void statusCodetest(){

     given()
                // hazırlık işlemlerini yapacağız (token, send body, parametreler)
             .when()
             .get("http://api.zippopotam.us/us/90210")

             .then()
             .log().body()  // log.All() bütün responce ı gösterir
             .statusCode(200) // status controlü
             .contentType(ContentType.JSON) // Hatalı durum kontrolünü yapalım TEXT yazdık JSON yerine
     ;

    }

    @Test
    public void contentTypeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()   // log.all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .contentType(ContentType.JSON)  // hatalı durum kontrolünü yapalım
        ;

    }

    @Test
    public void chechStateInResponseBody() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country",equalTo("United States"))
                .statusCode(200) //

        ;

    }

    //  body.counry  -> body("country")
    //  body.'post code' -> body("post code")
    //  body.'country abbreviation' -> body("country abbreviation")
    //  body.places[0].'place name' -> body("body.places[0].'place name'"]
    //  body.places[0].state -> body("places[0].state"

    @Test
    public void bodyJsonPathTest2() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state",equalTo("California")) // body.country == United States ??
                .statusCode(200) //

        ;

    }
    @Test
    public void bodyJsonPathTest3() {

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'",hasItem("Çaputçu Köyü")) // bir index verilmezse dizinin bütün elemanlarını alır
                .statusCode(200) //
        // "places.'place name'" bu bilgiler "Çaputçu Köyü" bu item e sahip mi
        ;

    }


    @Test
    public void bodyArrayHasSizeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places",hasSize(1))// verilen path deki listin size kontrolü
                .statusCode(200) //

        ;

    }

    @Test
    public void combiningTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places",hasSize(1))
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))
                .statusCode(200) //

        ;

    }

    @Test
    public void pathParamTest() {

        given()
                .pathParam("Country","us")
                .pathParam("ZipKod","90210")
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                .then()
                .log().body()

                .statusCode(200) //

        ;

    }
    @Test
    public void pathParamTest2() {
        //90210 dan 90213 ye kadar test sonuçlarında places size nın hepsinde 1 geldiğini test ediniz.

        for (int i=90210;i<=90213;i++) {
            given()
                    .pathParam("Country", "us")
                    .pathParam("ZipKod", i)
                    .log().uri()
                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200) //

            ;

        }
    }

    @Test
    public void queryParamTest() {
        //https://gorest.co.in/public/v2/users?page=1


            given()
                    .param("page", 1)
                    .log().uri()//request linki
                    .when()
                    .get("http://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page",equalTo(1))
                    .statusCode(200) //

            ;


    }

    @Test
    public void queryParamTest2() {
        //https://gorest.co.in/public/v2/users?page=1

        for (int pageNo=1; pageNo<=10; pageNo++) {
            given()
                    .param("page", pageNo)
                    .log().uri()//request linki

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pegination.page", equalTo(pageNo))
                    .statusCode(200) //

            ;

        }
    }

    RequestSpecification requestSpecs;
    ResponseSpecification responceSpecs;


    @BeforeClass
    void Setup(){
        baseURI="https://gorest.co.in/public/v1";

        requestSpecs =new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responceSpecs = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();


    }

    @Test
    public void requestResponceSpecification() {
        //https://gorest.co.in/public/v2/users?page=1


        given()
                .param("page", 1)
                .spec(requestSpecs)

                .when()
                .get("/users")// url nin başında kttp yoksa baseUri deki değer otomatik geliyor.

                .then()
                .log().body()
                .body("meta.pegination.page",equalTo(1))
                .spec(responceSpecs)

        ;


    }

    @Test
    public void extractingJsonPath() {


        String placeName=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'")
                //extract metodu ile given ile başlayan satır, bir değer döndürür hale geldi, en sonda extract olmalı

        ;

        System.out.println("placeName = " + placeName);

    }


    @Test
    public void extractingJsonPathInt() {

        int limit=

            given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit")

            ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");
    }


    @Test
    public void extractingJsonPathInt2() {

        int id=

            given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("data[2].id")

            ;
        System.out.println("id = " + id);

    }
    @Test
    public void extractingJsonPathIntList() {

        List<Integer> idler=

            given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("data.id") // data daki bütün idleri bir List şeklinde verir

            ;
        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(3045));

    }
    @Test
    public void extractingJsonPathStringList() {

        List<String> isimler=

            given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("data.name") // data daki bütün idleri bir List şeklinde verir

            ;
        System.out.println("isimler = " + isimler);
        Assert.assertTrue(isimler.contains("Datta Achari"));

    }

    @Test
    public void extractingJsonPathResponsAll() {

        Response response=

            given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().response() // bütün body alındı

            ;

        List<Integer> idler= response.path("data.id");
        List<Integer> isimler= response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("isimler = " + isimler);
        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPOJO() { // POJO : Json Objec i (Plain Old

        Location yer=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .extract().as(Location.class);  // Location şablonu
        ;

        System.out.println("yer = " +yer);

        System.out.println("yer.getCountry() = " + yer.getCountry());
        System.out.println("yer.getPlaces().get(0).getPlacename() = " + yer.getPlaces().get(0).getPlacename());
        
        

    }

}

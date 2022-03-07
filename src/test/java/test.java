import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class test {

    private String morty = "/api/character/2";

    @Tag("1api")
    @Test
    @DisplayName("Характер Морти")
    public void morti() {

        Response response1 = given()
                .baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/2")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        String morty = response1.getBody().asString();
        JSONObject mortyJson = new JSONObject(morty);

        JSONArray episodeArrayWithMorty = mortyJson.getJSONArray("episode");
        int episodeCountWithMorty = episodeArrayWithMorty.length();
        String lastEpisode = episodeArrayWithMorty.getString(episodeCountWithMorty - 1);

        System.out.println(lastEpisode);

    }

    @Tag("2api")
    @Test
    @DisplayName("test")
    public void test2() {
        String body = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "morpheus");
        requestBody.put("job", "leader");

        Response response3 = given()
                .baseUri("https://reqres.in/")
                .contentType("application/json;charset=UTF-8")
                .log().all()
                .when()
                .body(requestBody.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();

        JSONObject json = new JSONObject(response3);
        Assertions.assertEquals(json.getString("name"), "morpheus");
        Assertions.assertEquals(json.getString("job"), "leader");
    }
}

//    String resp2 = response1.getBody().asString();
//    JSONObject json = new JSONObject(resp2);
//    int count = json.getJSONObject("info").getInt("count");
//    int jsonsize = json.getJSONArray("results").length();
//    String name = json.getJSONArray("results").getJSONObject(jsonsize-1).getJSONObject("origin").getString("name");






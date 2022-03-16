import hooks.ApiHooks;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

@ExtendWith({ApiHooks.class})
public class ApiTests {

    public void compareSpecies(String morty, String character) throws Exception {
        String e1 = "Расы Морти и последнего персонажа не совпадают";
        String done = "Расы Морти и последнего персонажа совпадают";
        if (!morty.equals(character)) {
            throw new Exception(e1);
        } else System.out.println(done);
    }

    public void compareLocation(String morty, String character) throws Exception {
        String e1 = "Локации Морти и последнего персонажа не совпадают";
        String done = "Локации Морти и последнего персонажа совпадают";
        if (!morty.equals(character)) {
            throw new Exception(e1);
        } else System.out.println(done);
    }

    @Tag("api")
    @Test
    @DisplayName("Томат")
    public void potato() throws IOException {

        JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("/Users/sorokin/IdeaProjects/test_lesson_05/src/test/resources/test2.json"))));
        String name = jsonObject.getString("name");

        jsonObject.put("name", "Tomato");
        jsonObject.put("job", "Eat maket");

        Response response1 = given()
                .baseUri("https://reqres.in/")
                .contentType("application/json;charset=UTF-8")
                .when()
                .body(jsonObject.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        String tomato = response1.getBody().asString();
        JSONObject json = new JSONObject(tomato);
        Assertions.assertEquals(json.getString("name"), "Tomato");
        System.out.println("name совпадает");
        Assertions.assertEquals(json.getString("job"), "Eat maket");
        System.out.println("job совпадает");
    }

    @Tag("api")
    @Test
    @DisplayName("Морти")
    public void morti() throws Exception {

        Response response1 = given()
                .baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/2")
                .then()
                .statusCode(200)
                .extract().response();

        String morty = response1.getBody().asString();
        JSONObject mortyJson = new JSONObject(morty);

        JSONArray episodeArrayWithMorty = mortyJson.getJSONArray("episode");
        int episodeCountWithMorty = episodeArrayWithMorty.length();

        String lastEpisode = episodeArrayWithMorty.getString(episodeCountWithMorty - 1);

        System.out.println("\nПоследний эпизод в котором был Морти Смит — " + lastEpisode.substring(lastEpisode.length() - 2));

        Response response2 = given()
                .contentType(ContentType.JSON)
                .get(lastEpisode)
                .then().extract().response();

        String episode = response2.getBody().asString();
        JSONObject episodJson = new JSONObject(episode);

        JSONArray allCharactersInLastEpisode = episodJson.getJSONArray("characters");
        int charactersCount = allCharactersInLastEpisode.length();

        String lastCharacter = allCharactersInLastEpisode.getString(charactersCount - 1);

        Response response3 = given()
                .contentType(ContentType.JSON)
                .get(lastCharacter)
                .then().extract().response();

        String character = response3.getBody().asString();
        JSONObject characterJson = new JSONObject(character);
        String name = characterJson.getString("name");
        String species = characterJson.getString("species");
        String location = characterJson.getJSONObject("location").getString("name");


        System.out.println("\nПоследний персонаж в последнем эпизоде — " + name);
        System.out.println("Его раса — " + species);
        System.out.println("Его местонахождение — " + location);

        String mortySpecies = mortyJson.getString("species");
        String mortyLocation = mortyJson.getJSONObject("location").getString("name");

        System.out.println("\nРаса Морти — " + mortySpecies);
        System.out.println("Местонахождение Морти — " + mortyLocation);

        System.out.println("\nПроводим сравнения:");

        try {
            compareSpecies(species, mortySpecies);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            compareLocation(mortyLocation, location);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nСравнение завершено");
    }


}






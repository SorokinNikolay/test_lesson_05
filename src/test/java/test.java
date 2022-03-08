import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class test {

    public void compareSpecies(String morty, String character) throws Exception {
        String e1 = "Расса Морти и последнего персонажа не совпадает";
        String done = "Расса Морти и последнего персонажа совпадает";
        if(!morty.equals(character)) {throw new Exception(e1);}
        else System.out.println(done);
    }

    public void compareLocation(String morty, String character) throws Exception {
        String e1 = "Локации Морти и последнего персонажа не совпадает";
        String done = "Локации Морти и последнего персонажа совпадает";
        if(!morty.equals(character)) {throw new Exception(e1);}
        else System.out.println(done);
    }

    @Tag("1api")
    @Test
    @DisplayName("Характер Морти")
    public void morti() throws Exception {

        Response response1 = given()
                .baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/character/2")
                .then()
                .extract().response();

        String morty = response1.getBody().asString();
        JSONObject mortyJson = new JSONObject(morty);

        JSONArray episodeArrayWithMorty = mortyJson.getJSONArray("episode");
        int episodeCountWithMorty = episodeArrayWithMorty.length();

        String lastEpisode = episodeArrayWithMorty.getString(episodeCountWithMorty - 1);

        System.out.println();
        System.out.println("Последний эпизод в котором был Морти Смит — " + lastEpisode.substring(lastEpisode.length() - 2));

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
            compareSpecies(species,mortySpecies);
        }
        catch (Exception ex) {System.out.println(ex.getMessage());}

        try {
            compareLocation(mortyLocation, location);
        }
        catch (Exception ex) {System.out.println(ex.getMessage());}

        System.out.println("\nСравнение завершено");
    }
}






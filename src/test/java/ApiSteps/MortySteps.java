package ApiSteps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class MortySteps {

    @Step("Получаем информацию о Морти")
    public static String getMorty() {
        Response response1 = given()
                .baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/2")
                .then()
                .statusCode(200)
                .extract().response();

        return response1.getBody().asString();
    }

    @Step("Узнаём последний эпизод, в котором был Морти и запрашиваем информацию о нём")
    public static String getEpisode(JSONObject mortyJO) {
        JSONArray episodeArrayWithMorty = mortyJO.getJSONArray("episode");
        int episodeCountWithMorty = episodeArrayWithMorty.length();

        String lastEpisode = episodeArrayWithMorty.getString(episodeCountWithMorty - 1);
        Allure.addAttachment("Последний эпизод в котором был Морти Смит",
                lastEpisode.substring(lastEpisode.length() - 2));

        Response response2 = given()
                .contentType(ContentType.JSON)
                .get(lastEpisode)
                .then().extract().response();

        return response2.getBody().asString();
    }

    @Step("Узнаём последнего персонажа из эпизода и запрашиваем о нём информацию")
    public static String getCharacter(JSONObject lastEpisode) {

        JSONArray allCharactersInLastEpisode = lastEpisode.getJSONArray("characters");
        int charactersCount = allCharactersInLastEpisode.length();

        String lastCharacter = allCharactersInLastEpisode.getString(charactersCount - 1);

        Response response3 = given()
                .contentType(ContentType.JSON)
                .get(lastCharacter)
                .then().extract().response();

        return response3.getBody().asString();
    }

    @Step("Сраниваем рассы и локации Морти и последнего персонажа")
    public static void compare(JSONObject characterJson, JSONObject mortyJson) {

        String characterName = characterJson.getString("name");
        String characterSpecies = characterJson.getString("species");
        String characterLocation = characterJson.getJSONObject("location").getString("name");
        String CharacterInfo =
                "\nПоследний персонаж в последнем эпизоде — " + characterName
                        + "\nЕго раса — " + characterSpecies
                        + "\nЕго местонахождение — " + characterLocation;

        Allure.addAttachment("Информация о персонаже", CharacterInfo);

        String mortyName = mortyJson.getString("name");
        String mortySpecies = mortyJson.getString("species");
        String mortyLocation = mortyJson.getJSONObject("location").getString("name");
        String mortyInfo =
                "\nПолное имя Морти — " + mortyName
                        + "\nЕго раса — " + mortySpecies
                        + "\nЕго местонахождение — " + mortyLocation;

        Allure.addAttachment("Информация о Морти", mortyInfo);

        String compareResult = "";

        if(mortySpecies.equals(characterSpecies)) {
            compareResult += "Расы " + mortyName + " и " + characterName + " совпадают";
        } else compareResult += "Расы " + mortyName + " и " + characterName + " не совпадают";

        if(mortyLocation.equals(characterLocation)) {
            compareResult += "\nЛокации " + mortyName + " и " + characterName + " совпадают";
        } else compareResult += "\nЛокации " + mortyName + " и " + characterName + " не совпадают";

        Allure.addAttachment("Результат сравнения", compareResult);

    }

}

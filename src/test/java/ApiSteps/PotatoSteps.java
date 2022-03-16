package ApiSteps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static hooks.ApiHooks.getJson;
import static io.restassured.RestAssured.given;

public class PotatoSteps {

    @Step("Достаём JSONObject из файла {fileName}")
    public static JSONObject getJO(String fileName) throws IOException {
        getJson(fileName);
        return new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName))));
    }

    @Step("Назначаем в JSONObject ключу {key1} значение {value1}, а ключу {key2} значение {value2}")
    public static JSONObject putJO(JSONObject jsonObject, String key1, Object value1, String key2, Object value2) {
        jsonObject.put(key1, value1);
        jsonObject.put(key2, value2);
        return jsonObject;
    }

    @Step("Отправляем запрос на {url} и парсим ответ")
    public static JSONObject inOutJO(JSONObject jsonObject, String url) {
        Response response = given()
                .baseUri(url)
                .contentType("application/json;charset=UTF-8")
                .when()
                .body(jsonObject.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        String tomato = response.getBody().asString();
        return (new JSONObject(tomato));
    }

    @Step("Проверяем, содержит ли вернувшийся JSONObject нужные значения по ключам")
    public static void assertJO(JSONObject jsonObject) {
        String result;
        if (jsonObject.getString("name").equals("Tomato")
                && jsonObject.getString("job").equals("Eat maket")) {
            result = "Значения совпадают";
        } else result = "Значения не совпрадают";
        Allure.addAttachment("Результат проверки", result);
    }
}

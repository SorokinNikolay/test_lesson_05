package hooks;

import io.qameta.allure.Attachment;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiHooks implements BeforeAllCallback {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if(!started) {
            started = true;
            RestAssured.filters(new AllureRestAssured());
        }
    }

    @Attachment(value = "Читаемый Json файл", type = "application/json", fileExtension = ".txt")
    public static byte[] getJson(String resourceName) throws IOException {
        return Files.readAllBytes(Paths.get("src/test/resources", resourceName));
    }
}

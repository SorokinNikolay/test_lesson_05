import hooks.ApiHooks;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static ApiSteps.MortySteps.*;
import static ApiSteps.PotatoSteps.*;

@ExtendWith({ApiHooks.class})
public class ApiTests {

    @Tag("api")
    @Test
    @DisplayName("Томат")
    public void potato() throws IOException {

        JSONObject jsonObject = getJO("test2.json");
        putJO(jsonObject,"name","Tomato","job", "Eat maket");
        JSONObject responseJO = inOutJO(jsonObject,"https://reqres.in/");
        assertJO(responseJO);
    }

    @Tag("api")
    @Test
    @DisplayName("Морти")
    public void morti() {

        JSONObject mortyJson = new JSONObject(getMorty());
        JSONObject episodJson = new JSONObject(getEpisode(mortyJson));
        JSONObject characterJson = new JSONObject(getCharacter(episodJson));
        compare(characterJson, mortyJson);
    }


}






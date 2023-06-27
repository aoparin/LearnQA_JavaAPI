import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostTests {

    @Test
    public void postSecretPasswordTest() {

        ArrayList<String> passwordList = new ArrayList<>();
        passwordList.add("123456");
        passwordList.add("123456789");
        passwordList.add("qwerty");
        passwordList.add("password");
        passwordList.add("1234567");
        passwordList.add("12345678");
        passwordList.add("12345");
        passwordList.add("iloveyou");
        passwordList.add("111111");
        passwordList.add("123123");
        passwordList.add("abc123");
        passwordList.add("qwerty123");
        passwordList.add("q2w3e4r");
        passwordList.add("admin");
        passwordList.add("qwertyuiop");
        passwordList.add("654321");
        passwordList.add("555555");
        passwordList.add("lovely");
        passwordList.add("7777777");
        passwordList.add("welcome");
        passwordList.add("888888");
        passwordList.add("princess");
        passwordList.add("dragon");
        passwordList.add("password1");
        passwordList.add("123qwe");
        //¯\_(ツ)_/¯

        for (String password : passwordList) {

            Response postResponse = RestAssured
                    .given()
                    .queryParam("login", "super_admin")
                    .queryParam("password", password)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String authCookie = postResponse.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", authCookie);

            Response getResponse = RestAssured
                    .given()
                    .cookies(cookies)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String response = getResponse.asString();

            if (response.equals("You are authorized")) {
                System.out.println("Response: " + response + "\nPassword: " + password);
                break;
            }
        }
    }
}

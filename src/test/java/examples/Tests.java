package examples;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {

    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"})
    public void testHelloMethodWithoutName(String name){
        Map<String, String> queryParams = new HashMap<>();

        if (name.length() > 0){
            queryParams.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = (name.length() > 0) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "The answer is ont expected");
    }

    @Test
    public void stringLengthTest(){
        String text = "Test text";
        assertTrue(text.length() > 15, "String length should be greater than 15");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"})

    public void userAgentCheckTest(String userAgent){

        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);

        JsonPath response = RestAssured
                .given()
                .headers(header)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        if ((userAgent.equals("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"))){
            assertEquals("Mobile", response.getString("platform"), "UserAgent = " + userAgent + "\nplatform value is not match");
            assertEquals("No", response.getString("browser"), "UserAgent = " + userAgent + "\nbrowser value is not match");
            assertEquals("Android", response.getString("device"), "UserAgent = " + userAgent + "\ndevice value is not match");
        }

        else if ((userAgent.equals("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1"))){
            assertEquals("Mobile", response.getString("platform"), "UserAgent = " + userAgent + "\nplatform value is not match");
            assertEquals("Chrome", response.getString("browser"), "UserAgent = " + userAgent + "\nbrowser value is not match");
            assertEquals("iOS", response.getString("device"), "UserAgent = " + userAgent + "\ndevice value is not match");
        }

        else if ((userAgent.equals("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"))){
            assertEquals("Googlebot", response.getString("platform"), "UserAgent = " + userAgent + "\nplatform value is not match");
            assertEquals("Unknown", response.getString("browser"), "UserAgent = " + userAgent + "\nbrowser value is not match");
            assertEquals("Unknown", response.getString("device"), "UserAgent = " + userAgent + "\ndevice value is not match");
        }

        else if ((userAgent.equals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0"))){
            assertEquals("Web", response.getString("platform"), "UserAgent = " + userAgent + "\nplatform value is not match");
            assertEquals("Chrome", response.getString("browser"), "UserAgent = " + userAgent + "\nbrowser value is not match");
            assertEquals("No", response.getString("device"), "UserAgent = " + userAgent + "\ndevice value is not match");
        }

        else if ((userAgent.equals("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"))){
            assertEquals("Mobile", response.getString("platform"), "UserAgent = " + userAgent + "\nplatform value is not match");
            assertEquals("No", response.getString("browser"), "UserAgent = " + userAgent + "\nbrowser value is not match");
            assertEquals("iPhone", response.getString("device"), "UserAgent = " + userAgent + "\ndevice value is not match");
        }
    }

    @Test
    public void homeworkHeaderTest(){
        Response response = RestAssured.given().get("https://playground.learnqa.ru/api/homework_header").andReturn();
        assertEquals("Some secret value", response.getHeader("X-Secret-Homework-Header"), "Value does not match expected");
    }

    @Test
    public void homeworkCookieTest(){
        Response response = RestAssured.given().get("https://playground.learnqa.ru/api/homework_cookie").andReturn();
        assertEquals("hw_value", response.getCookie("HomeWork"), "Value does not match expected");
    }
}

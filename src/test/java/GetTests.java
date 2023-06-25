import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class GetTests {

    private int statusCode;

    @Test
    public void getTextTest(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void getJsonParsingTest(){
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String secondMessage = response.get("messages.message[1]");
        System.out.println(secondMessage);
    }

    @Test
    public void getRedirectHeaderTest(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String locationHeaders = response.getHeader("Location");
        System.out.println(locationHeaders);
    }

    @Test
    public void getLongRedirectHeaderTest(){

        String url = "https://playground.learnqa.ru/api/long_redirect";
        int redirectCount = 0;
        int statusCode = this.statusCode;

        while(statusCode != 200){
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();
            String locationHeaders = response.getHeader("Location");
            statusCode = response.statusCode();
            if (statusCode == 301){
                System.out.println("url: " + locationHeaders);
                url = locationHeaders;
                redirectCount++;
            }
            else if (statusCode == 200) {
                System.out.println("Number of redirects: " + redirectCount);
            }
            else if(statusCode !=200 && statusCode !=301){
                System.out.println("Unexpected status code: " + statusCode);
                break;
            }
        }
    }

    @Test
    public void getLongTimeJobTest() throws InterruptedException {

        String status;

        JsonPath responseWithoutParam = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        Integer seconds = responseWithoutParam.get("seconds");
        String token = responseWithoutParam.get("token");

        JsonPath responseWithParam = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        status = responseWithParam.get("status");

        if (status.equals("Job is NOT ready")) {
            Thread.sleep(seconds * 1000);
            JsonPath responseWithParamAfterWait = RestAssured
                    .given()
                    .queryParam("token", token)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            status = responseWithParamAfterWait.get("status");

         if (status.equals("Job is ready")){
             String result = responseWithParamAfterWait.get("result");
             System.out.println("Status: " + status + "\nResult: " + result);
            }
        }
    }
}

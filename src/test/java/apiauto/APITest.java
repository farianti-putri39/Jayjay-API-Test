package apiauto;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class APITest {

    @Test
    public void getUserTest(){
        RestAssured.baseURI = "https://reqres.in";
        given().when().get("api/users?page=1")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("data.id", Matchers.hasSize(6));
    }

    @Test
    public void createNewUserTest(){
        RestAssured.baseURI = "https://reqres.in";
        String name = "Ann";
        String job = "Student";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("job", job);
        given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(jsonObject.toString())
                .post("api/users")
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(name))
                .assertThat().body("job", Matchers.equalTo(job))
                .assertThat().body("$", Matchers.hasKey("id"))
                .assertThat().body("$", Matchers.hasKey("createdAt"));
    }

    @Test
    public void userPutTest(){
        RestAssured.baseURI = "https://reqres.in";
        int userPut = 2;
        String newName = "updatedUser";

        String fName = given().when().get("api/users/"+userPut).getBody().jsonPath().get("data.first_name");
        String lName = given().when().get("api/users/"+userPut).getBody().jsonPath().get("data.last_name");
        String avatar = given().when().get("api/users/"+userPut).getBody().jsonPath().get("data.avatar");
        String email = given().when().get("api/users/"+userPut).getBody().jsonPath().get("data.email");

        System.out.println("Name before: "+fName);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", userPut);
        bodyMap.put("email", email);
        bodyMap.put("avatar", avatar);
        bodyMap.put("first_name", newName);
        bodyMap.put("last_name", lName);
        JSONObject jsonObject = new JSONObject (bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("api/users/"+userPut)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));
    }

    @Test
    public void userPatchTest(){
        RestAssured.baseURI = "https://reqres.in/";
        int userPatch = 3;
        String newName = "updatedUser";
        String fName = given().when().get("api/users/"+userPatch).getBody().jsonPath().get("data.first_name");
        System.out.println("Name before: "+fName);

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("first_name", newName);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("api/users/"+userPatch)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));
    }

    @Test
    public void userDeleteTest(){
        RestAssured.baseURI = "https://reqres.in";
        int userDel = 2;
        given().log().all()
                .when().delete("api/users/"+userDel)
                .then()
                .log().all()
                .assertThat().statusCode(204);
    }

    @Test
    public void testValidateJsonSchemaSingleUser(){
        RestAssured.baseURI = "https://reqres.in";
        int userGet = 5;

        File file = new File("src/test/resources/jsonSchema/GetSingleUserSchema.json");

        given().when().get("/api/users/"+userGet)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
    }
}

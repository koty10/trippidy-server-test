package dev.koten.trippidy;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.koten.trippidy.dto.MemberDto;
import dev.koten.trippidy.dto.TripDto;
import dev.koten.trippidy.dto.UserProfileDto;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TripResourceTest {
    private static final Logger log = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static RequestSpecification request;
    private static ObjectMapper objectMapper;
    private static String idToken;
    private static String userId;
    private static String userEmail;

    @BeforeAll
    static void setup() throws IOException {
        objectMapper = new ObjectMapper();

        // Setup request properties
        RestAssured.port = Integer.valueOf(9680);
        RestAssured.basePath = "/api/v1/";
        RestAssured.baseURI = "https://trippidy.koten.dev";

        String configFilePath = "src/main/resources/trippidy.properties";
        FileInputStream propsInput = new FileInputStream(configFilePath);
        Properties prop = new Properties();
        prop.load(propsInput);

        // Login test user
        AuthAPI auth = AuthAPI.newBuilder(prop.getProperty("DOMAIN"), prop.getProperty("CLIENT_ID"), prop.getProperty("CLIENT_SECRET")).build();
        var tokenRequest = auth.login("test@example.com", "Diplomka123".toCharArray());
        idToken = tokenRequest.execute().getBody().getIdToken();

        var decodedIdToken = JWT.decode(idToken);
        userId = decodedIdToken.getClaim("sub").asString();
        userEmail = decodedIdToken.getClaim("email").asString();
        //log.info("idToken: " + idToken);

        request = RestAssured.given()
                .auth()
                .oauth2(idToken)
                .log().all();
    }

    @DisplayName("Get Trips should return 200")
    @Test
    @Order(1)
    void getTripsShouldReturn200Test() {
        RestAssured.given()
                .auth()
                .oauth2(idToken)
                .when()
                .get("/my/trip")
                .then()
                .statusCode(200);
    }

    @DisplayName("Get UserProfile should return 200")
    @Test
    @Order(2)
    void getUserProfileShouldReturn200Test() {
        RestAssured.given()
                .auth()
                .oauth2(idToken)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200);
    }

    @DisplayName("Register new UserProfile should return created UserProfile")
    @Test
    @Order(3)
    void registerNewUserProfileShouldReturnCreatedUserProfileTest() {
        RestAssured.given()
                .auth()
                .oauth2(idToken)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId));
    }

    @DisplayName("Edit UserProfile should return updated UserProfile")
    @Test
    @Order(4)
    void editUserProfileShouldReturnUpdatedUserProfileTest() throws JsonProcessingException {
        UserProfileDto userProfile = new UserProfileDto();
        userProfile.setId(userId);
        userProfile.setEmail(userEmail);
        userProfile.setFirstname("John");
        userProfile.setLastname("Doe");
        userProfile.setMembers(new ArrayList<>());
        userProfile.setBankAccountNumber("");

        RestAssured.given()
                .auth()
                .oauth2(idToken)
                .contentType("application/json; charset=UTF-8")
                .when()
                .body(objectMapper.writeValueAsString(userProfile))
                .put("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userProfile.getId()))
                .body("email", equalTo(userProfile.getEmail()));
    }

    @DisplayName("Get existing UserProfile should return UserProfile")
    @Test
    @Order(5)
    void getExistingUserProfileShouldReturnUserProfileTest() {
        RestAssured.given()
                .auth()
                .oauth2(idToken)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("email", equalTo(userEmail));
    }

    @DisplayName("Create Trip should return created Trip")
    @Test
    @Order(6)
    void createTripShouldReturnCreatedTripTest() throws JsonProcessingException {
        MemberDto member = new MemberDto();
        member.setUserProfileId(userId);
        member.setId("1");
        member.setRole("admin");
        member.setItems(new ArrayList<>());
        member.setTripId("1");
        member.setAccepted(true);

        TripDto trip = new TripDto();
        trip.setId("1");
        trip.setCompletedTransactions(new ArrayList<>());
        trip.setMembers(List.of(member));
        trip.setName("Test trip");

        RestAssured.given()
                .auth()
                .oauth2(idToken)
                .contentType("application/json; charset=UTF-8")
                .when()
                .body(objectMapper.writeValueAsString(trip))
                .post("/my/trip")
                .then()
                .statusCode(200)
                .body("id", equalTo(trip.getId()));
    }
}

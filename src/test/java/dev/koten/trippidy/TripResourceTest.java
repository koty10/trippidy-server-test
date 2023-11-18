package dev.koten.trippidy;

import com.auth0.client.auth.AuthAPI;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import dev.koten.trippidy.dto.ItemDto;
import dev.koten.trippidy.dto.MemberDto;
import dev.koten.trippidy.dto.TripDto;
import dev.koten.trippidy.dto.UserProfileDto;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import net.jodah.failsafe.internal.util.Assert;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TripResourceTest {
    private static final Logger log = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static Jsonb jsonb;
    private static String idToken1;
    private static String idToken2;
    private static String userId1;
    private static String userEmail1;
    private static String userId2;
    private static String userEmail2;
    private static String memberId1;
    private static String memberId2;
    private static String tripId;
    private static String tripName = "Test trip";

    @BeforeAll
    static void setup() throws IOException {
        jsonb = JsonbBuilder.create();

        // Setup request properties
        RestAssured.port = 9680;
        RestAssured.basePath = "/api/v1/";
        RestAssured.baseURI = "https://trippidy.koten.dev";

        String configFilePath = "src/main/resources/trippidy.properties";
        FileInputStream propsInput = new FileInputStream(configFilePath);
        Properties prop = new Properties();
        prop.load(propsInput);

        // Login test user
        AuthAPI auth = AuthAPI.newBuilder(prop.getProperty("DOMAIN"), prop.getProperty("CLIENT_ID"), prop.getProperty("CLIENT_SECRET")).build();
        var tokenRequest1 = auth.login("test@example.com", "Diplomka123".toCharArray());
        var tokenRequest2 = auth.login("test2@example.com", "Heslo123".toCharArray());
        idToken1 = tokenRequest1.execute().getBody().getIdToken();
        idToken2 = tokenRequest2.execute().getBody().getIdToken();

        var decodedIdToken1 = JWT.decode(idToken1);
        userId1 = decodedIdToken1.getClaim("sub").asString();
        userEmail1 = decodedIdToken1.getClaim("email").asString();

        var decodedIdToken2 = JWT.decode(idToken2);
        userId2 = decodedIdToken2.getClaim("sub").asString();
        userEmail2 = decodedIdToken2.getClaim("email").asString();
        //log.info("idToken: " + idToken);

//        request = RestAssured.given()
//                .auth()
//                .oauth2(idToken)
//                .log().all();
    }

    // First user

    @DisplayName("Get Trips should return 200")
    @Test
    @Order(1)
    void getTripsShouldReturn200Test() {
        RestAssured.given()
                .auth()
                .oauth2(idToken1)
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
                .oauth2(idToken1)
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
                .oauth2(idToken1)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId1));
    }

    @DisplayName("Edit UserProfile should return updated UserProfile")
    @Test
    @Order(4)
    void editUserProfileShouldReturnUpdatedUserProfileTest() throws JsonProcessingException {
        UserProfileDto userProfile = new UserProfileDto();
        userProfile.setId(userId1);
        userProfile.setEmail(userEmail1);
        userProfile.setFirstname("John");
        userProfile.setLastname("Doe");
        userProfile.setMembers(new ArrayList<>());
        userProfile.setBankAccountNumber("");

        RestAssured.given()
                .auth()
                .oauth2(idToken1)
                .contentType("application/json; charset=UTF-8")
                .when()
                .body(jsonb.toJson(userProfile))
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
                .oauth2(idToken1)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId1))
                .body("email", equalTo(userEmail1));
    }

    @DisplayName("Create Trip should return created Trip")
    @Test
    @Order(6)
    void createTripShouldReturnCreatedTripTest() throws JsonProcessingException {
        MemberDto member = new MemberDto();
        member.setUserProfileId(userId1);
        member.setRole("admin");
        member.setItems(new ArrayList<>());
        member.setAccepted(true);

        TripDto trip = new TripDto();
        trip.setCompletedTransactions(new ArrayList<>());
        trip.setMembers(List.of(member));
        trip.setName(tripName);

        var res = RestAssured.given()
                .auth()
                .oauth2(idToken1)
                .contentType("application/json; charset=UTF-8")
                .log().all()
                .when()
                .body(jsonb.toJson(trip))
                .post("/my/trip");

        res.then()
                .statusCode(200)
                .body("id", notNullValue());

        var resultTrip =  jsonb.fromJson(res.getBody().asString(), TripDto.class);

        tripId = resultTrip.getId();
        memberId1 = resultTrip.getMembers().stream().findFirst().get().getId();
    }

    @DisplayName("Create Item should return created Item")
    @Test
    @Order(7)
    void createItemShouldReturnCreatedItemTest() {
        ItemDto item = new ItemDto();
        item.setName("Test item");
        item.setMemberId(memberId1);
        item.setPrice(BigDecimal.ZERO);
        item.setCategoryName("Test category");

        RestAssured.given()
                .auth()
                .oauth2(idToken1)
                .contentType("application/json; charset=UTF-8")
                .when()
                .log().all()
                .body(jsonb.toJson(item))
                .post("/my/item")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @DisplayName("Create Shared Item should return created Item")
    @Test
    @Order(8)
    void createSharedItemShouldReturnCreatedItemTest() {
        ItemDto item = new ItemDto();
        item.setName("Test item shared");
        item.setMemberId(memberId1);
        item.setPrice(BigDecimal.ZERO);
        item.setCategoryName("Test category");
        item.setShared(true);

        RestAssured.given()
                .auth()
                .oauth2(idToken1)
                .contentType("application/json; charset=UTF-8")
                .when()
                .log().all()
                .body(jsonb.toJson(item))
                .post("/my/item")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @DisplayName("Create Private Item should return created Item")
    @Test
    @Order(9)
    void createPrivateItemShouldReturnCreatedItemTest() {
        ItemDto item = new ItemDto();
        item.setName("Test item private");
        item.setMemberId(memberId1);
        item.setPrice(BigDecimal.ZERO);
        item.setCategoryName("Test category");
        item.setPrivate(true);

        RestAssured.given()
                .auth()
                .oauth2(idToken1)
                .contentType("application/json; charset=UTF-8")
                .when()
                .log().all()
                .body(jsonb.toJson(item))
                .post("/my/item")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue());
    }

    // Second user

    @DisplayName("Register second UserProfile should return created UserProfile")
    @Test
    @Order(10)
    void registerSecondUserProfileShouldReturnCreatedUserProfileTest() {
        RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId2));
    }

    @DisplayName("Edit second UserProfile should return updated UserProfile")
    @Test
    @Order(11)
    void editSecondUserProfileShouldReturnUpdatedUserProfileTest() throws JsonProcessingException {
        UserProfileDto userProfile = new UserProfileDto();
        userProfile.setId(userId2);
        userProfile.setEmail(userEmail2);
        userProfile.setFirstname("Second");
        userProfile.setLastname("User");
        userProfile.setMembers(new ArrayList<>());
        userProfile.setBankAccountNumber("");

        RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .contentType("application/json; charset=UTF-8")
                .when()
                .body(jsonb.toJson(userProfile))
                .put("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userProfile.getId()))
                .body("email", equalTo(userProfile.getEmail()));
    }

    @DisplayName("Get second existing UserProfile should return UserProfile")
    @Test
    @Order(12)
    void getSecondExistingUserProfileShouldReturnUserProfileTest() {
        RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .when()
                .get("/my/userProfile")
                .then()
                .statusCode(200)
                .body("id", equalTo(userId2))
                .body("email", equalTo(userEmail2));
    }

    // First user

    @DisplayName("Create a Member invitation should return created Member")
    @Test
    @Order(13)
    void createMemberInvitationShouldReturnCreatedMemberTest() {
        MemberDto member = new MemberDto();
        member.setUserProfileId(userId2);
        member.setRole("member");
        member.setItems(new ArrayList<>());
        member.setTripId(tripId);
        member.setAccepted(false);

        var res = RestAssured.given()
                .auth()
                .oauth2(idToken1)
                .contentType("application/json; charset=UTF-8")
                .when()
                .body(jsonb.toJson(member))
                .post("/my/member");

        res.then()
                .statusCode(200)
                .body("id", notNullValue());

        var resultMember =  jsonb.fromJson(res.getBody().asString(), MemberDto.class);
        memberId2 = resultMember.getId();
    }

    // Second user

    @DisplayName("Accept a Member invitation should return accepted Member object")
    @Test
    @Order(14)
    void AcceptMemberInvitationShouldReturnAcceptedMemberObjectTest() {
        MemberDto member = new MemberDto();
        member.setUserProfileId(userId2);
        member.setId(memberId2);
        member.setRole("member");
        member.setItems(new ArrayList<>());
        member.setTripId(tripId);
        member.setAccepted(true);

        var res = RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .contentType("application/json; charset=UTF-8")
                .when()
                .body(jsonb.toJson(member))
                .put("/my/member")
                .then()
                .statusCode(200)
                .body("id", equalTo(member.getId()))
                .body("accepted", equalTo(true));
    }

    @DisplayName("Get Trips should return a list of Trips containing a new trip")
    @Test
    @Order(15)
    void getTripsShouldReturnListOfTripsContainingNewTripTest() {
        var res = RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .when()
                .get("/my/trip");
        res.then()
                .statusCode(200);
        List<TripDto> resultTrips =  jsonb.fromJson(res.getBody().asString(), new ArrayList<TripDto>(){}.getClass().getGenericSuperclass());
        Assertions.assertTrue(resultTrips.stream().anyMatch(trip -> trip.getName().equals(tripName)));
    }

    @DisplayName("Second user can see public items of other members")
    @Test
    @Order(16)
    void SecondUserCanSeePublicItemsOfOtherMembersTest() {
        var res = RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .when()
                .get("/my/trip");
        res.then()
                .statusCode(200);
        List<TripDto> resultTrips =  jsonb.fromJson(res.getBody().asString(), new ArrayList<TripDto>(){}.getClass().getGenericSuperclass());
        var resultTrip = resultTrips.get(resultTrips.size() - 1);
        var firstMember = resultTrip.getMembers().stream().filter(member -> member.getUserProfileId().equals(userId1)).findFirst().get();
        Assertions.assertTrue(firstMember.getItems().stream().anyMatch(ItemDto::isShared));
        Assertions.assertTrue(firstMember.getItems().stream().anyMatch(item -> !item.isPrivate() && !item.isShared()));
    }

    @DisplayName("Second user can not see private items of other members")
    @Test
    @Order(16)
    void SecondUserCanNotSeePrivateItemsOfOtherMembersTest() {
        var res = RestAssured.given()
                .auth()
                .oauth2(idToken2)
                .when()
                .get("/my/trip");
        res.then()
                .statusCode(200);
        List<TripDto> resultTrips =  jsonb.fromJson(res.getBody().asString(), new ArrayList<TripDto>(){}.getClass().getGenericSuperclass());
        var resultTrip = resultTrips.get(resultTrips.size() - 1);
        var firstMember = resultTrip.getMembers().stream().filter(member -> member.getUserProfileId().equals(userId1)).findFirst().get();
        Assertions.assertFalse(firstMember.getItems().stream().anyMatch(ItemDto::isPrivate));
    }

}

package org.amitstein;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import org.junit.jupiter.api.Test;
import org.Profile1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestProfile(Profile1.class)
class IPResourceTest {

  @Test
    void testIPDoesNotExist() {
        given()
          .when().get("/check-ip?ip=127.0.0.1")
          .then()
             .statusCode(200)
             .body(is("false"));
    }

    @Test
    void testIPExists() {
        given()
          .when().get("/check-ip?ip=45.155.91.99")
          .then()
             .statusCode(200)
             .body(is("true"));
    }
}
package config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestAssuredConfig {
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    
    public static void setup() {
        // konfiguracja z ApiBaseTest
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost:3000/api")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
                
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
    
    public static RequestSpecification getRequestSpec() {
        if (requestSpec == null) setup();
        return requestSpec;
    }
    
    public static ResponseSpecification getResponseSpec() {
        if (responseSpec == null) setup();
        return responseSpec;
    }
} 
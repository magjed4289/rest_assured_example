package tests;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tests.utils.ConfigFileReader;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BlogPostingTest {

    private final ConfigFileReader config = ConfigFileReader.getInstance();
    private final String siteId = config.getConfiguration().getSiteId();


    @Before
    public void setup() {
        RestAssured.baseURI = config.getConfiguration().getUri();
        RestAssured.port =  config.getConfiguration().getPort();
        RestAssured.basePath = config.getConfiguration().getHeadlessDeliveryUrl();
        RestAssured.authentication = preemptive().basic(config.getConfiguration().getEmail(), config.getConfiguration().getPassword());
    }

    @After
    public void tearDown() {
        RestAssured.reset();
    }

    @Test
    public void
    getSiteBlogPostingsPage_returns_200() {
        when().
                get("/sites/"+siteId+"/blog-postings").
        then().log().ifValidationFails().
                statusCode(400);
    }

    @Test
    public void
    getSiteBlogPostingsPage_returns_correct_body_values() {
        given().
                when().
                get("/sites/"+siteId+"/blog-postings").
        then().
                body("totalCount",equalTo(0));
    }

    @Test
    public void
    getSiteBlogPostingsPage_matches_schemas() {
        when().
                get("/sites/"+siteId+"/blog-postings").
        then().
                body(matchesJsonSchemaInClasspath("schemas/PageBlogPosting.json")).
                //body(matchesJsonSchemaInClasspath("schemas/BlogPosting.json")).
                body(matchesJsonSchemaInClasspath("schemas/Facet.json"));
    }

    @Test
    public void
    getSiteBlogPostingsPage_response_time() {
        given().header("Accept", "application/json").
                when().
                get("/sites/"+siteId+"/blog-postings").
        then().
                time(lessThan(5000L));
    }
}

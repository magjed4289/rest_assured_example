package tests;

import io.restassured.RestAssured;
import org.json.JSONObject;
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
                statusCode(200);
    }

    @Test
    public void
    getSiteBlogPostingsPage_matches_schemas() {
        when().
                get("/sites/"+siteId+"/blog-postings").
        then().log().body().
                    body(matchesJsonSchemaInClasspath("schemas/BlogPosting.json")).
                    body(matchesJsonSchemaInClasspath("schemas/PageBlogPosting.json"));

    }

    @Test
    public void
    post_blog_posting_with_required_fields_matches_schemas() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("articleBody", "body1");
        requestParams.put("headline", "body1");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toString()).
        when().
                post("/sites/"+siteId+"/blog-postings").
        then().log().body().
                body(matchesJsonSchemaInClasspath("schemas/PageBlogPosting.json")).
                body(matchesJsonSchemaInClasspath("schemas/BlogPosting.json"));
    }

    @Test
    public void
    getSiteBlogPostingsPage_returns_correct_body_values() {
        when().
                get("/sites/"+siteId+"/blog-postings").
        then().
                body("totalCount",equalTo(1));
    }

    @Test
    public void
    getSiteBlogPostingsPage_response_time() {
        given().header("Accept", "application/json").
        when().
                get("/sites/"+siteId+"/blog-postings").
        then().
                time(lessThan(1200L));
    }
}

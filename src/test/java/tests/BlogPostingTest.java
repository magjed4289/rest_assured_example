package tests;

import org.junit.Test;
import tests.utils.ConfigFileReader;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BlogPostingTest {

    private final ConfigFileReader config = ConfigFileReader.getInstance();

    @Test
    public void
    getSiteBlogPostingsPage_returns_200() {

        given().
                auth()
                .preemptive()
                .basic(config.getConfiguration().getEmail(), config.getConfiguration().getPassword())
                .header("Accept", "application/json").

        when().
                get(config.getConfiguration().getHeadlessDeliveryUrl()+"/sites/"+config.getConfiguration().getSiteId()+"/blog-postings").

        then().
                statusCode(200);
    }

    @Test
    public void
    getSiteBlogPostingsPage_returns_correct_body_values() {

        given().
                auth().
                preemptive().
                basic(config.getConfiguration().getEmail(), config.getConfiguration().getPassword()).
                header("Accept", "application/json").
        when().
                get(config.getConfiguration().getHeadlessDeliveryUrl()+"/sites/"+config.getConfiguration().getSiteId()+"/blog-postings").
        then().
                body("totalCount",equalTo(0));
    }

    @Test
    public void
    getSiteBlogPostingsPage_matches_schemas() {

        given().
                auth().
                preemptive().
                basic(config.getConfiguration().getEmail(), config.getConfiguration().getPassword()).
                header("Accept", "application/json").
        when().
                get(config.getConfiguration().getHeadlessDeliveryUrl()+"/sites/"+config.getConfiguration().getSiteId()+"/blog-postings").
        then().
                body(matchesJsonSchemaInClasspath("schemas/PageBlogPosting.json")).
                //body(matchesJsonSchemaInClasspath("schemas/BlogPosting.json")).
                body(matchesJsonSchemaInClasspath("schemas/Facet.json"));
    }
}
